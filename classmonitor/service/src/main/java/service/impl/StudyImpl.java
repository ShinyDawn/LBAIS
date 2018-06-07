package service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.entity.study.Alarm;
import service.entity.study.AlarmHandle;
import service.entity.study.Pattern;
import service.entity.study.PatternHandle;
import service.repository.study.AlarmHandleRepository;
import service.repository.study.AlarmRepository;
import service.repository.study.PatternHandleRepository;
import service.repository.study.PatternRepository;
import service.service.StudyService;
import service.vo.study.AlarmVO;
import service.vo.study.PatternVO;

@Service
public class StudyImpl implements StudyService{
	@Autowired
	private AlarmRepository alarm;
	@Autowired
	private AlarmHandleRepository alarm_handle;
	@Autowired
	private PatternRepository pattern;
	@Autowired
	private PatternHandleRepository pattern_handle;
	
	
	@Override
	public List<AlarmVO> getAlarm(String type, int cid) {
		// TODO Auto-generated method stub
		int date=getDate(type,"alarm");
		ArrayList<Alarm> list=(ArrayList<Alarm>) alarm.getAlarm(date, cid);
		ArrayList<AlarmVO> result=new ArrayList<AlarmVO>();
		if(list==null) return result;
		for(int i=0;i<list.size();i++){
			result.add(new AlarmVO(list.get(i)));
		}
		return result;
	}
	@Override
	public AlarmVO getSingleAlarm(int id) {
		return new AlarmVO(alarm.findById(id));
	}
	@Override
	public void handleAlarm(int aid, int isHandle, String handle_list) throws Exception {
		
		String[] type={"","迟到","游荡","睡觉","缺席","早退"};
//		String temp="{\"description\":\"有同学睡觉和缺席\",\"behaviours\":[{\"students\":\"张晓梅,李晓亮,何晓西,\",\"behaviour\":4},{\"students\":\"张晓梅,何晓西,宋晓军,\",\"behaviour\":3}]}";
		/*{"description":"有几位同学睡觉，蔡晓丁同学出教室","behaviours":[{"students":"student1003,student1007,student1008,","behaviour":3},{"students":"student2001,","behaviour":5}]}
*/
		int cid=alarm.findById(aid).getClass_id();
		
		// TODO Auto-generated method stub
		alarm.updateIsHandle(aid, isHandle);
		if(isHandle==3){
			//only in this time should add handle
			System.out.println(handle_list);
			JSONObject jsonObject = new JSONObject(handle_list);
			String description=jsonObject.getString("description");
			//修改原有的alarm的描述，替换成老师的描述
			alarm.updateDestribute(aid, description);
			JSONArray behaviours = jsonObject.getJSONArray("behaviours");
			int count=0;
			for(int i=0;i<behaviours.length();i++){
				JSONObject obj=(JSONObject) behaviours.get(i);
				String behaviour=type[(int) obj.getLong("behaviour")];
				String students=obj.getString("students");
				System.out.println(count+"  "+students+":"+behaviour);
				String[] list=students.split(",");
				for(int k=0;k<list.length;k++){
					count++;
					int hid=aid*100+count;
					int sid=Integer.parseInt(list[k].trim().substring(7))%100;
					AlarmHandle temp=new AlarmHandle();
					temp.setHid(Integer.toString(hid));
					temp.setCid(cid);
					temp.setSid(sid);
					temp.setDescription(behaviour);
					temp.setAid(aid);
					alarm_handle.save(temp);
				}
			}
		}
		
	}
	
	
	
	
	@Override
	public List<PatternVO> getPattern(String type, int cid) {
		int date=getDate(type,"pattern");
		ArrayList<Pattern> list=(ArrayList<Pattern>) pattern.getPattern(date, cid);
		ArrayList<PatternVO> result=new ArrayList<PatternVO>();
		if(list==null) return result;
		for(int i=0;i<list.size();i++){
			result.add(new PatternVO(list.get(i)));
		}
		return result;
	}
	@Override
	public PatternVO getSinglePattern(int id) {
		// TODO Auto-generated method stub
		return new PatternVO(pattern.findById(id));
	}
	@Override
	public void handlePattern(int pid, int isHandle, String handle_list) throws Exception {
//		System.out.println();
		// TODO Auto-generated method stub
		String[] type={"","多次迟到","多次缺席","多次结对离开","多次离席","多次睡觉"};
		pattern.updateIsHandle(pid, isHandle);
		
		int cid=pattern.findById(pid).getClass_id();
		
		if(isHandle==1){
			//only in this time should add handle
			JSONObject jsonObject = new JSONObject(handle_list);
			String description=jsonObject.getString("description");
//			System.out.println(description);
			//修改原有的pattern的描述，替换成老师的描述
			pattern.updateDestribute(pid, description);
			JSONArray behaviours = jsonObject.getJSONArray("behaviours");
			int count=0;
			for(int i=0;i<behaviours.length();i++){
				JSONObject obj=(JSONObject) behaviours.get(i);
				String behaviour=type[(int) obj.getLong("behaviour")];
				String students=obj.getString("students");
				System.out.println(count+"  "+students+":"+behaviour);
				String[] list=students.split(",");
				for(int k=0;k<list.length;k++){
					count++;
					int hid=pid*100+count;
					int sid=Integer.parseInt(list[k].trim().substring(7))%100;
					PatternHandle temp=new PatternHandle();
					temp.setHid(Integer.toString(hid));
					temp.setCid(cid);
					temp.setSid(sid);
					temp.setDescription(behaviour);
					temp.setPid(pid);
					pattern_handle.save(temp);
				}
			}
		}
	}
	
	public static int getDate(String type,String which){
		type=type.replace("\"", "");
		Calendar today=Calendar.getInstance();
		Date result = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		if(type.equals("最近一天")){
			Calendar time=Calendar.getInstance();
			if(which.equals("alarm")) return Integer.parseInt(sdf.format(time.getTime()));
			else
				time.add(Calendar.DATE, -1);
				return Integer.parseInt(sdf.format(time.getTime()));
			
		}
		if(type.equals("最近一周")){
			Calendar time=Calendar.getInstance();
			time.add(Calendar.DATE, -6);
			return Integer.parseInt(sdf.format(time.getTime()));
		}
		if(type.equals("最近一月")){
			Calendar time=Calendar.getInstance();
			time.add(Calendar.DATE, -30);
			return Integer.parseInt(sdf.format(time.getTime()));
		}
		return 0;
	}
	@Override
	public int getNumA_handle(int cid) {
		// TODO Auto-generated method stub
		return alarm.getNum_handle(cid);
	}
	@Override
	public int getNumA_confirm(int cid) {
		// TODO Auto-generated method stub
		return alarm.getNum_confirm(cid);
	}
	@Override
	public int getNumP_handle(int cid) {
		// TODO Auto-generated method stub
		return pattern.getNum_handle(cid);
	}
	
	public static void main(String[] args) throws Exception{
//		StudyImpl.test();
	}
}
