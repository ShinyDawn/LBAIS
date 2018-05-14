package service.impl;

import java.text.DecimalFormat;
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
import service.util.MathUtil;
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

    public Student getOneInfo(int cid, int sid) {
        return studentRepo.findByCidAndSid(cid, sid);
    }

    public List<StudentInfoVO> getStudentsInfoList(int cid, int period) {

        List<StudentInfoVO> studentInfoVOList = new ArrayList<>();
        List<Student> students = studentRepo.findByCid(cid);

        if (students == null || students.isEmpty()) {
            return studentInfoVOList;
        }

        for (Student student : students) {
            int sid = student.getSid();
            //int sid,String name,double attendanceRate,double livenessRate,double deciplineRate
            StudentInfoVO studentInfoVO = getStudentProblemInfo(cid, sid, period);
            studentInfoVOList.add(studentInfoVO);
        }

        return studentInfoVOList;
    }


    public StudentInfoVO getStudentProblemInfo(int cid, int sid, int period) {
        //int sid,String name,double attendanceRate,double livenessRate,double deciplineRate
        StudentInfoVO studentInfoVO = new StudentInfoVO();
        studentInfoVO.setSid(sid);
        studentInfoVO.setName(studentRepo.getName(cid, sid));
        double attendanceRate = getAttendancePrecent(cid, sid, period);
        double disciplineRate = getDisciplinePercent(cid, sid, period);
        LivenessVO livenessVO = getGeneralLivenessPercent(cid, sid, period);
        double livenessRate = livenessVO.getLivenessRate();
        double conRate = livenessVO.getConcentrationRate();
        double genRate = livenessVO.getGeneralRate();
        studentInfoVO.setAttendanceRate(attendanceRate);
        studentInfoVO.setDeciplineRate(disciplineRate);
        studentInfoVO.setGeneralRate(genRate);
        studentInfoVO.setLivenessRate(livenessRate);
        studentInfoVO.setConcentrationRate(conRate);


        /**
         * 出勤较少：出勤率不足80%
         * 兴趣较低：某一科课堂表现<=20% (偏科：课堂表现方差>0.6)
         * 纪律较差：出现旷课>=1次 或者 迟到未请假>=3次 早退未请假>=3次 或者自习课<=20%
         * 退步较大：任一指标下降>30%
         * 进步较大：任一指标上升>30%
         */
        //默认分析一周的问题
        List<ProblemVO> problemList = new ArrayList<>();

        /**
         * 出勤方面的问题
         */
        double rate = 0.0;

        DecimalFormat df = new DecimalFormat("#.00%");
        rate = getAttendanceRate(cid, sid, 7);
        if (rate < 0.8) {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("出勤较少");
            problem.setDetail("出勤率为" + df.format(rate) + "。");
            problem.setIsProgress(-1);
            problemList.add(problem);
        } else if (rate >= 0.8) {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("出勤良好");
            problem.setDetail("出勤率为" + df.format(rate) + "。");
            problem.setIsProgress(0);
            problemList.add(problem);
        }


        /**
         * 偏科
         */
        double[] sub_per = new double[5];
        sub_per[0]=getGeneralLivenessPercentBySubject(cid,sid,10,"语文");
        sub_per[1]=getGeneralLivenessPercentBySubject(cid,sid,10,"数学");
        sub_per[2]=getGeneralLivenessPercentBySubject(cid,sid,10,"英语");
        sub_per[3]=getGeneralLivenessPercentBySubject(cid,sid,10,"品德");
        sub_per[4]=getGeneralLivenessPercentBySubject(cid,sid,10,"科学");
        String lesson_detail = "";
        double sv = MathUtil.StandardDiviation(sub_per);
        if (sv>0.5){
            lesson_detail = "\n有偏科现象，详情请见课堂表现。";
        }

        /**
         * 课堂表现，可再细分专注度与活跃度
         */
        rate = genRate;
        if (rate <= 0.2) {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("兴趣较低");
            problem.setDetail("最近课堂表现低迷，需要注意。"+lesson_detail);
            problem.setIsProgress(-1);
            problemList.add(problem);
        } else if (rate >= 0.7) {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("兴趣满满");
            problem.setDetail("最近课堂表现活跃，值得肯定。"+lesson_detail);
            problem.setIsProgress(0);
            problemList.add(problem);
        } else {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("兴趣一般");
            problem.setDetail("最近课堂表现不错，仍须努力。"+lesson_detail);
            problem.setIsProgress(0);
            problemList.add(problem);
        }



        /**
         * 自习纪律低于20%
         * 出现旷课>=1次 或者 迟到未请假>=3次 早退未请假>=3次
         */
        rate = disciplineRate;
        String date = DateUtil.getPassedDate(period, DateUtil.getDate());
        List<Behavior> voilation = behaviorRipository.findAbsenteeVoilation(cid, sid, date);
        String detail = "";
        if (voilation == null || voilation.isEmpty()) {

        } else {
            //迟到
            int lateForClass = 0;
            //早退
            int earlyOut = 0;
            //缺勤旷课
            int cuttingSchool = 0;
            for (Behavior b : voilation) {
                if (b.getAction().equals("迟到")) {
                    lateForClass++;
                } else if (b.getAction().equals("早退")) {
                    earlyOut++;
                } else if (b.getAction().equals("缺勤")) {
                    cuttingSchool++;
                }
            }
            detail = "\n迟到" + lateForClass + "次，早退" + earlyOut + "次，旷课" + cuttingSchool + "次，详情见出勤表现。";
        }

        if (rate <= 0.2) {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("纪律较差");
            problem.setDetail("最近自习表现低迷，需要注意。" + detail);
            problem.setIsProgress(-1);
            problemList.add(problem);
        } else if (!detail.equals("")) {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("纪律较差");
            problem.setDetail(detail);
            problem.setIsProgress(-1);
            problemList.add(problem);
        }

        /**
         * 退步较大：课堂指标下降>30%，出勤、纪律下降60%
         %        * 进步较大：课堂指标上升>30%，出勤、纪律上升60% , period 再设计
         */

        double past_percent_a = 0.0;
        double now_percent_a = 0.0;
        double past_percent_d = 0.0;
        double now_percent_d = 0.0;
        double past_percent_l = 0.0;
        double now_percent_l = 0.0;
        now_percent_a = attendanceRate;
        past_percent_a = getAttendancePrecent(cid, sid, 7);
        now_percent_d = disciplineRate;
        past_percent_d = getDisciplinePercent(cid, sid, 7);
        now_percent_l = genRate;
        past_percent_l = getGeneralLivenessPercent(cid, sid, 7).getGeneralRate();

        double offset_a = now_percent_a - past_percent_a;
        double offset_d = now_percent_d - past_percent_d;
        double offset_l = now_percent_l - past_percent_l;

        String detail_a = "";
        String detail_d = "";
        String detail_l = "";

        //退步
        boolean fall = false;
        if (offset_a < -0.6) {
            detail_a = "出勤表现退步明显，需要注意。";
            fall = true;
        }
        if (offset_d < -0.6) {
            detail_d = "自习表现退步明显，需要注意。";
            fall = true;
        }
        if (offset_l < -0.3) {
            detail_l = "课堂表现退步明显，需要注意。";
            fall = true;
        }

        if (fall) {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("退步较大");
            problem.setDetail(detail_a + detail_d + detail_l);
            problem.setIsProgress(-1);
            problemList.add(problem);
        }

        detail_a = "";
        detail_d = "";
        detail_l = "";
        boolean advance = false;
        if (offset_a > 0.6) {
            detail_a = "出勤表现进步明显，需要表扬。";
            advance = true;
        }
        if (offset_d > 0.6) {
            detail_d = "自习表现进步明显，需要表扬。";
            advance = true;
        }
        if (offset_l > 0.3) {
            detail_l = "课堂表现进步明显，需要表扬。";
            advance = true;
        }

        if (advance) {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("进步较大");
            problem.setDetail(detail_a + detail_d + detail_l);
            problem.setIsProgress(1);
            problemList.add(problem);
        }

        studentInfoVO.setProblem(problemList);
        return studentInfoVO;

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

    public double getAttendanceRate(int cid, int sid, int period) {

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

    public double getAttendancePrecent(int cid, int sid, int period) {
        double rankPercent = 0;
        int rank = 0;
        List<Integer> sidList = studentRepo.getSid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return rankPercent;
        }
        List<StudentDataVO> rankList = new ArrayList<>();
        for (Integer integer : sidList) {
            int id = (int) integer;
            StudentDataVO studentDataVO = new StudentDataVO(id, getAttendanceRate(cid, id, period));
            rankList.add(studentDataVO);
        }
        rank = getSort(rankList, sid);
        int num = sidList.size();

        if (num == 0) {
            return rankPercent;
        }
        rankPercent = 1 - 1.0 * (rank - 0.9) / num;
        return rankPercent;
    }

    ;

    /**
     * 每一门学科每一个课时的每个学生的课堂表现统计 数值 （活跃度、参与度、综合表现）
     */
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
             * 三个需要计算的指标之一 handsUp ！！！！！！
             * 更改：为综合表现度
             * 参数定义
             *         double general_a = 0.7;
             *         double general_b = 0.3;
             */
//
//            double handsUp = timesOfHandsUp;

            double general_a = 0.7;
            double general_b = 0.3;

            double generalRate = general_a * livenessRate + general_b * concentrationRate;

            LivenessVO livenessVO = new LivenessVO();
            livenessVO.setDate(date);
            livenessVO.setTid(tid);
            livenessVO.setSubject(subject);
//            livenessVO.setHandsUpTimes(handsUp);
            livenessVO.setGeneralRate(generalRate);
            livenessVO.setLivenessRate(livenessRate);
            livenessVO.setConcentrationRate(concentrationRate);
            resultList.add(livenessVO);
        }

        return resultList;
    }


    /**
     * 每一门学科每一个课时的每个学生的课堂表现统计 排名percent （活跃度、参与度、综合表现）（echarts 学科-图接口）
     */
    public List<LivenessVO> getLivenessPercentBySubject(int cid, int sid, int period, String subject) {
        List<LivenessVO> percentList = new ArrayList<>();

        List<Integer> sidList = studentRepo.getSid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return percentList;
        }
        //stuNum 不可能为零
        int stuNum = sidList.size();

        List<LivenessVO> primitiveList = getLivenessInfoBysubject(cid, sid, period, subject);
        if (primitiveList == null || primitiveList.isEmpty()) {
            return percentList;
        }


        for (int i = 0; i < primitiveList.size(); i++) {
            LivenessVO currentStu = primitiveList.get(i);
            int rank_con, rank_live, rank_gen;
            List<StudentDataVO> rankList_con = new ArrayList<>();
            List<StudentDataVO> rankList_live = new ArrayList<>();
            List<StudentDataVO> rankList_gen = new ArrayList<>();
            for (Integer integer : sidList) {
                int id = (int) integer;
                List<LivenessVO> compareList = getLivenessInfoBysubject(cid, id, period, subject);
                for (int j = 0; j < compareList.size(); j++) {
                    LivenessVO compareStu = compareList.get(j);
                    if (currentStu.getDate().equals(compareStu.getDate()) && currentStu.getTid() == compareStu.getTid()) {

                        StudentDataVO data_con = new StudentDataVO(id, compareStu.getConcentrationRate());
                        rankList_con.add(data_con);

                        StudentDataVO data_live = new StudentDataVO(id, compareStu.getLivenessRate());
                        rankList_live.add(data_live);

                        StudentDataVO data_gen = new StudentDataVO(id, compareStu.getGeneralRate());
                        rankList_gen.add(data_gen);
                        //提高性能
                        compareList.remove(j);
                        break;
                    }
                }
            }

            rank_con = getSort(rankList_con, sid);
            rank_gen = getSort(rankList_gen, sid);
            rank_live = getSort(rankList_live, sid);

            double percent_con = 0.0;
            double percent_live = 0.0;
            double percent_gen = 0.0;

            if (stuNum != 0) {
                percent_con = 1 - 1.0 * (rank_con - 0.9) / stuNum;
                percent_live = 1 - 1.0 * (rank_live - 0.9) / stuNum;
                percent_gen = 1 - 1.0 * (rank_gen - 0.9) / stuNum;
            }
            LivenessVO percentVO = new LivenessVO();
            percentVO.setGeneralRate(percent_gen);
            percentVO.setLivenessRate(percent_live);
            percentVO.setConcentrationRate(percent_con);

            percentVO.setDate(currentStu.getDate());
            percentVO.setTid(currentStu.getTid());
            percentVO.setSubject(subject);
            percentList.add(percentVO);
        }

        return percentList;
    }

    /**
     * 每一门学科每天每个学生的课堂表现统计 数值（活跃度、参与度、综合表现）
     */

    public List<LivenessVO> getLivenessDailyBySubject(int cid, int sid, int period, String subject) {

        List<LivenessVO> resultList = new ArrayList<>();
        List<LivenessVO> primitiveList = getLivenessInfoBysubject(cid, sid, period, subject);

        if (primitiveList == null || primitiveList.isEmpty()) {
            return resultList;
        }
        for (int i = 0; i < primitiveList.size(); i++) {
            LivenessVO current = primitiveList.get(i);
            for (int j = i + 1; j < primitiveList.size(); j++) {
                LivenessVO compare = primitiveList.get(j);
                if (current.getDate().equals(compare.getDate()) && current.getTid() != compare.getTid()) {
                    current.setConcentrationRate(0.5 * (current.getConcentrationRate() + compare.getConcentrationRate()));
                    current.setLivenessRate(0.5 * (current.getLivenessRate() + compare.getLivenessRate()));
                    current.setGeneralRate(0.5 * (current.getGeneralRate() + compare.getGeneralRate()));
                    current.setHandsUpTimes(0.5 * (current.getHandsUpTimes() + compare.getHandsUpTimes()));
                    primitiveList.remove(j);
                    break;
                }
            }
            resultList.add(current);
        }

        return resultList;
    }

    /**
     * 每一门学科每天每个学生的课堂表现统计 排名percent（活跃度、参与度、综合表现）每天都填充
     */
    public List<LivenessVO> getLivenessInfoDaily(int cid, int sid, int period, String subject) {
        List<LivenessVO> resultList = new ArrayList<>();

        List<LivenessVO> primitivelist = getLivenessPercentDaily(cid, sid, period, subject);

        if (primitivelist == null || primitivelist.isEmpty()) {
            return resultList;
        }

        if (period == primitivelist.size()) {
            return primitivelist;
        }
        String endDate = DateUtil.getDate();
        for (int i = period - 1; i >= 0; i--) {
            String date = DateUtil.getPassedDate(i, endDate);

            try {
                if (DateUtil.dayForWeek(date) == 6 || DateUtil.dayForWeek(date) == 7) {
                    continue;
                }
            } catch (Exception e) {

            }
            boolean dateExist = false;
            for (int j = 0; j < primitivelist.size(); j++) {
                LivenessVO livenessVO = primitivelist.get(j);
                if (livenessVO.getDate().equals(date)) {
                    resultList.add(livenessVO);
                    primitivelist.remove(j);
                    dateExist = true;
                    break;
                }
            }
            if (!dateExist) {
                LivenessVO livenessVO = new LivenessVO();
                livenessVO.setDate(date);
                livenessVO.setSubject(subject);
                resultList.add(livenessVO);
            }
        }

        return resultList;
    }

    /**
     * 每一门学科每天每个学生的课堂表现统计 排名percent（活跃度、参与度、综合表现）
     */
    private List<LivenessVO> getLivenessPercentDaily(int cid, int sid, int period, String subject) {


        List<LivenessVO> percentList = new ArrayList<>();

        List<Integer> sidList = studentRepo.getSid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return percentList;
        }
        //stuNum 不可能为零
        int stuNum = sidList.size();

        List<LivenessVO> primitiveList = getLivenessDailyBySubject(cid, sid, period, subject);
        if (primitiveList == null || primitiveList.isEmpty()) {
            return percentList;
        }
        for (int i = 0; i < primitiveList.size(); i++) {
            LivenessVO currentStu = primitiveList.get(i);
            int rank_con, rank_live, rank_gen;
            List<StudentDataVO> rankList_con = new ArrayList<>();
            List<StudentDataVO> rankList_live = new ArrayList<>();
            List<StudentDataVO> rankList_gen = new ArrayList<>();
            for (Integer integer : sidList) {
                int id = (int) integer;
                List<LivenessVO> compareList = getLivenessDailyBySubject(cid, id, period, subject);
                for (int j = 0; j < compareList.size(); j++) {
                    LivenessVO compareStu = compareList.get(j);
                    if (currentStu.getDate().equals(compareStu.getDate())) {

                        StudentDataVO data_con = new StudentDataVO(id, compareStu.getConcentrationRate());
                        rankList_con.add(data_con);

                        StudentDataVO data_live = new StudentDataVO(id, compareStu.getLivenessRate());
                        rankList_live.add(data_live);

                        StudentDataVO data_gen = new StudentDataVO(id, compareStu.getGeneralRate());
                        rankList_gen.add(data_gen);
                        //提高性能
                        compareList.remove(j);
                        break;
                    }
                }
            }

            rank_con = getSort(rankList_con, sid);
            rank_gen = getSort(rankList_gen, sid);
            rank_live = getSort(rankList_live, sid);

            double percent_con = 0.0;
            double percent_live = 0.0;
            double percent_gen = 0.0;

            if (stuNum != 0) {
                percent_con = 1 - 1.0 * (rank_con - 0.9) / stuNum;
                percent_live = 1 - 1.0 * (rank_live - 0.9) / stuNum;
                percent_gen = 1 - 1.0 * (rank_gen - 0.9) / stuNum;
            }
            LivenessVO percentVO = new LivenessVO();
            percentVO.setGeneralRate(percent_gen);
            percentVO.setLivenessRate(percent_live);
            percentVO.setConcentrationRate(percent_con);

            percentVO.setDate(currentStu.getDate());
            percentVO.setTid(currentStu.getTid());
            percentVO.setSubject(currentStu.getSubject());
            percentList.add(percentVO);
        }

        return percentList;
    }


    /**
     * 每一门学科每天每个学生的课堂表现统计 percent（活跃度、参与度、综合表现）时间段内 所有学科 (echarts 全部-图接口)
     */
    public List<LivenessShowVO> getLivenessPercentDaily(int cid, int sid, int period) {

        List<LivenessShowVO> resultList = new ArrayList<>();

        List<String> initList = curriculumRepository.getDistinctSubject(cid);

        if (initList == null || initList.isEmpty()) {
            return resultList;
        }

        for (String s : initList) {
            if (s.equals("自习")) {
                continue;
            }
            List<LivenessVO> primitiveList = getLivenessInfoDaily(cid, sid, period, s);
            LivenessShowVO livenessShowVO = new LivenessShowVO();
            livenessShowVO.setSubject(s);
            livenessShowVO.setData(primitiveList);
            resultList.add(livenessShowVO);
        }
        return resultList;
    }


    private LivenessVO getGenaralInfoBySubject(int cid, int sid, int period, String subject) {


        LivenessVO livenessVO = new LivenessVO();
        livenessVO.setSubject(subject);

        List<LivenessVO> livenessVOList = getLivenessInfoBysubject(cid, sid, period, subject);
        if (livenessVOList == null || livenessVOList.isEmpty()) {
            return livenessVO;
        }


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
        livenessVO.setGeneralRate();

        return livenessVO;

    }

    /**
     * 获得某学生各科在某个时间段内的课堂表现统计 数值（活跃度、参与度、综合表现）
     */
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
            LivenessVO livenessVO = getGenaralInfoBySubject(cid, sid, period, s);
            resultList.add(livenessVO);
        }

        return resultList;
    }

    ;

    /**
     * 获得某学生在某个时间段内的综合课堂表现统计 数值（活跃度、参与度、综合表现）
     */
    public LivenessVO getGeneralLivenessRate(int cid, int sid, int period) {
        /**
         * 总活跃度需要考虑各个课程的权重，目前根据在统计时间内各科各有多少课来设定权重 VO(course,times)
         */

        List<String> subjects = curriculumRepository.getDistinctSubject(cid);
        LivenessVO result = new LivenessVO();

        if (subjects == null || subjects.isEmpty()) {
            return result;
        }

        String startDate = DateUtil.getPassedDate(period, DateUtil.getDate());
        String endDate = DateUtil.getDate();
        int totalCourse = 0;


        double gen_rate = 0.0;
        double con_rate = 0.0;
        double live_rate = 0.0;
        for (String s : subjects) {
            if (s.equals("自习")) {
                continue;
            }
            int weight = curriculumRepository.countWeight(cid, startDate, endDate, s);
            LivenessVO livenessVO = getGenaralInfoBySubject(cid, sid, period, s);
            gen_rate += livenessVO.getGeneralRate() * weight * 1.0;
            con_rate += livenessVO.getConcentrationRate() * weight * 1.0;
            live_rate += livenessVO.getLivenessRate() * weight * 1.0;
            totalCourse += weight;
        }

        if (totalCourse == 0) {
            return result;
        }

        gen_rate /= totalCourse;
        con_rate /= totalCourse;
        live_rate /= totalCourse;

        result.setGeneralRate(gen_rate);
        result.setLivenessRate(live_rate);
        result.setConcentrationRate(con_rate);

        return result;
    }

    /**
     * 获得某学生在某个时间段内的综合课堂表现统计 percent（活跃度、参与度、综合表现）
     */

    public LivenessVO getGeneralLivenessPercent(int cid, int sid, int period) {
        LivenessVO percent = new LivenessVO();

        List<Integer> sidList = studentRepo.getSid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return percent;
        }
        //stuNum 不可能为零
        int stuNum = sidList.size();

        LivenessVO currentStu = getGeneralLivenessRate(cid, sid, period);


        int rank_con, rank_live, rank_gen;
        List<StudentDataVO> rankList_con = new ArrayList<>();
        List<StudentDataVO> rankList_live = new ArrayList<>();
        List<StudentDataVO> rankList_gen = new ArrayList<>();

        for (Integer integer : sidList) {
            int id = (int) integer;
            LivenessVO compareStu = getGeneralLivenessRate(cid, id, period);

            StudentDataVO data_con = new StudentDataVO(id, compareStu.getConcentrationRate());
            rankList_con.add(data_con);

            StudentDataVO data_live = new StudentDataVO(id, compareStu.getLivenessRate());
            rankList_live.add(data_live);

            StudentDataVO data_gen = new StudentDataVO(id, compareStu.getGeneralRate());
            rankList_gen.add(data_gen);
        }

        rank_con = getSort(rankList_con, sid);
        rank_gen = getSort(rankList_gen, sid);
        rank_live = getSort(rankList_live, sid);

        double percent_con = 0.0;
        double percent_live = 0.0;
        double percent_gen = 0.0;

        if (stuNum != 0) {
            percent_con = 1 - 1.0 * (rank_con - 0.9) / stuNum;
            percent_live = 1 - 1.0 * (rank_live - 0.9) / stuNum;
            percent_gen = 1 - 1.0 * (rank_gen - 0.9) / stuNum;
        }

        percent.setGeneralRate(percent_gen);
        percent.setLivenessRate(percent_live);
        percent.setConcentrationRate(percent_con);

        percent.setDate(currentStu.getDate());
        percent.setTid(currentStu.getTid());
        percent.setSubject(currentStu.getSubject());

        return percent;
    }

    /**
     * 作废
     */
//    public double getGeneralLivenessRateBySubject(int cid, int sid, int period, String subject) {
//
//        List<LivenessVO> livenessVOList = getLivenessInfo(cid, sid, period);
//
//        /**
//         * 各科的综合活跃度需要考虑 课堂参与度和课堂专注度的参数 暂时设为0.7:0.3
//         */
//
//        double general_a = 0.7;
//        double general_b = 0.3;
//
//        double generalRate = 0;
//
//        if (livenessVOList == null || livenessVOList.isEmpty()) {
//            return generalRate;
//        }
//        for (LivenessVO livenessVO : livenessVOList) {
//            if (livenessVO.getSubject().equals(subject)) {
//                generalRate = general_a * livenessVO.getLivenessRate() * 1.0 + general_b * livenessVO.getConcentrationRate() * 1.0;
//            }
//        }
//        return generalRate;
//    }

    /**
     * 获得各科的rank，可以看方差
     */

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
            StudentDataVO studentDataVO = new StudentDataVO(id, getGenaralInfoBySubject(cid, id, period, subject).getGeneralRate());
            rankList.add(studentDataVO);
        }
        rank = getSort(rankList, sid);

        int num = sidList.size();

        if (num == 0) {
            return rankPercent;
        }

        rankPercent = 1 - 1.0 * (rank - 0.9) / num;

        return rankPercent;
    }

    ;;

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
            //需要在这里检查出勤吗？

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
        rankPercent = 1 - 1.0 * (rank - 0.9) / num;
        return rankPercent;

    }

    ;
}
