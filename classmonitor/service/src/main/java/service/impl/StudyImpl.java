package service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.entity.study.Alarm;
import service.entity.study.Pattern;
import service.repository.study.AlarmHandleRepository;
import service.repository.study.AlarmRepository;
import service.repository.study.PatternHandleRepository;
import service.repository.study.PatternRepository;
import service.service.StudyService;

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
	public List<Alarm> getAlarm(String type, int cid) {
		// TODO Auto-generated method stub
		int date=getDate(type);
		return alarm.getAlarm(date, cid);
	}
	@Override
	public Alarm getSingleAlarm(int id) {
		// TODO Auto-generated method stub
		if(alarm==null) System.out.println("Wrong");
		else System.out.println("get alarm="+id);
		System.out.println(alarm.findById(id).getVedio_path());
		return alarm.findById(id);
	}
	@Override
	public void handleAlarm(int aid, int isHandle, String handle_list) throws Exception {
		// TODO Auto-generated method stub
		alarm.updateById(aid, isHandle);
		if(isHandle!=2){
			
		}
		
	}
	@Override
	public List<Pattern> getPattern(String type, int cid) {
		int date=getDate(type);
		return pattern.getPattern(date, cid);
	}
	@Override
	public Pattern getSinglePattern(int id) {
		// TODO Auto-generated method stub
		return pattern.findById(id);
	}
	@Override
	public void handlePattern(int pid, int isHandle, String handle_list) throws Exception {
		// TODO Auto-generated method stub
		pattern.updateById(pid, isHandle);
		if(isHandle!=2){
			
		}
	}
	
	public static int getDate(String type){
		int date=0;
		return date;
	}
	@Override
	public void alertAlarm(String alarm_json) {
		// TODO Auto-generated method stub
		
	}
}
