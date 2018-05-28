package service.service;

import java.util.List;

import service.vo.study.AlarmVO;
import service.vo.study.PatternVO;

public interface StudyService {
	public List<AlarmVO> getAlarm(String type,int cid);
	public AlarmVO getSingleAlarm(int id);
	public void handleAlarm(int aid,int isHandle,String handle_list) throws Exception;
	public List<PatternVO> getPattern(String type,int cid);
	public PatternVO getSinglePattern(int id);
	public void handlePattern(int pid,int isHandle,String handle_list) throws Exception;
	//得到三个数据
	public int getNumA_handle(int cid);
	public int getNumA_confirm(int cid);
	public int getNumP_handle(int cid);
	
}
