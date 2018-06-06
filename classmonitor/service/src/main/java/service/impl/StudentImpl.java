package service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.entity.Behavior;
import service.entity.CourseCal;
import service.entity.Curriculum;
import service.entity.Student;
//import service.entity.study.Pattern;
//import service.entity.study.PatternHandle;
import service.repository.*;
//import service.repository.study.PatternHandleRepository;
//import service.repository.study.PatternRepository;
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
    @Autowired
    private CourseCalRepository courseCalRepository;
//    @Autowired
//    private PatternHandleRepository patternHandleRepository;
//    @Autowired
//    private PatternRepository patternRepository;

//    public List<Student> getInfo(int cid) {
//        return studentRepo.findByCid(cid);
//    }
//
//    public int add(int cid, int sid, String name, String gender, int x, int y) {
//        int seat = studentRepo.countSeat(cid, sid, x, y);
//        if (seat != 0)
//            return -2;
//        Student current = studentRepo.findBySid(sid);
//        if (current == null) {
//            Student s = new Student();
//            s.setCid(cid);
//            s.setSid(sid);
//            s.setName(name);
//            s.setGender(gender);
//            s.setX(x);
//            s.setY(y);
//            current = studentRepo.save(s);
//            return current.getId();
//        } else
//            return -1;
//    }
//
//    public int update(int id, int cid, int sid, String name, String gender, int x, int y) {
//        int seat = studentRepo.countSeat(cid, sid, x, y);
//        if (seat != 0)
//            return -2;
//        int num = studentRepo.countSid(cid, sid);
//        if (num == 1) {
//            studentRepo.updateOne(id, sid, name, gender, x, y);
//            return 1;
//        } else
//            return -1;
//    }
//
//    public void delete(int id) {
//        studentRepo.deleteOne(id);
//    }

    //part of Personal Model

    public Student getOneInfo(int cid, int sid) {
        return studentRepo.findByCidAndSid(cid, sid);
    }

    public List<StudentInfoVO> getStudentsInfoList(int cid, String startDate, String endDate) {

        List<StudentInfoVO> studentInfoVOList = new ArrayList<>();
        List<Student> students = studentRepo.findByCid(cid);

        if (students == null || students.isEmpty()) {
            return studentInfoVOList;
        }

        for (Student student : students) {
            int sid = student.getSid();
            StudentInfoVO studentInfoVO = getStudentProblemInfo(cid, sid, startDate, endDate);
            studentInfoVOList.add(studentInfoVO);
        }

        return studentInfoVOList;
    }


    public StudentInfoVO getStudentProblemInfo(int cid, int sid, String startDate, String endDate) {

        //int sid,String name,double attendanceRate,double livenessRate,double deciplineRate
        StudentInfoVO studentInfoVO = new StudentInfoVO();
        studentInfoVO.setSid(sid);
        studentInfoVO.setName(studentRepo.findByCidAndSid(cid, sid).getName());

        //出勤
        double attendanceRate = getAttendanceRate(cid, sid, startDate, endDate);
        double disciplineRate = getDisciplineRange(cid, sid, startDate, endDate);

        //课堂
        LivenessVO livenessVO = getGeneralLivenessPercent(cid, sid, startDate, endDate);
        double livenessRate = livenessVO.getLivenessRate();
        double conRate = livenessVO.getConcentrationRate();
        double genRate = livenessVO.getGeneralRate();

        //
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
         * 进步较大：任一指标上升>30% （今天比昨天，这周比上周，如此类推）
         */

        List<ProblemVO> problemList = new ArrayList<>();

        /**
         * 出勤表现低于80% 出现旷课、迟到未请假、早退未请假
         */
        double rate = 0.0;

        List<Behavior> voilation = behaviorRipository.getAbsentee(cid, sid, startDate, endDate);
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
                if (!b.getStatus().equals("已请假")) {
                    if (b.getAction().equals("迟到")) {
                        lateForClass++;
                    } else if (b.getAction().equals("早退")) {
                        earlyOut++;
                    } else if (b.getAction().equals("缺勤")) {
                        cuttingSchool++;
                    }
                }

            }
            detail = "\n迟到" + lateForClass + "次，早退" + earlyOut + "次，旷课" + cuttingSchool + "次，详情请见出勤表现。";
        }

        DecimalFormat df = new DecimalFormat("#.00%");
        rate = attendanceRate;
        if (rate < 0.8) {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("出勤较少");
            problem.setDetail("出勤表现为" + df.format(rate) + "。" + detail);
            problem.setIsProgress(0);
            problemList.add(problem);
        } else if (rate >= 0.8) {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("出勤良好");
            problem.setDetail("出勤表现为" + df.format(rate) + "。" + detail);
            problem.setIsProgress(0);
            problemList.add(problem);
        }


        /**
         * 开始计算课堂相关的分析
         */
        List<String> subjectList = new ArrayList<>();
        subjectList.add("语文");
        subjectList.add("数学");
        subjectList.add("英语");
        subjectList.add("品德");
        subjectList.add("科学");

        /**
         * 各科专注度或者活跃度 percent在后20%
         */
        String con_detail = "";
        String live_detail = "";
        List<LivenessVO> lessonList = new ArrayList<>();
        for (String s : subjectList) {
            LivenessVO temp = getGeneralLivenessPercentBySubject(cid, sid, startDate, endDate, s);
            lessonList.add(temp);
//
            if (temp.getLivenessRate() <= 0.5) {
                live_detail += "," + s;
            }
            if (temp.getConcentrationRate() <= 0.5) {
                con_detail += "," + s;
            }
        }
        if (!live_detail.equals("")) {
            live_detail = live_detail.substring(1);
            live_detail = "\n" + live_detail + "课不够活跃，需要努力。";
        }
        if (!con_detail.equals("")) {
            con_detail = con_detail.substring(1);
            con_detail = "\n" + con_detail + "课不够专注，需要努力。";

        }
        /**
         * 偏科
         */

        List<Double> genList = new ArrayList<>();
        for (LivenessVO temp : lessonList) {
            genList.add(temp.getGeneralRate());
        }

        String lesson_detail = "";
        double sv = MathUtil.StandardDiv(genList);
        if (sv > 0.5) {
            lesson_detail = "\n有偏科现象，详情请见课堂表现。";
        }


        /**
         * 课堂表现
         */
        rate = genRate;
        if (rate <= 0.2) {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("课堂低迷");
            problem.setDetail("最近课堂整体相对表现低迷，需要加油。" + live_detail + con_detail + lesson_detail);
            problem.setIsProgress(0);
            problemList.add(problem);
        } else if (rate >= 0.7) {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("课堂积极");
            problem.setDetail("最近课堂整体表现很好，值得肯定。" + live_detail + con_detail + lesson_detail);
            problem.setIsProgress(0);
            problemList.add(problem);
        } else {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("课堂良好");
            problem.setDetail("最近课堂整体表现不错，仍须努力。" + live_detail + con_detail + lesson_detail);
            problem.setIsProgress(0);
            problemList.add(problem);
        }


        /**
         * 自习表现低于60% 理论上应该有pattarn 先不写
         */
        rate = disciplineRate;

        if (rate <= 0.6) {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("纪律较差");
            problem.setDetail("最近自习有些违纪，需要注意。");
            problem.setIsProgress(0);
            problemList.add(problem);
        } else {
            ProblemVO problem = new ProblemVO();
            problem.setTitle("纪律良好");
            problem.setDetail("最近自习表现不错，继续保持。");
            problem.setIsProgress(0);
            problemList.add(problem);
        }

        /**
         * 退步趋势：课堂指标下降>30%，出勤、纪律下降60%
         %        * 进步趋势：课堂指标上升>30%，出勤、纪律上升60% , period 再设计
         */

        double past_percent_a = 0.0;
        double now_percent_a = 0.0;
        double past_percent_d = 0.0;
        double now_percent_d = 0.0;
        double past_percent_l = 0.0;
        double now_percent_l = 0.0;


        String pastDate = DateUtil.getPassedDate(DateUtil.getDistanceOfTwoDate(DateUtil.parseDate(startDate), DateUtil.parseDate(endDate)), startDate);

        now_percent_a = attendanceRate;
        past_percent_a = getAttendanceRate(cid, sid, pastDate, startDate);
        now_percent_d = disciplineRate;
        past_percent_d = getDisciplineRange(cid, sid, pastDate, startDate);
        now_percent_l = genRate;
        past_percent_l = getGeneralLivenessPercent(cid, sid, pastDate, startDate).getGeneralRate();

        double offset_a = now_percent_a - past_percent_a;
        double offset_d = now_percent_d - past_percent_d;
        double offset_l = now_percent_l - past_percent_l;

        String detail_a = "";
        String detail_d = "";
        String detail_l = "";

        List<Double> y = new ArrayList<>();
        List<Double> x = new ArrayList<>();
        double period = DateUtil.getDistanceOfTwoDate(DateUtil.parseDate(startDate), DateUtil.parseDate(endDate));
        for (int i = 0; i <= period; i++) {
            x.add(i * 1.0);
            y.add(getGeneralLivenessPercent(cid, sid, startDate, DateUtil.getFutureDate(i, startDate)).getGeneralRate());
        }

        double k = MathUtil.getA(x, y);
        System.out.println(k);

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

    private ProblemVO analyzeTrend(int cid, int sid, String startDate, String endDate) {

        ProblemVO problemVO = new ProblemVO();
        List<Student> sidList = studentRepo.findByCid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return problemVO;
        }
        int stuNum = sidList.size();

        double current_k = 0.0;
        double period = DateUtil.getDistanceOfTwoDate(DateUtil.parseDate(startDate), DateUtil.parseDate(endDate));
        List<StudentDataVO> rankList = new ArrayList<>();
        for (Student student : sidList) {
            int id = student.getSid();
            List<Double> y = new ArrayList<>();
            List<Double> x = new ArrayList<>();
            for (int i = 0; i <= period; i++) {
                x.add(i * 1.0);
                y.add(getGeneralLivenessPercent(cid, id, startDate, DateUtil.getFutureDate(i, startDate)).getGeneralRate());
            }
            double k = MathUtil.getA(x, y);
            if (id == sid) {
                current_k = k;
            }
            StudentDataVO data = new StudentDataVO(id, k);
            rankList.add(data);
        }
        int rank = getSort(rankList, sid);
        double percent = 1 - 1.0 * (rank - 0.9) / stuNum;
        current_k = current_k * period / 7.0;
        boolean fall = false;
        String detail = "";
        if (percent < 0.2 || current_k < -0.03) {
            detail = Summery.lesson_fall;
            fall = true;
        }

        if (fall) {
            problemVO.setTitle("退步较大");
            problemVO.setDetail(detail);
            problemVO.setIsProgress(-1);
        }
        return problemVO;
    }

//    private List<ProblemVO> analyzeBehavior(int cid,int sid,String startDate,String endDate,){
//        return  null;
//    }
    ;

    /**
     * 缺勤情况列表
     *
     * @param cid
     * @param sid
     * @param startDate
     * @param endDate
     * @return
     */
    public List<AttendanceVO> getAttendanceInfo(int cid, int sid, String startDate, String endDate) {

        List<AttendanceVO> attendanceVOList = new ArrayList<>();

        List<Behavior> behaviorList = behaviorRipository.getAbsentee(cid, sid, startDate, endDate);

        if (behaviorList == null || behaviorList.isEmpty()) {
            return attendanceVOList;
        }

        for (Behavior b : behaviorList) {
            AttendanceVO attendanceVO = new AttendanceVO(b.getDate(), b.getTid(), b.getPlace(), b.getAction(), b.getStatus());
            attendanceVOList.add(attendanceVO);
        }

        return attendanceVOList;
    }

    public double getAttendanceRate(int cid, int sid, String startDate, String endDate) {

        int courses = timeRepository.countByCid(cid);
//        int courses = 6;

        //迟到算0.5课时（不管有无请假）
        double lateForClass = 0.5 * behaviorRipository.countDuringPeriod(cid, sid, startDate, endDate, "迟到");
        //早退算0.5课时（不管有无请假）
        double earlyOut = 0.5 * behaviorRipository.countDuringPeriod(cid, sid, startDate, endDate, "早退");

        double absentee = 0.0;

        double cuttingLesson = 0.0;
        double cuttingStudy = 0.0;

        //缺勤请假算1课时；自习缺勤请假不算入课时

        //缺勤旷课算3课时；自习旷课算1课时

        List<Behavior> absenteeList = behaviorRipository.findAction(cid, sid, startDate, endDate, "缺勤");
        for (Behavior b : absenteeList) {
            if (b.getStatus().equals("已请假")) {
                if (!b.getPlace().equals("自习")) {
                    absentee++;
                }
            } else {
                if (!b.getPlace().equals("自习")) {
                    cuttingLesson++;
                } else {
                    cuttingStudy++;
                }
            }
        }

        double cuttingSchool = 0.5 * courses * cuttingLesson + cuttingStudy;

        //总课时
        int totalperiods = curriculumRepository.countTotalCourses(cid, startDate, endDate);

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

//    public double getAttendanceRange(int cid, int sid, String startDate, String endDate) {
//
//        double rangePercent = 0;
//        double currentRate = getAttendanceRate(cid, sid, startDate, endDate);
//        double topRate = currentRate;
//        List<Integer> sidList = studentRepo.getSid(cid);
//        if (sidList == null || sidList.isEmpty()) {
//            return rangePercent;
//        }
//
//        for (Integer integer : sidList) {
//            int id = (int) integer;
//            double rate = getAttendanceRate(cid, id, startDate, endDate);
//            if (rate > topRate) {
//                topRate = rate;
//            }
//        }
//
//        if (topRate <= 0 || currentRate < 0) {
//            return rangePercent;
//        }
//
//        rangePercent = currentRate / topRate;
//        return rangePercent;
//
//    }

    ;

    public int initCourse(int cid, String startDate, String endDate) {
        String[] student = {"钱多多", "张晓伟", "王小明", "李小娜", "王小峰", "韩小雪", "周小芳", "赵小磊", "陆小瑶", "刘小洋", "叶小天", "王小明", "赵小川",
                "李小静", "吴小杰", "孙小豪", "郑小波"};
//        String[][] curriculum = {{"语文", "数学", "英语", "品德", "科学", "自习", "自习"},
//                {"数学", "数学", "语文", "自习", "品德", "自习", "英语"}, {"语文", "语文", "数学", "数学", "英语", "英语", "自习"},
//                {"英语", "英语", "语文", "数学", "科学", "自习", "自习"}, {"英语", "数学", "语文", "语文", "自习", "自习", "自习"},
//                {"语文", "数学", "英语", "英语", "自习", "科学", "品德"}, {"语文", "数学", "英语", "自习", "自习", "自习", "自习"},
//                {"语文", "数学", "英语", "自习", "自习", "自习", "自习"}};
        String[] subjects = {"语文", "数学", "英语", "品德", "科学"};
        for (int i = 0; i < student.length; i++) {
            for (String s : subjects) {
                getLivenessInfoBysubjectOrigin(cid, i + 1, startDate, endDate, s);
            }
        }
        return 0;
    }

    /**
     * lesson 1
     * 某一时间段内每一门学科每一个课时的每个学生的课堂表现统计 数值 （活跃度、参与度、综合表现）
     */
    private List<LivenessVO> getLivenessInfoBysubjectOrigin(int cid, int sid, String startDate, String endDate, String subject) {

        List<LivenessVO> resultList = new ArrayList<>();
        List<Curriculum> initList = curriculumRepository.getCourseList(cid, subject, startDate, endDate);

        if (initList == null || initList.isEmpty()) {
            return resultList;
        }

//        courseCalRepository.deleteAll();

        for (Curriculum data : initList) {
            String date = data.getDate();
            int tid = data.getTid();

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
            int timeOfLesson = timeRepository.findById(tid).getTotal_time();
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

//            double general_a = 0.7;
//            double general_b = 0.3;
//
//            double generalRate = general_a * livenessRate + general_b * concentrationRate;

            LivenessVO livenessVO = new LivenessVO();
            livenessVO.setDate(date);
            livenessVO.setTid(tid);
            livenessVO.setSubject(subject);
//            livenessVO.setGeneralRate(generalRate);
            livenessVO.setLivenessRate(livenessRate);
            livenessVO.setConcentrationRate(concentrationRate);
            livenessVO.setGeneralRate();
            resultList.add(livenessVO);

            CourseCal courseCal = new CourseCal();
            courseCal.setCid(cid);
            courseCal.setConcentrationRate(concentrationRate);
            courseCal.setDate(date);
            courseCal.setGeneralRate(livenessVO.getGeneralRate());
            courseCal.setLivenessRate(livenessRate);
            courseCal.setSid(sid);
            courseCal.setTid(tid);
            courseCal.setSubject(subject);
            courseCalRepository.save(courseCal);

        }


        return resultList;
    }


    public List<LivenessVO> getLivenessInfoBysubject(int cid, int sid, String startDate, String endDate, String subject) {

        List<LivenessVO> resultList = new ArrayList<>();
        List<CourseCal> initList = courseCalRepository.findAll(cid, sid,subject, startDate, endDate);

        if (initList == null || initList.isEmpty()) {
            return resultList;
        }

//        courseCalRepository.deleteAll();

        for (CourseCal data : initList) {
            LivenessVO livenessVO = new LivenessVO();
            livenessVO.setDate(data.getDate());
            livenessVO.setTid(data.getTid());
            livenessVO.setSubject(subject);
//            livenessVO.setGeneralRate(generalRate);
            livenessVO.setLivenessRate(data.getLivenessRate());
            livenessVO.setConcentrationRate(data.getConcentrationRate());
            livenessVO.setGeneralRate(data.getGeneralRate());
            resultList.add(livenessVO);

        }


        return resultList;
    }


    /**
     * lesson 2
     * 每一门学科每一个课时的每个学生的课堂表现统计 排名percent （活跃度、参与度、综合表现）（echarts 学科-图接口）
     */
    public List<LivenessVO> getLivenessPercentBySubject(int cid, int sid, String startDate, String endDate, String subject) {
        List<LivenessVO> percentList = new ArrayList<>();

        List<Student> sidList = studentRepo.findByCid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return percentList;
        }
        //stuNum 不可能为零
        int stuNum = sidList.size();

        List<LivenessVO> primitiveList = getLivenessInfoBysubject(cid, sid, startDate, endDate, subject);
        if (primitiveList == null || primitiveList.isEmpty()) {
            return percentList;
        }


        for (int i = 0; i < primitiveList.size(); i++) {
            LivenessVO currentStu = primitiveList.get(i);
            int rank_con, rank_live, rank_gen;
            List<StudentDataVO> rankList_con = new ArrayList<>();
            List<StudentDataVO> rankList_live = new ArrayList<>();
            List<StudentDataVO> rankList_gen = new ArrayList<>();
            for (Student student : sidList) {
                int id = student.getSid();
                List<LivenessVO> compareList = getLivenessInfoBysubject(cid, id, startDate, endDate, subject);
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
                if (percent_gen < 0.2)
                    percent_gen = 0.2;
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
     * lesson 3
     * 每一门学科每天每个学生的课堂表现统计 数值（活跃度、参与度、综合表现）
     */
    private List<LivenessVO> getLivenessDailyBySubject(int cid, int sid, String startDate, String endDate, String subject) {

        List<LivenessVO> resultList = new ArrayList<>();
        List<LivenessVO> primitiveList = getLivenessInfoBysubject(cid, sid, startDate, endDate, subject);

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
     * lesson 3.1
     * 每一门学科每周每个学生的课堂表现统计 数值（活跃度、参与度、综合表现）
     */
    private List<LivenessVO> getLivenessWeeklyBySubject(int cid, int sid, String startDate, String endDate, String subject) {

        int period = (int) (DateUtil.getDistanceOfTwoDate(DateUtil.parseDate(startDate), DateUtil.parseDate(endDate)));

        List<LivenessVO> resultList = new ArrayList<>();
        String start = startDate;
        String end = startDate;
        for (int i = period / 7; i > 0; i--) {
            start = end;
            end = DateUtil.getFutureDate(7, start);
            LivenessVO temp = getGenaralInfoBySubject(cid, sid, start, end, subject);
            temp.setDate("前" + i + "周");
            resultList.add(temp);
        }
        return resultList;
    }

    /**
     * lesson 5
     * 每一门学科每天每个学生的课堂表现统计 排名percent（活跃度、参与度、综合表现）每天都填充
     */
//    private List<LivenessVO> getLivenessInfoEveryDay(int cid, int sid, String startDate, String endDate, String subject) {
//
//        List<LivenessVO> resultList = new ArrayList<>();
//
//        List<LivenessVO> primitivelist = getLivenessPercentDaily(cid, sid, startDate, endDate, subject);
//
//        if (primitivelist == null || primitivelist.isEmpty()) {
//            return resultList;
//        }
//        int period = (int) (DateUtil.getDistanceOfTwoDate(DateUtil.parseDate(startDate), DateUtil.parseDate(endDate)));
//
//        if (period == primitivelist.size()) {
//            return primitivelist;
//        }
//
//        for (int i = period - 1; i >= 0; i--) {
//            String date = DateUtil.getPassedDate(i, endDate);
//
//            try {
//                if (DateUtil.dayForWeek(date) == 6 || DateUtil.dayForWeek(date) == 7) {
//                    continue;
//                }
//            } catch (Exception e) {
//
//            }
//            boolean dateExist = false;
//            for (int j = 0; j < primitivelist.size(); j++) {
//                LivenessVO livenessVO = primitivelist.get(j);
//                if (livenessVO.getDate().equals(date)) {
//                    resultList.add(livenessVO);
//                    primitivelist.remove(j);
//                    dateExist = true;
//                    break;
//                }
//            }
//            if (!dateExist) {
//                LivenessVO livenessVO = new LivenessVO();
//                livenessVO.setDate(date);
//                livenessVO.setSubject(subject);
//                resultList.add(livenessVO);
//            }
//        }
//
//        return resultList;
//    }

    /**
     * lesson 4
     * 每一门学科每天每个学生的课堂表现统计 排名percent（活跃度、参与度、综合表现）
     */
    private List<LivenessVO> getLivenessPercentDaily(int cid, int sid, String startDate, String endDate, String subject) {

        List<LivenessVO> percentList = new ArrayList<>();

        List<Student> sidList = studentRepo.findByCid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return percentList;
        }
        //stuNum 不可能为零
        int stuNum = sidList.size();

        List<LivenessVO> primitiveList = getLivenessDailyBySubject(cid, sid, startDate, endDate, subject);
        if (primitiveList == null || primitiveList.isEmpty()) {
            return percentList;
        }
        for (int i = 0; i < primitiveList.size(); i++) {
            LivenessVO currentStu = primitiveList.get(i);
            int rank_con, rank_live, rank_gen;
            List<StudentDataVO> rankList_con = new ArrayList<>();
            List<StudentDataVO> rankList_live = new ArrayList<>();
            List<StudentDataVO> rankList_gen = new ArrayList<>();
            for (Student student : sidList) {
                int id = student.getSid();
                List<LivenessVO> compareList = getLivenessDailyBySubject(cid, id, startDate, endDate, subject);
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
                if (percent_gen < 0.2)
                    percent_gen = 0.2;
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
//    private List<LivenessShowVO> getLivenessPercentEveryDay(int cid, int sid, String startDate, String endDate) {
//
//        List<LivenessShowVO> resultList = new ArrayList<>();
//
//        List<String> initList = curriculumRepository.getDistinctSubject(cid);
//
//        if (initList == null || initList.isEmpty()) {
//            return resultList;
//        }
//
//        for (String s : initList) {
//            if (s.equals("自习")) {
//                continue;
//            }
//            List<LivenessVO> primitiveList = getLivenessInfoEveryDay(cid, sid, startDate, endDate, s);
//            LivenessShowVO livenessShowVO = new LivenessShowVO();
//            livenessShowVO.setSubject(s);
//            livenessShowVO.setData(primitiveList);
//            resultList.add(livenessShowVO);
//        }
//        return resultList;
//    }
    public List<LivenessVO> getLivenessPercentDay(int cid, int sid, String startDate, String endDate) {

        List<LivenessVO> resultList = new ArrayList<>();

        List<String> initList = curriculumRepository.getDistinctSubject(cid);

        if (initList == null || initList.isEmpty()) {
            return resultList;
        }

        for (String s : initList) {
            if (s.equals("自习")) {
                continue;
            }
            LivenessVO livenessVO = getGeneralLivenessPercentBySubject(cid, sid, startDate, endDate, s);
            resultList.add(livenessVO);
        }
        return resultList;
    }


    /**
     * lesson 4.1
     * 每一门学科周每个学生的课堂表现统计 排名percent（活跃度、参与度、综合表现）
     */
    private List<LivenessVO> getLivenessPercentWeekly(int cid, int sid, String startDate, String endDate, String subject) {

        List<LivenessVO> percentList = new ArrayList<>();
        List<Student> sidList = studentRepo.findByCid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return percentList;
        }
        //stuNum 不可能为零
        int stuNum = sidList.size();

        List<LivenessVO> primitiveList = getLivenessWeeklyBySubject(cid, sid, startDate, endDate, subject);
        if (primitiveList == null || primitiveList.isEmpty()) {
            return percentList;
        }
        for (int i = 0; i < primitiveList.size(); i++) {
            LivenessVO currentStu = primitiveList.get(i);
            int rank_con, rank_live, rank_gen;
            List<StudentDataVO> rankList_con = new ArrayList<>();
            List<StudentDataVO> rankList_live = new ArrayList<>();
            List<StudentDataVO> rankList_gen = new ArrayList<>();
            for (Student student : sidList) {
                int id = student.getSid();
                List<LivenessVO> compareList = getLivenessWeeklyBySubject(cid, id, startDate, endDate, subject);
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
                if (percent_gen < 0.2)
                    percent_gen = 0.2;
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
     * 每一门学科周每个学生的课堂表现统计 percent（活跃度、参与度、综合表现）时间段内 所有学科 (echarts 全部-图接口)
     */
    public List<LivenessShowVO> getLivenessPercentEveryWeek(int cid, int sid, String startDate, String endDate) {

        List<LivenessShowVO> resultList = new ArrayList<>();

        List<String> initList = curriculumRepository.getDistinctSubject(cid);

        if (initList == null || initList.isEmpty()) {
            return resultList;
        }

        for (String s : initList) {
            if (s.equals("自习")) {
                continue;
            }
            List<LivenessVO> primitiveList = getLivenessPercentWeekly(cid, sid, startDate, endDate, s);
            LivenessShowVO livenessShowVO = new LivenessShowVO();
            livenessShowVO.setSubject(s);
            livenessShowVO.setData(primitiveList);
            resultList.add(livenessShowVO);
        }
        return resultList;
    }


    /**
     * 每一门学科一段时间内的平均表现 数值（去掉有请假的极端情况）判断每一科是否有进步
     */
    private LivenessVO getGenaralInfoBySubject(int cid, int sid, String startDate, String endDate, String subject) {

        LivenessVO livenessVO = new LivenessVO();
        livenessVO.setSubject(subject);

        List<LivenessVO> livenessVOList = getLivenessInfoBysubject(cid, sid, startDate, endDate, subject);

        double livenessRate = 0;
        double concentrationRate = 0;
        double handsUpTimes = 0;

        if (livenessVOList == null || livenessVOList.isEmpty()) {
            return livenessVO;
        } else {
            double sumLivenessRate = 0;
            double sumConcentrationRate = 0;
            double sumhandsUpTimes = 0;
            int attendTimes = livenessVOList.size();

            for (LivenessVO temp : livenessVOList) {
                //这里需要处理请假，因为是各个科目的总体情况，需要处理极端数据：请假不计入计数，未请假仍计入
                //注意一种情况：迟到和早退、以及离开时间不算太长的话，举手次数相对比较多时课堂参与度也会相对来说较高，因此还是计入为好，故只计算课堂参与度低于0.3的情况
                if (temp.getGeneralRate() < 0.5) {
                    if (behaviorRipository.countByCidAndSidAndDateAndTidAndStatus(cid, sid, temp.getDate(), temp.getTid(), "已请假") != 0) {
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
     * 获得某学生各科在某个时间段内的课堂平均表现统计 数值（活跃度、参与度、综合表现）
     */
//    public List<LivenessVO> getLivenessInfo(int cid, int sid, int period) {
//
//        List<LivenessVO> resultList = new ArrayList<>();
//        String endDate = DateUtil.getDate();
//        String startDate = DateUtil.getPassedDate(period, endDate);
//
//        List<String> subjects = curriculumRepository.getDistinctSubject(cid);
//        if (subjects == null || subjects.isEmpty()) {
//            return resultList;
//        }
//        for (String s : subjects) {
//            if (s.equals("自习")) {
//                continue;
//            }
//            LivenessVO livenessVO = getGenaralInfoBySubject(cid, sid, startDate, endDate, s);
//            resultList.add(livenessVO);
//        }
//        return resultList;
//    }

    ;

    /**
     * 获得某学生在某个时间段内的平均所有课堂表现统计 数值（活跃度、参与度、综合表现）各科按权重计算
     */
    private LivenessVO getGeneralLivenessRate(int cid, int sid, String startDate, String endDate) {
        /**
         * 总活跃度需要考虑各个课程的权重，目前根据在统计时间内各科各有多少课来设定权重 VO(course,times)
         */

        List<String> subjects = curriculumRepository.getDistinctSubject(cid);
        LivenessVO result = new LivenessVO();

        if (subjects == null || subjects.isEmpty()) {
            return result;
        }

        int totalCourse = 0;


        double gen_rate = 0.0;
        double con_rate = 0.0;
        double live_rate = 0.0;
        for (String s : subjects) {
            if (s.equals("自习")) {
                continue;
            }
            int weight = curriculumRepository.countCourse(cid, startDate, endDate, s);
            LivenessVO livenessVO = getGenaralInfoBySubject(cid, sid, startDate, endDate, s);
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
     * 获得某学生在某个时间段内的综合课堂表现统计 percent（活跃度、参与度、综合表现）判断整体是否有进步
     */

    public LivenessVO getGeneralLivenessPercent(int cid, int sid, String startDate, String endDate) {
        LivenessVO percent = new LivenessVO();

        List<Student> sidList = studentRepo.findByCid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return percent;
        }
        //stuNum 不可能为零
        int stuNum = sidList.size();

        LivenessVO currentStu = getGeneralLivenessRate(cid, sid, startDate, endDate);

        int rank_con, rank_live, rank_gen;
        List<StudentDataVO> rankList_con = new ArrayList<>();
        List<StudentDataVO> rankList_live = new ArrayList<>();
        List<StudentDataVO> rankList_gen = new ArrayList<>();

        for (Student student : sidList) {
            int id = student.getSid();
            LivenessVO compareStu = getGeneralLivenessRate(cid, id, startDate, endDate);

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
            if (percent_gen < 0.2)
                percent_gen = 0.2;
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
     * 每一门学科一段时间内的平均表现 percent（去掉有请假的极端情况）判断每一科是否有进步、退步
     * 可以看各科的方差
     */
    public LivenessVO getGeneralLivenessPercentBySubject(int cid, int sid, String startDate, String endDate, String subject) {

        LivenessVO percent = new LivenessVO();

        List<Student> sidList = studentRepo.findByCid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return percent;
        }
        //stuNum 不可能为零
        int stuNum = sidList.size();

        LivenessVO currentStu = getGenaralInfoBySubject(cid, sid, startDate, endDate, subject);

        int rank_con, rank_live, rank_gen;
        List<StudentDataVO> rankList_con = new ArrayList<>();
        List<StudentDataVO> rankList_live = new ArrayList<>();
        List<StudentDataVO> rankList_gen = new ArrayList<>();

        for (Student student : sidList) {
            int id = student.getSid();
            LivenessVO compareStu = getGenaralInfoBySubject(cid, id, startDate, endDate, subject);

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
            if (percent_gen < 0.2)
                percent_gen = 0.2;
        }

        percent.setGeneralRate(percent_gen);
        percent.setLivenessRate(percent_live);
        percent.setConcentrationRate(percent_con);

        percent.setDate(currentStu.getDate());
        percent.setTid(currentStu.getTid());
        percent.setSubject(currentStu.getSubject());

        return percent;

    }

    ;;

    /**
     * 自习列表
     *
     * @param cid
     * @param sid
     * @param startDate
     * @param endDate
     * @return
     */

    public List<DisciplineVO> getDisplineInfo(int cid, int sid, String startDate, String endDate) {
        List<DisciplineVO> disciplineVOList = new ArrayList<>();

        List<Behavior> behaviorList = behaviorRipository.findBehaviorsDuringStudy(cid, sid, startDate, endDate);


        boolean flag = true;
//        List<PatternHandle> patternHandleList = patternHandleRepository.findByCidAndSid(cid,sid);
//        if(patternHandleList==null||patternHandleList.isEmpty()){
//            flag = false;
//        }else {for(PatternHandle patternHandle :patternHandleList){
//            String hid = patternHandle.getHid();
//            List<Pattern> patterns = patternRepository.findAllByHandle_idAndDateAfterAndDateBefore(hid,startDate,endDate);
//            if (patterns==null || patterns.isEmpty()){
//
//            }else{
//                for(Pattern pattern :patterns){
//                    DisciplineVO disciplineVO = new DisciplineVO(pattern.getDate()+"", pattern.getDate()+"", pattern.getDestribute(), pattern.getId(),pattern.getHandle_id());
//                    disciplineVOList.add(disciplineVO);
//                }
//            }
//        }
//        }
        if (behaviorList == null || behaviorList.isEmpty()) {
            return disciplineVOList;
        }
        for (Behavior b : behaviorList) {
            DisciplineVO disciplineVO = new DisciplineVO(b.getDate(), b.getStart(), b.getAction(), b.getTotal_time(), b.getStatus());
            disciplineVOList.add(disciplineVO);
        }

        /**
         * 假设自习的违纪行为都存在列表当中
         */

        return disciplineVOList;

    }

    ;

    /**
     * 自习守序度
     *
     * @param cid
     * @param sid
     * @param startDate
     * @param endDate
     * @return
     */
    private double getDisciplineRate(int cid, int sid, String startDate, String endDate) {

        double disciplineRate = 1;

        List<Behavior> behaviorList = behaviorRipository.findBehaviorsDuringStudy(cid, sid, startDate, endDate);

        if (behaviorList == null || behaviorList.isEmpty()) {
            return disciplineRate;
        }

        int studyNum = curriculumRepository.countCourse(cid, startDate, endDate, "自习");
        int studyTime = timeRepository.findById(1).getTotal_time();

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

    public double getDisciplineRange(int cid, int sid, String startDate, String endDate) {

        double rangePercent = 0;
        double currentRate = getDisciplineRate(cid, sid, startDate, endDate);
        double topRate = currentRate;
        List<Student> sidList = studentRepo.findByCid(cid);
        if (sidList == null || sidList.isEmpty()) {
            return rangePercent;
        }

        for (Student student : sidList) {
            int id = student.getSid();
            double rate = getDisciplineRate(cid, id, startDate, endDate);
            if (rate > topRate) {
                topRate = rate;
            }
        }

        if (topRate <= 0 || currentRate < 0) {
            return rangePercent;
        }

        rangePercent = currentRate / topRate;
        return rangePercent;

    }

    //    private double getDisciplinePercent(int cid, int sid, int period) {
//
//        double rankPercent = 0;
//        int rank = 0;
//        List<Integer> sidList = studentRepo.getSid(cid);
//        if (sidList == null || sidList.isEmpty()) {
//            return rankPercent;
//        }
//        List<StudentDataVO> rankList = new ArrayList<>();
//        for (Integer integer : sidList) {
//            int id = (int) integer;
//            StudentDataVO studentDataVO = new StudentDataVO(id, getDisciplineRate(cid, id, period));
//            rankList.add(studentDataVO);
//        }
//        rank = getSort(rankList, sid);
//        int num = sidList.size();
//
//        if (num == 0) {
//            return rankPercent;
//        }
//        rankPercent = 1 - 1.0 * (rank - 0.9) / num;
//        return rankPercent;
//
//    }

    ;
}
