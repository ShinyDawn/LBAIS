package service.service;

import java.util.List;

import service.dao.Student;
import service.vo.AttendanceVO;
import service.vo.DisciplineVO;
import service.vo.LivenessVO;

public interface StudentService {

	public List<Student> getInfo(int cid);

	public int add(int cid, int sid, String name, String gender, int x, int y);

	public int update(int id, int cid, int sid, String name, String gender, int x, int y);

	public void delete(int id);

	//part of Personal Model
	public List<AttendanceVO> getAttendanceInfo(int cid,int sid,int period);
	public double getAttendenceRate(int cid,int sid,int period);

	public List<LivenessVO> geLivenessInfo(int cid,int sid,int period,String subject);
	public double getLivenessRate(int cid,int sid,int period,String subject);

	public List<DisciplineVO> getDisplineInfo(int cid,int sid,int period);
	public double getDisciplineRate(int cid,int sid,int period);

}
