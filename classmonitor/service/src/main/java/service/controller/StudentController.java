package service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.entity.Student;
import service.service.StudentService;
import service.util.DateUtil;
import service.vo.*;

@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/test")
    @ResponseBody
    public List<LivenessVO> getLivenessInfoBysubject(@RequestParam("cid") int cid,@RequestParam("sid") int sid, @RequestParam("period") int period, @RequestParam("subject") String subject) {
        String endDate = DateUtil.getDate();
        String startDate = DateUtil.getPassedDate(period, endDate);
        return studentService. getLivenessPercentBySubject(cid, sid,startDate,endDate,subject);
    }

    @RequestMapping(value = "/tester")
    @ResponseBody
    public int init(@RequestParam("cid") int cid,@RequestParam("period") int period) {
        String endDate = DateUtil.getDate();
        String startDate = DateUtil.getPassedDate(period, endDate);
        return studentService. initCourse(cid, startDate,endDate);
    }



    /**
     * 用来获得首页所有信息
     *
     * @param cid
     * @param period
     * @return
     */

    @RequestMapping(value = "/students")
    @ResponseBody
    public List<StudentInfoVO> getStudentsInfo(@RequestParam("cid") int cid, @RequestParam("period") int period) {
        String endDate = DateUtil.getDate();
        String startDate = DateUtil.getPassedDate(period, endDate);
        return studentService.getStudentsInfoList(cid, startDate,endDate);
    }

    /**
     * 用来获得综合分析模块
     *
     * @param cid
     * @param sid
     * @param period
     * @return
     */
    @RequestMapping(value = "/studentInfo")
    @ResponseBody
    public StudentInfoVO getStudentInfo(@RequestParam("cid") int cid, @RequestParam("sid") int sid, @RequestParam("period") int period) {
        String endDate = DateUtil.getDate();
        String startDate = DateUtil.getPassedDate(period, endDate);
        return studentService.getStudentProblemInfo(cid, sid, startDate,endDate);
    }

    /**
     * 用来获得学生信息
     *
     * @param cid
     * @param sid
     * @return
     */
    @RequestMapping(value = "/student/get")
    @ResponseBody
    public Student index(@RequestParam("cid") int cid, @RequestParam("sid") int sid) {
        return studentService.getOneInfo(cid, sid);
    }

    /**
     * 用来获得出勤表
     *
     * @param cid
     * @param period
     * @return
     */
    @RequestMapping(value = "/student/attendance")
    @ResponseBody
    public List<AttendanceVO> getAttendenceInfo(@RequestParam("cid") int cid, @RequestParam("sid") int sid, @RequestParam("period") int period) {
        String endDate = DateUtil.getDate();
        String startDate = DateUtil.getPassedDate(period, endDate);
        return studentService.getAttendanceInfo(cid, sid, startDate, endDate);
    }


    /**
     * 用来获得出勤率
     *
     * @param cid
     * @param sid
     * @param period
     * @return
     */
    @RequestMapping(value = "/student/attendanceRate")
    @ResponseBody
    public double getAttendencePercent(@RequestParam("cid") int cid, @RequestParam("sid") int sid, @RequestParam("period") int period) {
        String endDate = DateUtil.getDate();
        String startDate = DateUtil.getPassedDate(period, endDate);
        return studentService.getAttendanceRate(cid, sid, startDate, endDate);
    }

    /**
     * 课堂表现 - 学科图
     *
     * @param cid
     * @param sid
     * @param period
     * @param subject
     * @return
     */
    @RequestMapping(value = "/student/lesson/subject")
    @ResponseBody
    public List<LivenessVO> getLivenessPercentBySubject(@RequestParam("cid") int cid, @RequestParam("sid") int sid, @RequestParam("period") int period, @RequestParam("subject") String subject) {
        String endDate = DateUtil.getDate();
        String startDate = DateUtil.getPassedDate(period, endDate);
        return studentService.getLivenessPercentBySubject(cid, sid, startDate, endDate, subject);
    }


    /**
     * 课堂表现 - 全部图
     *
     * @param cid
     * @param sid
     * @param period
     * @return
     */
    @RequestMapping(value = "/student/lesson")
    @ResponseBody
    public List<LivenessVO> getLivenessPercentDaily(@RequestParam("cid") int cid, @RequestParam("sid") int sid, @RequestParam("period") int period) {
        String endDate = DateUtil.getDate();
        String startDate = DateUtil.getPassedDate(period, endDate);
        return studentService.getLivenessPercentDay(cid, sid, startDate,endDate);
    }

    /**
     * 课堂表现 - 全部图 按周统计
     *
     * @param cid
     * @param sid
     * @param period
     * @return
     */
    @RequestMapping(value = "/student/lessonweek")
    @ResponseBody
    public List<LivenessShowVO> getLivenessPercentEveryWeek(@RequestParam("cid") int cid, @RequestParam("sid") int sid, @RequestParam("period") int period) {
        String endDate = DateUtil.getDate();
        String startDate = DateUtil.getPassedDate(period, endDate);
        return studentService.getLivenessPercentEveryWeek(cid, sid, startDate, endDate);
    }



    /**
     * 获得各科的课堂表现排名percent
     *
     * @param cid
     * @param sid
     * @param period
     * @param subject
     * @return
     */
    @RequestMapping(value = "/student/livenessPercent/subject")
    @ResponseBody
    public LivenessVO getLivenessRateBySubjectPercent(@RequestParam("cid") int cid, @RequestParam("sid") int sid, @RequestParam("period") int period, @RequestParam("subject") String subject) {
        String endDate = DateUtil.getDate();
        String startDate = DateUtil.getPassedDate(period, endDate);
        return studentService.getGeneralLivenessPercentBySubject(cid, sid, startDate, endDate, subject);
    }

    /**
     * 用来获得课堂综合表现排名
     *
     * @param cid
     * @param sid
     * @param period
     * @return
     */

    @RequestMapping(value = "/student/livenessPercent")
    @ResponseBody
    public LivenessVO getLivenessPercent(@RequestParam("cid") int cid, @RequestParam("sid") int sid, @RequestParam("period") int period) {
        String endDate = DateUtil.getDate();
        String startDate = DateUtil.getPassedDate(period, endDate);
        return studentService.getGeneralLivenessPercent(cid, sid, startDate, endDate);

    }

    /**
     * 用来获得自习纪律情况
     *
     * @param cid
     * @param sid
     * @param period
     * @return
     */
    @RequestMapping(value = "/student/discipline")
    @ResponseBody
    public List<DisciplineVO> getDisplineInfo(@RequestParam("cid") int cid, @RequestParam("sid") int sid, @RequestParam("period") int period) {
        String endDate = DateUtil.getDate();
        String startDate = DateUtil.getPassedDate(period, endDate);
        return studentService.getDisplineInfo(cid, sid, startDate, endDate);
    }


    /**
     * 用来获得自习纪律排名
     *
     * @param cid
     * @param sid
     * @param period
     * @return
     */
    @RequestMapping(value = "/student/disciplinePercent")
    @ResponseBody
    public double getDisciplinePercent(@RequestParam("cid") int cid, @RequestParam("sid") int sid, @RequestParam("period") int period) {
        String endDate = DateUtil.getDate();
        String startDate = DateUtil.getPassedDate(period, endDate);
        return studentService.getDisciplineRange(cid, sid, startDate, endDate);
    }

}
