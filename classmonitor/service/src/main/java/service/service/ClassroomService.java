package service.service;

import java.util.List;

import service.dao.Classroom;
import service.vo.StudentNewVO;

public interface ClassroomService {

	public List<Classroom> findAllClasses();
	
	public Classroom getInfo(int id);
	
	public void addOne(String cname,String teacher,int num,String grade,String date);
	
	public void updateOne(int id, String cname,String teacher,int num,String grade,String date);
	
	public void deleteOne(int id);

	public List<StudentNewVO> getStudentInfoList(int cid);

	public List<StudentNewVO> getStudentInfoBySimpleFilter(int cid,String problem);

	public List<StudentNewVO> getStudentInfoByAdvancedFilter(int cid);
}
