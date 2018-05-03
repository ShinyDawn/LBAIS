package service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.entity.Behavior;
import service.entity.Curriculum;
import service.entity.Student;
import service.entity.Time;
import service.repository.BehaviorRepository;
import service.repository.CurriculumRepository;
import service.repository.StudentRepository;
import service.repository.TimeRepository;
import service.service.StudentService;
import service.util.DateUtil;
import service.vo.*;

import javax.persistence.Tuple;

@Service
public class StudentImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private BehaviorRepository behaviorRipository;
    @Autowired
    private CurriculumRepository curriculumRepository;
//    @Autowired
//    private TimeRepository timeRepository;

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
     * @param cid    class id
     * @param sid    student id
     * @param period 1,7,30,...
     * @return 缺勤情况列表
     */
    public List<AttendanceVO> getAttendanceInfo(int cid, int sid, int period) {

        String date = DateUtil.getPassedDate(period, DateUtil.getDate());
        List<AttendanceVO> attendanceVOList = new ArrayList<>();

        List<Behavior> behaviorList = behaviorRipository.findAbsentee(cid, sid, date);

        if (behaviorList == null || behaviorList.isEmpty()) {
            return attendanceVOList;
        }

        for (Behavior b : behaviorList) {
            AttendanceVO attendanceVO = new AttendanceVO(b.getDate(), b.getTid(), b.getPlace(), b.getAction(), b.getStatus());
            attendanceVOList.add(attendanceVO);
            System.out.println(attendanceVO.toString());
        }

        return attendanceVOList;
    }

    public double getAttendenceRate(int cid, int sid, int period) {

        String date = DateUtil.getPassedDate(period, DateUtil.getDate());
        int courses = curriculumRepository.countCoursesOneDay(cid);

        //迟到算0.5课时（不管有无请假）
        double lateForClass = 0.5 * behaviorRipository.countLateForClass(cid, sid, date);
        //早退算0.5课时（不管有无请假）
        double earlyOut = 0.5 * behaviorRipository.countEarlyOut(cid, sid, date);
        //缺勤请假算1课时；自习缺勤请假不算入课时
        double absentee = 1.0 * behaviorRipository.countAbsentee(cid, sid, date);
        //缺勤旷课算半天的课时（假设每天的课时相等）；自习旷课算1课时
        double cuttingSchool = 0.5 * courses * behaviorRipository.countCuttingLesson(cid, sid, date) + behaviorRipository.countCuttingStudy(cid, sid, date);

        //总课时
        double totalperiods = curriculumRepository.countTotalCourses(cid, date, DateUtil.getDate());

        double attendenceRate = 1;
        attendenceRate = 1 - (lateForClass + earlyOut + absentee + cuttingSchool) / totalperiods;

        return attendenceRate;
    }

    ;

    public List<LivenessVO> getLivenessInfo(int cid, int sid, int period) {

        /**
         *     private String Date;
         *     private String subject;
         *     private double livenessRate;
         *     private double concentrationRate;
         *     private double handsUpTimes;
         */
        double livenessRate = 0;
        double concentrationRate = 0;
        double handsUpTimes = 0;
        String subject;
        return null;
    }

    public List<LivenessVO> getLivenessInfoBysubject(int cid, int sid, int period, String subject) {

        List<LivenessVO> resultList = new ArrayList<>();
        String startDate = DateUtil.getPassedDate(period, DateUtil.getDate());
        List<LivenessDataVO> initList = curriculumRepository.getDistinctCourse(cid, subject, startDate);

        if (initList ==null ||initList.isEmpty()) {
            return resultList;
        }

        //中间计算表
        List<LivenessCalculateVO> calculateList = new ArrayList<>();

        for(LivenessDataVO data : initList){
            String date = data.getDate();
            int tid = data.getTid();

            /**
             * 9个需要从数据库统计的指标
             */
            double timesOfHandsUp = behaviorRipository.countHandsUp(cid,sid,date,tid);
//            double timesOfHansUpAll=behaviorRipository.countHandsUpAll(cid,date,tid);
//            double timeOfDull=behaviorRipository.sumTimeOfActionInOneLesson(cid,sid,date,tid,"发呆");
//            double timeOfAbsentee=behaviorRipository.sumTimeOfActionInOneLesson(cid,sid,date,tid,"缺勤");
//            double timeOfLeave=behaviorRipository.sumTimeOfActionInOneLesson(cid,sid,date,tid,"离开");
//            double timeOfLate=behaviorRipository.sumTimeOfActionInOneLesson(cid,sid,date,tid,"迟到");
//            double timeOfEarly=behaviorRipository.sumTimeOfActionInOneLesson(cid,sid,date,tid,"早退");
//            double timeOfLesson=timeRepository.getCourseHour(cid,tid);
//            int studentsOnLesson = 40;

            /**
             * 一些参数
             */
            double liveness_a = 0.7;
            double liveness_b = 0.3;

            /**
             * 三个需要计算的指标 livenessRate concentrationRate handsUp
             * 一些中间指标
             * 学生课堂参与度 = 学生举手次数/平均每人举手次数=（全班举手次数/全班实到人数）* a + 学生在教室时长/课程时长 * b
             * 学生课堂专注度 = 1 - 发呆/学生在教室时长
             * 学生举手次数
             * 注意分母为0的情况
             */
//            double livenessRate = 0;
//            if (timesOfHansUpAll == 0)
//            double concentrationRate = 0;

            double handsUp = timesOfHandsUp;

            LivenessCalculateVO livenessCalculateVO = new LivenessCalculateVO(date,tid,subject);
            livenessCalculateVO.setTimesOfHandsUp(behaviorRipository.countHandsUp(cid,sid,date,tid));
            livenessCalculateVO.setTimesOfAllHandsUp(behaviorRipository.countHandsUpAll(cid,date,tid));
//            livenessCalculateVO.setTimeOfDull(behaviorRipository.sumTimeOfActionInOneLesson(cid,sid,date,tid,"发呆"));
            calculateList.add(livenessCalculateVO);
        }


        for(LivenessCalculateVO calculate:calculateList){
            LivenessVO livenessVO  = new LivenessVO();
            livenessVO.setDate(calculate.getDate());
            livenessVO.setTid(calculate.getTid());
            livenessVO.setSubject(calculate.getSubject());
            livenessVO.setHandsUpTimes(calculate.getTimesOfHandsUp());
            resultList.add(livenessVO);
        }


//        List<LivenessDataVO> handsUpTimesList = new ArrayList<>();
//        handsUpTimesList = behaviorRipository.countHandsUp(cid,sid,date);

        return resultList;
    }

    ;;

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
