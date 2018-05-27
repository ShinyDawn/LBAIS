package service.service;

import java.util.List;

import service.entity.Student;
import service.vo.*;

public interface StudentService {

//    public List<Student> getInfo(int cid);


//    public int add(int cid, int sid, String name, String gender, int x, int y);

//    public int update(int id, int cid, int sid, String name, String gender, int x, int y);

//    public void delete(int id);

    //part of Personal Model
    public Student getOneInfo(int cid, int sid);

    public List<AttendanceVO> getAttendanceInfo(int cid, int sid, String startDate, String endDate);

    public double getAttendanceRate(int cid, int sid, String startDate, String endDate);


    public List<LivenessVO> getLivenessPercentBySubject(int cid, int sid, String startDate, String endDate, String subject);

    public List<LivenessVO> getLivenessPercentDay(int cid, int sid, String startDate, String endDate);

    public List<LivenessShowVO> getLivenessPercentEveryWeek(int cid, int sid, String startDate, String endDate);


    public LivenessVO getGeneralLivenessPercentBySubject(int cid, int sid, String startDate, String endDate, String subject);

    public LivenessVO getGeneralLivenessPercent(int cid, int sid, String startDate, String endDate);

    public List<DisciplineVO> getDisplineInfo(int cid, int sid, String startDate, String endDate);


    public double getDisciplineRange(int cid, int sid, String startDate, String endDate);


    public StudentInfoVO getStudentProblemInfo(int cid, int sid, String startDate, String endDate);

    public List<StudentInfoVO> getStudentsInfoList(int cid, String startDate, String endDate);


}
