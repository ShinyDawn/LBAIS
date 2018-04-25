package service.service;

import java.util.List;

import service.dao.Classroom;

public interface ClassroomService {

	public List<Classroom> findAllClasses();
	
	public Classroom getInfo(int id);
	
	public void addOne(String cname,String teacher,int num,String grade,String date);
	
	public void updateOne(int id, String cname,String teacher,int num,String grade,String date);
	
	public void deleteOne(int id);
}
