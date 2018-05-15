package service.impl;

import java.io.BufferedReader;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.entity.Behavior;
import service.entity.study.Alarm;
import service.entity.study.TempState;
import service.model.StateHelper;
import service.repository.BehaviorRepository;
import service.repository.study.AlarmRepository;
import service.repository.study.TempStateRepository;
import service.service.SelfstudyService;
import sun.applet.Main;

@Service
public class SelfstudyImpl implements SelfstudyService {
	@Autowired
	private AlarmRepository alarm;
	@Autowired
	private TempStateRepository tempstate;
	@Autowired
	private BehaviorRepository behavior;

	HashMap alarm_value = new HashMap() {
		{
			put("正常", 0.0);
			put("迟到", 1.0);
			put("缺席", 2.0);
			put("长时离开", 3.0);
			put("早退", 3.0);
			put("站立", 1.0);
			put("游荡", 4.0);
			put("睡觉", 1.0);
		}
	};
	public static final double alarm_stan = 5.0;

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
		int alarm_id=0;
		int alarm_num=0;
		for (; sequence <= max_seq; sequence++) {
			List<TempState> state_list = tempstate.findByNumAndCidAndSequence(num, cid, sequence);
			for (int i = 0; i < state_list.size(); i++) {
				TempState current = state_list.get(i);
				int sid = current.getSid();
				String current_state = current.getState();
				String orig_state = list.get(sid - 1).getState();
				// 状态不变化
				if (current_state.equals(orig_state)&&current_state.equals("坐"))
					continue;
				int target=checkBehaviour(list.get(sid-1),current_state,sequence);
			}
			//add alarm
			if(checkAlarm(list)==1&&alarm_id==0){
				alarm_num++;
				Alarm temp=new Alarm();
				temp.setClass_id(cid);
				temp.setHandle_id("");
				temp.setId(0);
				String path="E:/vedio/1960/alarm"+Integer.toString(alarm_num)+".avi";
				getVedio(sequence,path);
				temp.setVedio_path(path);
				Date day = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				temp.setDate(Integer.parseInt(df.format(day)));
				df = new SimpleDateFormat("HH:mm:ss");
				temp.setTime(df.format(day));
				String destribution="";
				for(int i=0;i<list.size();i++){
					if(!list.get(i).getDescription().equals("正常"))
					destribution=destribution+list.get(i).getSid()+"号同学"+list.get(i).getDescription()+";";
				}
				temp.setDestribute(destribution);
				System.out.println(temp.getTime()+"  :  "+temp.getDestribute());
				Alarm result=alarm.save(temp);
				alarm_id=result.getId();
				
			}
			if(checkAlarm(list)==0)
				alarm_id=0;
		}
		checkPattern();
	}

	public int addBehavior(StateHelper orig, int sequence,String state){
		int between=sequence-orig.getStart_seq();
		if(orig.getBeh_id()==0){
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
		}else{
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			String end=df.format(calendar.getTime());
			behavior.updateById(orig.getBeh_id(),end,"未请假",state,between);
			return orig.getBeh_id();
		}
		
	}
	
	/*
	 * 站立 3s
	 * 室外  15s内短期离开，15s外长期离开
	 * 迟到  15s记为缺席
	 * 睡觉  10s
	 * 游荡  5s
	 * */
	public int checkBehaviour(StateHelper orig,String cur_state, int sequence) {
		//迟到缺勤
		if(sequence==1&&cur_state.equals("室外")){
			orig.setState(cur_state);
			orig.setBeh_id(addBehavior(orig,sequence,"迟到"));
			orig.setDescription("迟到");
			return 1;
		}
		if(cur_state.equals("室外")&&(orig.getDescription().equals("迟到"))){
			if(sequence>=15){
				addBehavior(orig,sequence,"缺席");
			}else{
				addBehavior(orig,sequence,"迟到");
			}
			if(sequence==15){
				orig.setDescription("迟到");
				return 1;
			}
			return 0;
		}
		int key=5;
		//改成不良状态
		if(orig.getState().equals("坐")){
			orig.setState(cur_state);
			orig.setStart_seq(sequence);
			return 0;
		}
		//站立
		key=5;
		if(orig.getState().equals("站立")&&(cur_state.equals("站立"))){
			if(sequence-orig.getStart_seq()>key){
				orig.setBeh_id(addBehavior(orig,sequence,"站立"));
			}
			if(sequence-orig.getStart_seq()==key){
				orig.setDescription("站立");
				return 1;
			}
			return 0;
		}
		//睡觉
		key=10;
		if(orig.getState().equals("睡觉")&&(cur_state.equals("睡觉"))){
			if(sequence-orig.getStart_seq()>key){
				orig.setBeh_id(addBehavior(orig,sequence,"睡觉"));
			}
			if(sequence-orig.getStart_seq()==key){
				orig.setDescription("睡觉");
				return 1;
			}
			return 0;
		}
		//游荡
		key=5;
		if(orig.getState().equals("游荡")){
			if(sequence-orig.getStart_seq()>key){
				orig.setBeh_id(addBehavior(orig,sequence,"游荡"));
			}
			if(sequence-orig.getStart_seq()==key){
				orig.setDescription("游荡");
				return 1;
			}
			return 0;
		}
		//早退
		if(cur_state.equals("室外")&&(sequence==92)&&(orig.getDescription().equals("长时离开"))){
			orig.setBeh_id(addBehavior(orig,sequence,"早退"));
			return 0;
		}
		//室外
		key=15;
		if(cur_state.equals("室外")){
			if(sequence-orig.getStart_seq()<key){
				orig.setBeh_id(addBehavior(orig,sequence,"短时离开"));
			}else{
				orig.setBeh_id(addBehavior(orig,sequence,"长时离开"));
			}
			if(sequence-orig.getStart_seq()==key){
				orig.setDescription("长时离开");
			}
			if(sequence-orig.getStart_seq()<=key)
			return 1;
			return 0;
		}
		
		//坐
		if(cur_state.equals("坐")){
			orig.setState(cur_state);
			orig.setBeh_id(0);
			orig.setDescription("正常");
			return 0;
		}
		return 0;
	}

	public int checkAlarm(ArrayList<StateHelper> list) {
		double value=0.0;
		for(int i=0;i<list.size();i++){
			value+=(double)alarm_value.get(list.get(i).getDescription());
		}
		System.out.println("value="+value);
		if(value>=alarm_stan) return 1;
		return 0;
	}

	public void checkPattern() {
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String today=df.format(calendar.getTime());
		calendar.add(Calendar.DATE, -3);
		String threeday=df.format(calendar.getTime());
		calendar.add(Calendar.DATE, -4);
		String week=df.format(calendar.getTime());
		
		int cid=2;
		for(int sid=1;sid<=16;sid++){
			//某个同学一天内三次出教室
			List<Behavior> list=behavior.getInADay(cid, sid, today);
			int count=0;
			for(int i=0;i<list.size();i++){
				String action=list.get(i).getAction();
				if(action.equals(""));
			}
		}
		
	}

	public void getVedio(int sequence,String name) throws InterruptedException {
		// TODO Auto-generated method stub
		String input="C:\\Users\\dell\\Desktop\\vedio\\1960.mp4";
		String output="F:\\java_neon_work\\LBAIS\\classmonitor\\presentation\\src\\main\\resources\\static\\video\\"+name;
		String commands="ffmpeg -ss 0:1:"+sequence+" -t 0:0:20 -i "+input+" -vcodec copy -acodec copy "+output;
		ProcessBuilder builder = new ProcessBuilder();
        builder.command(commands);
        Thread.sleep(3000);
	}
	
	public static String sendPost(String url, String param)  throws Exception{  
        PrintWriter out = null;  
        BufferedReader in = null;  
        String result = "";  
        try {  
            URL realUrl = new URL(url);  
            URLConnection conn = realUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10*1000);
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
        } finally{  
            try{  
                if(out!=null)out.close();  
                if(in!=null) in.close();  
            }  
            catch(Exception ex){
            }  
        }  
        return result;  
    }  

	public static void main(String[] args) throws Exception {
		//http://localhost:10002/study/get_s_alarm?alarm_id=1
		SelfstudyImpl.sendPost("http://localhost:10002/study/alertAlarm?alarm_id=1","");
	}
}
