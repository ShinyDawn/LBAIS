package service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.dao.Behavior;
import service.dao.Student;
import service.repository.BehaviorRepository;
import service.repository.StudentRepository;
import service.service.StudentService;
import service.util.DateUtil;
import service.vo.AttendanceVO;
import service.vo.DisciplineVO;
import service.vo.LivenessVO;

@Service
public class StudentImpl implements StudentService {
    @Autowired
    StudentRepository studentRepo;
    BehaviorRepository behaviorRipository;

    public List<Student> getInfo(int cid) {
        return studentRepo.findByCid(cid);
    }

    public int add(int cid, int sid, String name, String gender, int x, int y) {
        int seat = studentRepo.countSeat(cid, sid, x, y);
        if (seat != 0)
            return -2;
        Student current = studentRepo.findBySid(sid);
        if (current == null) {
            Student s = new Student();
            s.setCid(cid);
            s.setSid(sid);
            s.setName(name);
            s.setGender(gender);
            s.setX(x);
            s.setY(y);
            current = studentRepo.save(s);
            return current.getId();
        } else
            return -1;
    }

    public int update(int id, int cid, int sid, String name, String gender, int x, int y) {
        int seat = studentRepo.countSeat(cid, sid, x, y);
        if (seat != 0)
            return -2;
        int num = studentRepo.countSid(cid, sid);
        if (num == 1) {
            studentRepo.updateOne(id, sid, name, gender, x, y);
            return 1;
        } else
            return -1;
    }

    public void delete(int id) {
        studentRepo.deleteOne(id);
    }

    //part of Personal Model

    /**
     *
     * @param cid class id
     * @param sid student id
     * @param period 1,7,30,...
     * @return 缺勤情况列表
     */
    public List<AttendanceVO> getAttendanceInfo(int cid, int sid, int period) {

        String date = DateUtil.getPassedDate(period, DateUtil.getDate());
        List<Behavior> behaviorList = behaviorRipository.findAbsentee(cid, sid, date);
        List<AttendanceVO> attendanceVOList = new ArrayList<>();
        if (behaviorList == null || behaviorList.isEmpty()) {
            return attendanceVOList;
        }

        for (Behavior b : behaviorList) {
            AttendanceVO attendanceVO = new AttendanceVO(b.getDate(), b.getTid(), b.getPlace(), b.getBehavior(), b.getStatus());
            attendanceVOList.add(attendanceVO);
            System.out.println(attendanceVO.toString());
        }

        return attendanceVOList;
    }

    public double getAttendenceRate(int cid, int sid, int period) {

        //迟到算0.5课时（不管有无请假）
        double lateForClass = 0;
        //早退算0.5课时（不管有无请假）
        double earlyOut = 0;
        //缺勤请假算1课时；自习缺勤请假不算入课时
        double absentee = 0;
        //缺勤旷课算半天的课时（假设每天的课时相等）；自习旷课算1课时
        double cuttingSchool = 0;

        //总课时
        double totalperiods = 0;

        double attendenceRate = 0;

        return attendenceRate;
    }

    ;

    public List<LivenessVO> geLivenessInfo(int cid, int sid, int period, String subject) {
        return null;
    }

    ;

    public double getLivenessRate(int cid, int sid, int period, String subject) {
        return 0;
    }

    ;

    public List<DisciplineVO> getDisplineInfo(int cid, int sid, int period) {
        return null;
    }

    ;

    public double getDisciplineRate(int cid, int sid, int period) {
        return 0;
    }

    ;
}
