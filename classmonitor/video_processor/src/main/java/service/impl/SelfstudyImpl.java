package service.impl;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.entity.Behavior;
import service.entity.study.Alarm;
import service.entity.study.Pattern;
import service.entity.study.TempState;
import service.model.ActionInfo;
import service.model.InitInfo;
import service.model.StateHelper;
import service.repository.BehaviorRepository;
import service.repository.StudentRepository;
import service.repository.study.AlarmRepository;
import service.repository.study.PatternRepository;
import service.repository.study.TempStateRepository;
import service.service.SelfstudyService;
import service.tool.SeatHelper;
import sun.applet.Main;

@Service
public class SelfstudyImpl implements SelfstudyService {
	@Autowired
	private AlarmRepository alarm;
	@Autowired
	private PatternRepository pattern;
	@Autowired
	private TempStateRepository tempstate;
	@Autowired
	private BehaviorRepository behavior;
	@Autowired
	private StudentRepository stu_repo;

	HashMap alarm_value = new HashMap() {
		{
			put("正常", 0.0);
			put("迟到", 1.0);
			put("缺席", 2.0);
			put("离开", 3.0);
			put("早退", 3.0);
			put("站立", 1.0);
			put("游荡", 4.0);
			put("睡觉", 1.0);
		}
	};
	public static final double alarm_stan = 6.0;

	@Override
	public void test1960() throws Exception {
		// TODO Auto-generated method stub
		int num = 1960;
		int cid = 2;
		int max_seq = tempstate.getMaxSeq(num, cid);
		int sequence = 1;
		int sum = 16;
		ArrayList<StateHelper> list = new ArrayList<StateHelper>();
		for (int i = 0; i < 16; i++) {
			StateHelper temp = new StateHelper();
			temp.setCid(cid);
			temp.setSid(i + 1);
			list.add(temp);
		}
		int alarm_id = 0;
		int alarm_num = 0;
		for (; sequence <= max_seq; sequence++) {
			List<TempState> state_list = tempstate.findByNumAndCidAndSequence(num, cid, sequence);
			for (int i = 0; i < state_list.size(); i++) {
				TempState current = state_list.get(i);
				int sid = current.getSid();
				String current_state = current.getState();
				String orig_state = list.get(sid - 1).getState();
				// 状态不变化
				if (current_state.equals(orig_state) && current_state.equals("坐"))
					continue;
				int target = checkBehaviour(list.get(sid - 1), current_state, sequence);
			}
			// add alarm
			if (checkAlarm(list) == 1 && alarm_id == 0) {
				alarm_num++;
				Alarm temp = new Alarm();
				temp.setClass_id(cid);
				temp.setStart(sequence);
				temp.setId(0);
				String path = "../video/alarm1.mp4";
				//getVedio(sequence, path);
				temp.setVedio_path(path);
				Date day = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				temp.setDate(Integer.parseInt(df.format(day)));
				df = new SimpleDateFormat("HH:mm:ss");
				temp.setTime(df.format(day));
				String destribution = "";
				for (int i = 0; i < list.size(); i++) {
					if (!list.get(i).getDescription().equals("正常"))
						destribution = destribution + list.get(i).getSid() + "号同学" + list.get(i).getDescription() + ";";
				}
				temp.setDestribute(destribution);
				System.out.println(temp.getTime() + "  :  " + temp.getDestribute());
				Alarm result = alarm.save(temp);
				alarm_id = result.getId();
				sendPost("http://localhost:10002/study/alertAlarm?alarm_id"+alarm_id, "");

			}
			if (checkAlarm(list) == 0&&alarm_id!=0){
				sendPost("http://localhost:10002/study/alertAlarm?alarm_id="+alarm_id, "");
				alarm_id=0;
			}
		}
		//checkPattern();
	}

	public int addBehavior(StateHelper orig, int sequence, String state) {
		int between = sequence - orig.getStart_seq();
		if (orig.getBeh_id() == 0) {
			Behavior target = new Behavior();
			target.setId(0);
			target.setCid(orig.getCid());
			target.setSid(orig.getSid());
			target.setTotal_time(between);
			Date day = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			target.setDate(df.format(day));
			df = new SimpleDateFormat("HH:mm:ss");
			target.setEnd(df.format(day));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(day);
			calendar.add(Calendar.SECOND, between * -1);
			target.setStart(df.format(calendar.getTime()));
			target.setAction(state);
			target.setStatus("未请假");
			target.setTid(2);
			target.setPlace("");
			return behavior.save(target).getId();
		} else {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			String end = df.format(calendar.getTime());
			// behavior.updateById(orig.getBeh_id(),end,"未请假",state,between);
			return orig.getBeh_id();
		}

	}

	/*
	 * 站立 3s 室外 15s内短期离开，15s外长期离开 迟到 15s记为缺席 睡觉 10s 游荡 5s
	 */
	public int checkBehaviour(StateHelper orig, String cur_state, int sequence) {
		// 迟到缺勤
		if (sequence == 1 && cur_state.equals("室外")) {
			orig.setState(cur_state);
			orig.setBeh_id(addBehavior(orig, sequence, "迟到"));
			orig.setDescription("迟到");
			return 1;
		}
		if (cur_state.equals("室外") && (orig.getDescription().equals("迟到"))) {
			if (sequence >= 15) {
				addBehavior(orig, sequence, "缺席");
			} else {
				addBehavior(orig, sequence, "迟到");
			}
			if (sequence == 15) {
				orig.setDescription("迟到");
				return 1;
			}
			return 0;
		}
		int key = 5;
		// 改成不良状态
		if (orig.getState().equals("坐")) {
			orig.setState(cur_state);
			orig.setStart_seq(sequence);
			return 0;
		}
		// 站立
		key = 5;
		if (orig.getState().equals("站立") && (cur_state.equals("站立"))) {
			if (sequence - orig.getStart_seq() > key) {
				orig.setBeh_id(addBehavior(orig, sequence, "站立"));
			}
			if (sequence - orig.getStart_seq() == key) {
				orig.setDescription("站立");
				return 1;
			}
			return 0;
		}
		// 睡觉
		key = 10;
		if (orig.getState().equals("睡觉") && (cur_state.equals("睡觉"))) {
			if (sequence - orig.getStart_seq() > key) {
				orig.setBeh_id(addBehavior(orig, sequence, "睡觉"));
			}
			if (sequence - orig.getStart_seq() == key) {
				orig.setDescription("睡觉");
				return 1;
			}
			return 0;
		}
		// 游荡
		key = 5;
		if (orig.getState().equals("游荡")) {
			if (sequence - orig.getStart_seq() > key) {
				orig.setBeh_id(addBehavior(orig, sequence, "游荡"));
			}
			if (sequence - orig.getStart_seq() == key) {
				orig.setDescription("游荡");
				return 1;
			}
			return 0;
		}
		// 早退
		if (cur_state.equals("室外") && (sequence == 92) && (orig.getDescription().equals("离开"))) {
			orig.setBeh_id(addBehavior(orig, sequence, "早退"));
			return 0;
		}
		// 室外
		key = 15;
		if (cur_state.equals("室外")) {
			if (sequence - orig.getStart_seq() < key) {
				orig.setBeh_id(addBehavior(orig, sequence, "短时离开"));
			} else {
				orig.setBeh_id(addBehavior(orig, sequence, "离开"));
			}
			if (sequence - orig.getStart_seq() == key) {
				orig.setDescription("离开");
			}
			if (sequence - orig.getStart_seq() <= key)
				return 1;
			return 0;
		}

		// 坐
		if (cur_state.equals("坐")) {
			orig.setState(cur_state);
			orig.setBeh_id(0);
			orig.setDescription("正常");
			return 0;
		}
		return 0;
	}

	public int checkAlarm(ArrayList<StateHelper> list) {
		double value = 0.0;
		for (int i = 0; i < list.size(); i++) {
			value += (double) alarm_value.get(list.get(i).getDescription());
		}
		System.out.println("value=" + value);
		if (value >= alarm_stan)
			return 1;
		return 0;
	}
	
	@Override
	public void checkPattern(int cid) throws Exception {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String today = df.format(calendar.getTime());
		calendar.add(Calendar.DATE, -3);
		String threeday = df.format(calendar.getTime());
		calendar.add(Calendar.DATE, -4);
		String week = df.format(calendar.getTime());

		List<Integer> student=behavior.getByDateStu(cid, today);
		ArrayList<Integer> team=new ArrayList<Integer>();
		for(int i=0;i<student.size();i++){
			int sid=student.get(i);
			String sname=stu_repo.getName(cid,sid);
			List<Behavior> Oneday=behavior.getByDate(cid,today,sid);
			List<Behavior> Threeday=behavior.getByDate(cid,threeday,sid);
			List<Behavior> Weekday=behavior.getByDate(cid,week,sid);
			
			String description="";
			//某个同学在一天内超过五次出现异常情况；
			if(Oneday.size()>=5) {
				for(int k=0;k<Oneday.size();k++){
					description=description+Oneday.get(i).getDate()+" "+
				Oneday.get(i).getStart()+" "+Oneday.get(k).getAction()+";<br>";
				}
				addPattern(cid,sname+"同学在1天内出现"+Oneday.size()+"次异常行为:<br>"+description);
			}
			//某个同学在一天内的超过三个时段 睡觉；
			int sleep=0;
			description="";
			for(int k=0;k<Oneday.size();k++){
				String action=Oneday.get(k).getAction();
				if(action.equals("睡觉")) {
					description=description+Oneday.get(k).getDate()+" "+
							Oneday.get(k).getStart()+" "+action+";<br>";
					sleep++;
					}
			}
			if(sleep>=3)
					addPattern(cid,sname+"号同学在1天内出现"+sleep+"次睡觉行为:<br>"+description);
				
			//某个同学在一天内的超过三个时段出教室出教室（包括短时离开）；
			int leave=0;
			description="";
			for(int k=0;k<Oneday.size();k++){
				String action=Oneday.get(k).getAction();
				if(action.contains("离开")||action.equals("早退")){
					description=description+Oneday.get(k).getDate()+" "+
							Oneday.get(k).getStart()+" "+action+";";
					leave++;
				}
			}
			if(leave>=3)
				addPattern(cid,sname+"号同学在1天内出现"+leave+"次离开教室的行为:<br>"+description);
			//某个同学在三天内超过十次出现异常情况；
			if(Threeday.size()>=10){
				description="";
				for(int k=0;k<Threeday.size();k++){
					description=description+Threeday.get(i).getDate()+" "+
							Threeday.get(i).getStart()+" "+Threeday.get(k).getAction()+";<br>";
				}
				addPattern(cid,sname+"同学在3天内出现"+Threeday.size()+"次异常行为:<br>"+description);
			}
			//某个同学在一周内超过 三个时段迟到或者缺勤。
			int late=0;
			description="";
			for(int k=0;k<Weekday.size();k++){
				String action=Weekday.get(k).getAction();
				if(action.equals("迟到")||action.equals("缺勤")){
					late++;
					description=description+Weekday.get(i).getDate()+" "+
							Weekday.get(i).getStart()+" "+Weekday.get(k).getAction()+";<br>";
				}
			}
			if(late>=3)
				addPattern(cid,sname+"同学在一周内出现"+late+"次迟到或者是缺勤行为:<br>"+description);
		}
		//某两位同学在一周内超过三个时段内先后出教室；考虑几次相差的时间均不超过xx
		if(team.size()>1){
			for(int i=0;i<team.size()-1;i++){
				for(int j=i+1;j<team.size();j++){
					getTeam(cid,i,j,week);
				}
			}
		}
	}
	
	public static final int space=60;  //只允许两个离开行为之间最多相差60s
	
	public int getTeam(int cid,int s1,int s2,String date) throws Exception{
		List<Behavior> Week1=behavior.getByDate(cid,date,s1);
		ArrayList<Behavior> beh1=new ArrayList<Behavior>();
		List<Behavior> Week2=behavior.getByDate(cid,date,s1);
		ArrayList<Behavior> beh2=new ArrayList<Behavior>();
		String description="";
		for(int i=0;i<Week1.size();i++){
			String action=Week1.get(i).getAction();
			if(action.contains("离开")||action.equals("早退")){
				beh1.add(Week1.get(i));
			}
		}
		for(int i=0;i<Week2.size();i++){
			String action=Week2.get(i).getAction();
			if(action.contains("离开")||action.equals("早退")){
				beh2.add(Week2.get(i));
			}
		}
		//不足次数考虑提前放弃
		if(beh1.size()<=2||beh2.size()<=3) return 0;
		int key=0;
		for(int i=0;i<beh1.size();i++){
			Behavior temp=beh1.get(i);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String day=temp.getDate();
			String cl1=day+" "+temp.getStart();
			Calendar t1=Calendar.getInstance();
			t1.setTime(sdf.parse(cl1));
			long begin = t1.getTimeInMillis();
			for(int k=0;k<beh2.size();k++){
				if(beh2.get(k).getDate().equals(day)){
					String cl2=day+" "+beh2.get(k).getStart();
					Calendar t2=Calendar.getInstance();
					t2.setTime(sdf.parse(cl2));
					long end = t2.getTimeInMillis();
					long between=(end - begin)/(1000);
					if(between>-1*space&&between<space) {
						key++;
						Behavior temp1=beh1.get(i);
						Behavior temp2=beh2.get(k);
						description=description+temp1.getDate()+" "+stu_repo.findName(cid, s1)+
								" "+temp1.getStart()+"-"+temp1.getEnd()+";"+stu_repo.findName(cid, s2)+
								" "+temp2.getStart()+"-"+temp2.getEnd()+";<br>";
					}
				}
			}
		}
		if(key>3){
			addPattern(cid,stu_repo.getName(cid, s1)+"号同学和"+stu_repo.getName(cid, s2)+"号同学在最近一周内"+key+"次先后出教室，建议了解相关情况。");
			return 1;
		}
		return 0;
	}
	
	public void addPattern(int cid,String destribute){
		
		Pattern temp = new Pattern();
		temp.setClass_id(cid);
		temp.setId(0);
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		temp.setDate(Integer.parseInt(df.format(day)));
		temp.setIsHandle(0);
		temp.setDestribute(destribute);
		System.out.println(cid+":"+destribute);
		pattern.save(temp);
	}

	public void getVedio(int sequence, String name) throws InterruptedException {
		// TODO Auto-generated method stub
		String input = "C:\\Users\\dell\\Desktop\\vedio\\1960.mp4";
		String output = "F:\\java_neon_work\\LBAIS\\classmonitor\\presentation\\src\\main\\resources\\static\\video\\"
				+ name;
		String commands = "ffmpeg -ss 0:1:" + sequence + " -t 0:0:20 -i " + input + " -vcodec copy -acodec copy "
				+ output;
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(commands);
//		Thread.sleep(3000);
	}
	
	public void getVedio(int sequence,int end, String name) throws InterruptedException {
		// TODO Auto-generated method stub
		String input = "C:\\Users\\dell\\Desktop\\vedio\\1960.mp4";
		String output = "F:\\java_neon_work\\LBAIS\\classmonitor\\presentation\\src\\main\\resources\\static\\video\\"
				+ name;
		int between=end-sequence;
		String a=Integer.toString(between/3600)+":"+Integer.toString(between/60-(between/3600)*60)+":"+Integer.toString(between%60);
		String commands = "ffmpeg -ss 0:1:" + sequence + " -t "+a+" -i " + input + " -vcodec copy -acodec copy "
				+ output;
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(commands);
//		Thread.sleep(3000);
	}
	

	public static String sendPost(String url, String param) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(10 * 1000);
			conn.setDoOutput(true); // 发送POST请求必须设置如下两行
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			throw e; // 异常外抛
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	
	private static int interval = 90; // 90帧，1s30帧即每三秒判定一次
	private static double rate = 0.3; // 精度90帧内有30%检测不到即认为不在
	private static Map<Integer, ActionInfo> map=new HashMap<>();
	private static Map<Integer,String> beh=new HashMap<>();
	@Override
	public void takeClass(String path,int cid,String video) throws Exception {
		int alarm_id=0;
		double alarm_num=0.0;
		String description="";
		File file = new File(path);
		String[] fileName = file.list();
		String pathStart=path + "\\" + fileName[0];
		pathStart=pathStart.substring(0,pathStart.length()-19);
		//初始化位置信息
		SeatHelper.init();
		InitInfo initInfo = new InitInfo();
		initInfo.setSeatTable(SeatHelper.getInitalSeatArea());
		initInfo.setCid(2);
		initInfo.setDate("2018-05-17");
		initInfo.setTid(1);
		initInfo.setPlace("语文");
//		new AbsenteeImpl().analyseAbsentee(IOHelper.dealWithJson(), initInfo);
		//初始化状态信息
		beh=new HashMap<>();
		int start=0;
		List<List<Point2D>> temp=TempImpl.getData(start,pathStart);
		map = TempImpl.setInitalStatus(temp, initInfo);
//		for (Integer key : map.keySet()) {
//		      System.out.println(key+":"+map.get(key).getName());
//		    }
		//循环信息
		start+=90;
		for(;start<fileName.length-90;start+=90){
			System.out.println(start+" !!!!!!!!!!!!!!!");
			alarm_num=0.0;
			description="";
			temp=TempImpl.getData(start,pathStart);
			Map<Integer, String> result=TempImpl.updateStatus(start,temp, initInfo, map);
			for (Integer key : result.keySet()) {
			      alarm_num=alarm_num+(double)alarm_value.get(result.get(key));
			      //description=description+key+"号同学"+result.get(key)+";";
			      description=description+stu_repo.getName(cid, key)+"同学"+result.get(key)+";";
			    }
			if(alarm_num>=18) {
				alarm_num=0;
				description="";
				result=new HashMap();
			}
			Map<Integer, String> sleep=TempImpl.getSleep(start,pathStart);
			for (Integer key : sleep.keySet()) {
				if(!result.keySet().contains(key)){
					alarm_num=alarm_num+(double)alarm_value.get("睡觉");
					//description=description+key+"号同学"+result.get(key)+";";
					description=description+stu_repo.getName(cid, key)+"同学"+result.get(key)+";";
				}
			}
			if(alarm_num>=alarm_stan && alarm_id == 0){
					Alarm temp_a = new Alarm();
					temp_a.setClass_id(cid);
					temp_a.setStart(start/30);
					temp_a.setId(0);
					temp_a.setVedio_path(video);
					Date day = new Date();
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
					temp_a.setDate(Integer.parseInt(df.format(day)));
					df = new SimpleDateFormat("HH:mm:ss");
					temp_a.setTime(df.format(day));
					temp_a.setDestribute(description);
					Alarm turnvalue = alarm.save(temp_a);
					alarm_id = turnvalue.getId();
					sendPost("http://localhost:10002/study/alertAlarm?alarm_id="+alarm_id, "");
				System.out.println(start/30+" "+alarm_num+" "+description);
				}else{
					alarm_id=0;
				}
				
			}
	}
	
	public static void main(String[] args) throws Exception {
		//new SelfstudyImpl().takeClass("E:\\百度云下载\\2018.5.4\\jsonOut\\IMG_1960",2);
	}
	
}
