package service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.entity.Behavior;
import service.entity.Student;
import service.repository.BehaviorRepository;
import service.repository.CurriculumRepository;
import service.repository.StudentRepository;
import service.repository.TimeRepository;
import service.service.StudentService;
import service.util.DateUtil;
import service.vo.*;

import static service.util.MathUtil.getSort;


@Service
public class StudentImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private BehaviorRepository behaviorRipository;
    @Autowired
    private CurriculumRepository curriculumRepository;
    @Autowired
    private TimeRepository timeRepository;

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


    public List<StudentInfoVO> getStudentsInfoList(int cid, int period) {

        List<StudentInfoVO> studentInfoVOList = new ArrayList<>();
        List<Student> students = studentRepo.findByCid(cid);

        if (students == null || students.isEmpty()) {
            return studentInfoVOList;
        }
        /**
         * 旷课：只要缺勤不请假就算旷课
         * 补课：出勤率不足 ？% 或者有非自习课的请假现象 几次？
         * 偏科：各个科目的活跃度方差大于 ？
         * 效率：发呆时间超过 ？%
         * 纪律：自习有异常pattern 需要关注
         * 退步：综合指标呈下降趋势
         * 进步：综合指标呈上升趋势？？？需要存在吗？
         */
        String[] problemlist = {"请假较多", "兴趣较多", "纪律较差", "退步较大"};


        for (Student student : students) {
            int sid = student.getSid();
            //int sid,String name,double attendanceRate,double livenessRate,double deciplineRate
            StudentInfoVO studentInfoVO = new StudentInfoVO();
            studentInfoVO.setSid(sid);
            studentInfoVO.setName(student.getName());
            double attendanceRate = getAttendencePrecent(cid, sid, 30);
            double disciplineRate = getDisciplinePercent(cid, sid, 30);
            double livenessRate = getGeneralLivenessPercent(cid, sid, 30);
            studentInfoVO.setAttendanceRate(attendanceRate);
            studentInfoVO.setDeciplineRate(disciplineRate);
            studentInfoVO.setLivenessRate(livenessRate);

            studentInfoVOList.add(studentInfoVO);
        }

        return studentInfoVOList;
    }

    public List<StudentInfoVO> getStudentProblemInfo(int cid, int sid, int period) {
        List<StudentInfoVO> studentInfoVOList = new ArrayList<>();

        /**
         * 出勤较少：出勤率不足80%
         * 兴趣较低：某一科课堂表现<=20% (偏科：课堂表现方差>0.6)
         * 纪律较差：出现旷课>=1次 或者 迟到未请假>=3次 早退未请假>=3次 或者自习课<=20%
         * 退步较大：任一指标下降>30%
         * 进步较大：任一指标上升>30%
         */
        String[] problemlist = {"出勤较少", "兴趣较低", "纪律较差", "退步较大"};
        String[] problemtDetail = {"出勤率不足","课堂表现低于20%。","偏科：","课堂表现很好","课堂表现仍须努力"};
        String[] decipline = {"旷课","迟到","早退","自习纪律低于20%"};
        String[] trend={"下降","上升"};
        String detailFor = "详情请见";
        String[] model = {"出勤表现","课堂表现","自习表现"};


        //int sid,String name,double attendanceRate,double livenessRate,double deciplineRate
        StudentInfoVO studentInfoVO = new StudentInfoVO();
        studentInfoVO.setSid(sid);
        double attendanceRate = getAttendencePrecent(cid, sid, period);
        double disciplineRate = getDisciplinePercent(cid, sid,period);
        double livenessRate = getGeneralLivenessPercent(cid, sid,period);
        studentInfoVO.setAttendanceRate(attendanceRate);
        studentInfoVO.setDeciplineRate(disciplineRate);
        studentInfoVO.setLivenessRate(livenessRate);

        studentInfoVOList.add(studentInfoVO);
        return null;

    }

    ;

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
        int totalperiods = curriculumRepository.countTotalCourses(cid, date, DateUtil.getDate());

        double attendenceRate = 1;

        if (totalperiods == 0) {
            return attendenceRate;
        }

        double absenctCourses = lateForClass + earlyOut + absentee + cuttingSchool;

        if (absenctCourses > totalperiods) {
            absenctCourses = totalperiods;
        }
        attendenceRate = 1 - 1.0 * absenctCourses / totalperiods;

        return attendenceRate;
    }

    public double getAttendencePrecent(int cid, int sid, int period) {
        double rankPercent = 0;
        int rank = 0;
        List<Integer> sidList = studentRepo.getSid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return rankPercent;
        }
        List<StudentDataVO> rankList = new ArrayList<>();
        for (Integer integer : sidList) {
            int id = (int) integer;
            StudentDataVO studentDataVO = new StudentDataVO(id, getAttendenceRate(cid, id, period));
            rankList.add(studentDataVO);
        }
        rank = getSort(rankList, sid);
        int num = sidList.size();

        if (num == 0) {
            return rankPercent;
        }
        rankPercent = 1 - 1.0 * rank / num;
        return rankPercent;
    }

    ;

    public List<LivenessVO> getLivenessInfo(int cid, int sid, int period) {

        List<LivenessVO> resultList = new ArrayList<>();
        String startDate = DateUtil.getPassedDate(period, DateUtil.getDate());

        List<String> subjects = curriculumRepository.getDistinctSubject(cid);
        if (subjects == null || subjects.isEmpty()) {
            return resultList;
        }

        for (String s : subjects) {
            if (s.equals("自习")) {
                continue;
            }
            List<LivenessVO> livenessVOList = getLivenessInfoBysubject(cid, sid, period, s);
            LivenessVO livenessVO = new LivenessVO();
            livenessVO.setSubject(s);

            double livenessRate = 0;
            double concentrationRate = 0;
            double handsUpTimes = 0;

            if (livenessVOList == null || livenessVOList.isEmpty()) {

            } else {
                double sumLivenessRate = 0;
                double sumConcentrationRate = 0;
                double sumhandsUpTimes = 0;
                int attendTimes = livenessVOList.size();

                for (int i = 0; i < livenessVOList.size(); i++) {
                    //这里需要处理请假，因为是各个科目的总体情况，需要处理极端数据：请假不计入计数，未请假仍计入
                    //注意一种情况：迟到和早退、以及离开时间不算太长的话，举手次数相对比较多时课堂参与度也会相对来说较高，因此还是计入为好，故只计算课堂参与度低于0.3的情况
                    LivenessVO temp = livenessVOList.get(i);
                    if (temp.getLivenessRate() < 0.3) {
                        if (behaviorRipository.countApproval(cid, sid, temp.getDate(), temp.getTid()) != 0) {
                            attendTimes -= 1;
                            //以防万一，统一设置请假的统计指标为0,消除后面统一技术影响
                            temp.setHandsUpTimes(0);
                            temp.setLivenessRate(0);
                            temp.setConcentrationRate(0);
                        }
                    }
                    sumLivenessRate += temp.getLivenessRate();
                    sumConcentrationRate += temp.getConcentrationRate();
                    sumhandsUpTimes += temp.getHandsUpTimes();
                }

                if (attendTimes != 0) {
                    livenessRate = sumLivenessRate / attendTimes;
                    concentrationRate = sumConcentrationRate / attendTimes;
                    handsUpTimes = sumhandsUpTimes / attendTimes;
                }

            }

            livenessVO.setLivenessRate(livenessRate);
            livenessVO.setConcentrationRate(concentrationRate);
            livenessVO.setHandsUpTimes(handsUpTimes);
            resultList.add(livenessVO);
        }

        return resultList;
    }

    public List<LivenessVO> getLivenessInfoBysubject(int cid, int sid, int period, String subject) {

        List<LivenessVO> resultList = new ArrayList<>();
        String startDate = DateUtil.getPassedDate(period, DateUtil.getDate());
        String endDate = DateUtil.getDate();
        List<LessonDataVO> initList = curriculumRepository.getDistinctCourse(cid, subject, startDate, endDate);

        if (initList == null || initList.isEmpty()) {
            return resultList;
        }

        for (LessonDataVO data : initList) {
            String date = data.getField();
            int tid = data.getInteger();

            /**
             * 9个需要从数据库统计的指标
             * 这里迟到、早退、离开、缺勤，即使请假也会计入统计，因为这里是个体各科目的具体到每一节课的情况
             */
            double timesOfHandsUp = behaviorRipository.countByCidAndSidAndDateAndTidAndAction(cid, sid, date, tid, "举手");
            double timesOfHansUpAll = behaviorRipository.countByCidAndDateAndTidAndAction(cid, date, tid, "举手");
            int timeOfDull = behaviorRipository.sumTimeOfActionInOneLesson(cid, sid, date, tid, "发呆");
            int timeOfAbsentee = behaviorRipository.sumTimeOfActionInOneLesson(cid, sid, date, tid, "缺勤");
            int timeOfLeave = behaviorRipository.sumTimeOfActionInOneLesson(cid, sid, date, tid, "离开");
            int timeOfLate = behaviorRipository.sumTimeOfActionInOneLesson(cid, sid, date, tid, "迟到");
            int timeOfEarly = behaviorRipository.sumTimeOfActionInOneLesson(cid, sid, date, tid, "早退");
            int timeOfLesson = timeRepository.getCourseHour(cid, tid);
            int numOfStuduent = studentRepo.countStudentsByCid(cid);
            int absenteeNum = behaviorRipository.countByCidAndDateAndTidAndAction(cid, date, tid, "缺勤");
            int studentsOnLesson = numOfStuduent - absenteeNum;

            /**
             * 一些参数
             */
            double liveness_a = 0.7;
            double liveness_b = 0.3;

            /**
             * 三个需要计算的指标之一 livenessRate
             * 一些中间指标
             * 学生课堂参与度 = 学生举手次数/平均每人举手次数=（全班举手次数/全班实到人数）* a + 学生在教室时长/课程时长 * b
             * 注意分母为0的情况
             */
            double avg_hands_up = 0;
            if (studentsOnLesson != 0) {
                avg_hands_up = timesOfHansUpAll * 1.0 / studentsOnLesson;
            }

            double rate_hands_up = 0;
            if (avg_hands_up != 0) {
                rate_hands_up = timesOfHandsUp * 1.0 / avg_hands_up;
            }

            double rate_on_lesson = 0;
            int time_on_lesson = timeOfLesson - timeOfEarly - timeOfLate - timeOfAbsentee - timeOfLeave;
            //学生在教室时长不为负
            if (time_on_lesson < 0) {
                time_on_lesson = 0;
            }

            if (timeOfLesson != 0 && time_on_lesson >= 0) {
                rate_on_lesson = time_on_lesson * 1.0 / timeOfLesson;
            }

            double livenessRate = liveness_a * rate_hands_up + liveness_b * rate_on_lesson;

            /**
             * 三个需要计算的指标之一concentrationRate
             * 一些中间指标
             * 学生课堂专注度 = 1 - 发呆/学生在教室时长
             * 注意分母为0的情况,即如果学生不在教室，专注度为0
             */

            double concentrationRate = 0;
            if (time_on_lesson != 0) {
                concentrationRate = 1 - 1.0 * timeOfDull / time_on_lesson;
            }
            if (concentrationRate < 0) {
                concentrationRate = 0;
            }

            /**
             * 三个需要计算的指标之一 handsUp
             */

            double handsUp = timesOfHandsUp;

            LivenessVO livenessVO = new LivenessVO();
            livenessVO.setDate(date);
            livenessVO.setTid(tid);
            livenessVO.setSubject(subject);
            livenessVO.setHandsUpTimes(handsUp);
            livenessVO.setLivenessRate(livenessRate);
            livenessVO.setConcentrationRate(concentrationRate);
            resultList.add(livenessVO);

        }

        return resultList;
    }

    ;

    public double getGeneralLivenessRateBySubject(int cid, int sid, int period, String subject) {

        List<LivenessVO> livenessVOList = getLivenessInfo(cid, sid, period);

        /**
         * 各科的综合活跃度需要考虑 课堂参与度和课堂专注度的参数 暂时设为0.7:0.3
         */

        double general_a = 0.7;
        double general_b = 0.3;

        double generalRate = 0;

        if (livenessVOList == null || livenessVOList.isEmpty()) {
            return generalRate;
        }
        for (LivenessVO livenessVO : livenessVOList) {
            if (livenessVO.getSubject().equals(subject)) {
                generalRate = general_a * livenessVO.getLivenessRate() * 1.0 + general_b * livenessVO.getConcentrationRate() * 1.0;
            }
        }
        return generalRate;
    }

    public double getGeneralLivenessRate(int cid, int sid, int period) {
        /**
         * 总活跃度需要考虑各个课程的权重，目前根据在统计时间内各科各有多少课来设定权重 VO(course,times)
         */

        List<String> subjects = curriculumRepository.getDistinctSubject(cid);
        double generalRate = 0;

        if (subjects == null || subjects.isEmpty()) {
            return generalRate;
        }

        String startDate = DateUtil.getPassedDate(period, DateUtil.getDate());
        String endDate = DateUtil.getDate();
        int totalCourse = 0;

        for (String s : subjects) {
            if (s.equals("自习")) {
                continue;
            }
            int weight = curriculumRepository.countWeight(cid, startDate, endDate, s);
            double generalSubjectRate = getGeneralLivenessRateBySubject(cid, sid, period, s);
            generalRate += generalSubjectRate * weight * 1.0;
            totalCourse += weight;
        }

        if (totalCourse == 0) {
            return generalRate;
        }

        generalRate /= totalCourse;
        return generalRate;
    }

    public double getGeneralLivenessPercentBySubject(int cid, int sid, int period, String subject) {
        double rankPercent = 0;
        int rank = 0;
        List<Integer> sidList = studentRepo.getSid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return rankPercent;
        }
        List<StudentDataVO> rankList = new ArrayList<>();
        for (Integer integer : sidList) {
            int id = (int) integer;
            StudentDataVO studentDataVO = new StudentDataVO(id, getGeneralLivenessRateBySubject(cid, id, period, subject));
            rankList.add(studentDataVO);
        }
        rank = getSort(rankList, sid);

        int num = sidList.size();

        if (num == 0) {
            return rankPercent;
        }

        rankPercent = 1.0 * rank / num;

        return rankPercent;
    }

    ;

    public double getGeneralLivenessPercent(int cid, int sid, int period) {

        double rankPercent = 0;
        int rank = 0;
        List<Integer> sidList = studentRepo.getSid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return rankPercent;
        }
        List<StudentDataVO> rankList = new ArrayList<>();
        for (Integer integer : sidList) {
            int id = (int) integer;
            StudentDataVO studentDataVO = new StudentDataVO(id, getGeneralLivenessRate(cid, id, period));
            rankList.add(studentDataVO);
        }
        rank = getSort(rankList, sid);
        int num = sidList.size();

        if (num == 0) {
            return rankPercent;
        }
        rankPercent = 1 - 1.0 * rank / num;
        return rankPercent;
    }

    ;

    /**
     * 需要再设计
     *
     * @param cid    class id
     * @param sid    student id
     * @param period 7,30,90...
     * @return 自习列表
     */

    public List<DisciplineVO> getDisplineInfo(int cid, int sid, int period) {
        String date = DateUtil.getPassedDate(period, DateUtil.getDate());
        List<DisciplineVO> disciplineVOList = new ArrayList<>();

        List<Behavior> behaviorList = behaviorRipository.findBehaviorsDuringStudy(cid, sid, date);

        if (behaviorList == null || behaviorList.isEmpty()) {
            return disciplineVOList;
        }

        for (Behavior b : behaviorList) {
            DisciplineVO disciplineVO = new DisciplineVO(b.getDate(), b.getEnd(), b.getAction(), b.getTotal_time(), b.getStatus());
            disciplineVOList.add(disciplineVO);
        }

        /**
         * 这里应该跟pattern有一块联动
         */

        return disciplineVOList;

    }

    ;

    /**
     * 需要再设计
     *
     * @param cid    class id
     * @param sid    student id
     * @param period 7,30,90...
     * @return 自习守序度
     */

    public double getDisciplineRate(int cid, int sid, int period) {
        String startDate = DateUtil.getPassedDate(period, DateUtil.getDate());
        String endDate = DateUtil.getDate();


        double disciplineRate = 1;

        List<Behavior> behaviorList = behaviorRipository.findBehaviorsDuringStudy(cid, sid, startDate);

        if (behaviorList == null || behaviorList.isEmpty()) {
            return disciplineRate;
        }

        int studyNum = curriculumRepository.countTotalStudys(cid, startDate, endDate);
        int studyTime = timeRepository.getCourseHour(cid, 1);

        int totalTime = studyTime * studyNum;
        int outTime = 0;

        /**
         * 不同行为的权重根据zq那边设置，需要更精细的计算
         */

        for (Behavior b : behaviorList) {
            outTime += b.getTotal_time();
        }

        if (outTime > totalTime) {
            outTime = totalTime;
        }

        disciplineRate = 1 - outTime * 1.0 / totalTime;

        return disciplineRate;
    }

    public double getDisciplinePercent(int cid, int sid, int period) {

        double rankPercent = 0;
        int rank = 0;
        List<Integer> sidList = studentRepo.getSid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return rankPercent;
        }
        List<StudentDataVO> rankList = new ArrayList<>();
        for (Integer integer : sidList) {
            int id = (int) integer;
            StudentDataVO studentDataVO = new StudentDataVO(id, getDisciplineRate(cid, id, period));
            rankList.add(studentDataVO);
        }
        rank = getSort(rankList, sid);
        int num = sidList.size();

        if (num == 0) {
            return rankPercent;
        }
        rankPercent = 1 - 1.0 * rank / num;
        return rankPercent;

    }

    ;
}
