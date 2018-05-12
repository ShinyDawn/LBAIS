package service.service;

import java.util.List;

import service.entity.Student;
import service.vo.AttendanceVO;
import service.vo.DisciplineVO;
import service.vo.LivenessVO;
import service.vo.StudentInfoVO;

public interface StudentService {

	public List<Student> getInfo(int cid);

	public int add(int cid, int sid, String name, String gender, int x, int y);

	public int update(int id, int cid, int sid, String name, String gender, int x, int y);

	public void delete(int id);

	//part of Personal Model
	public List<AttendanceVO> getAttendanceInfo(int cid,int sid,int period);
	public double getAttendenceRate(int cid,int sid,int period);
	public double getAttendencePrecent(int cid,int sid,int period);

	public List<LivenessVO> getLivenessInfoBysubject(int cid,int sid,int period,String subject);
	public List<LivenessVO> getLivenessInfo(int cid,int sid,int period);
	public double getGeneralLivenessRateBySubject(int cid,int sid,int period,String subject);
	public double getGeneralLivenessRate(int cid,int sid,int period);

	public double getGeneralLivenessPercentBySubject(int cid,int sid,int period,String subject);
	public double getGeneralLivenessPercent(int cid,int sid,int period);

	public List<DisciplineVO> getDisplineInfo(int cid,int sid,int period);
	public double getDisciplineRate(int cid,int sid,int period);
	public double getDisciplinePercent(int cid,int sid,int period);

	public List<StudentInfoVO> getStudentProblemInfo(int cid, int sid, int period);

	public List<StudentInfoVO> getStudentsInfoList(int cid,int period);

}
