package service.service;

import java.util.List;

import service.entity.study.Alarm;
import service.entity.study.Pattern;

public interface StudyService {
	public List<Alarm> getAlarm(String type,int cid);
	public Alarm getSingleAlarm(int id);
	public void handleAlarm(int aid,int isHandle,String handle_list) throws Exception;
	public List<Pattern> getPattern(String type,int cid);
	public Pattern getSinglePattern(int id);
	public void handlePattern(int pid,int isHandle,String handle_list) throws Exception;
	
	//alert an alarm
	public void alertAlarm(String alarm_json);
	
}
