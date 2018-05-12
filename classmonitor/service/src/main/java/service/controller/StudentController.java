package service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.entity.Student;
import service.service.StudentService;
import service.vo.AttendanceVO;
import service.vo.DisciplineVO;
import service.vo.LivenessVO;
import service.vo.StudentInfoVO;

@Controller
public class StudentController {
	@Autowired
	private StudentService studentService;


    @RequestMapping(value = "/student")
    @ResponseBody
    public List<StudentInfoVO> getStudentsInfo(@RequestParam("cid") int cid ){
        return studentService.getStudentsInfoList(cid,7);
    }
	/**
	 *
	 * @param cid 班级id
	 * @param sid 学生在班级里的id
	 * @param period 7（最近1周）/30（1个月）/150（1学期）/365（1学年）
	 * @return 出勤情况List
	 */
	@RequestMapping(value = "/student/attendance")
	@ResponseBody
	public List<AttendanceVO> getAttendenceInfo(@RequestParam("cid") int cid, @RequestParam("sid")int sid, @RequestParam("period")int period ){
		return studentService.getAttendanceInfo(cid,sid,period);
	}

	@RequestMapping(value = "/student/attendanceRate")
	@ResponseBody
	public double getAttendenceRate(@RequestParam("cid") int cid, @RequestParam("sid")int sid, @RequestParam("period")int period ){
		return studentService.getAttendenceRate(cid,sid,period);
	}

    @RequestMapping(value = "/student/attendancePercent")
    @ResponseBody
    public double getAttendencePrecent(@RequestParam("cid") int cid, @RequestParam("sid")int sid, @RequestParam("period")int period){
        return studentService.getAttendencePrecent(cid,sid,period);

    }

	@RequestMapping(value = "/student/liveness/subject")
	@ResponseBody
	public List<LivenessVO> getLivenessInfoBySubject(@RequestParam("cid") int cid, @RequestParam("sid")int sid, @RequestParam("period")int period , @RequestParam("subject") String subject){
		return studentService.getLivenessInfoBysubject(cid,sid,period,subject);
	}

	@RequestMapping(value = "/student/liveness")
	@ResponseBody
	public List<LivenessVO> getLivenessInfo(@RequestParam("cid") int cid, @RequestParam("sid")int sid, @RequestParam("period")int period){
		return studentService.getLivenessInfo(cid,sid,period);
	}

	@RequestMapping(value = "/student/livenessRate/subject")
	@ResponseBody
	public double getLivenessRateBySubject(@RequestParam("cid") int cid, @RequestParam("sid")int sid, @RequestParam("period")int period,@RequestParam("subject") String subject){
		return studentService.getGeneralLivenessRateBySubject(cid,sid,period,subject);
	}

	@RequestMapping(value = "/student/livenessPercent/subject")
	@ResponseBody
	public double getLivenessRateBySubjectPercent(@RequestParam("cid") int cid, @RequestParam("sid")int sid, @RequestParam("period")int period,@RequestParam("subject") String subject){
		return studentService.getGeneralLivenessPercentBySubject(cid,sid,period,subject);
	}

	@RequestMapping(value = "/student/livenessRate")
	@ResponseBody
	public double getLivenessRate(@RequestParam("cid") int cid, @RequestParam("sid")int sid, @RequestParam("period")int period){
		return studentService.getGeneralLivenessRate(cid,sid,period);
	}

	@RequestMapping(value = "/student/livenessPercent")
	@ResponseBody
	public double getLivenessPercent(@RequestParam("cid") int cid, @RequestParam("sid")int sid, @RequestParam("period")int period){
		return studentService.getGeneralLivenessPercent(cid,sid,period);

	}

    @RequestMapping(value = "/student/discipline")
    @ResponseBody
    public List<DisciplineVO> getDisplineInfo(@RequestParam("cid") int cid, @RequestParam("sid")int sid, @RequestParam("period")int period){
        return studentService.getDisplineInfo(cid,sid,period);
    }

    @RequestMapping(value = "/student/disciplineRate")
    @ResponseBody
    public double getDisciplineRate(@RequestParam("cid") int cid, @RequestParam("sid")int sid, @RequestParam("period")int period){
        return studentService.getDisciplineRate(cid,sid,period);

    }

    @RequestMapping(value = "/student/disciplinePercent")
    @ResponseBody
    public double getDisciplinePercent(@RequestParam("cid") int cid, @RequestParam("sid")int sid, @RequestParam("period")int period){
        return studentService.getDisciplinePercent(cid,sid,period);

    }

	@RequestMapping(value = "/students")
	@ResponseBody
	public List<Student> index(@RequestParam("cid") int cid) {
		return studentService.getInfo(cid);
	}

	@RequestMapping(value = "/students/add")
	@ResponseBody
	public int add(@RequestParam("id") int id, @RequestParam("cid") int cid, @RequestParam("sid") int sid,
			@RequestParam("name") String name, @RequestParam("gender") String gender, @RequestParam("x") int x,
			@RequestParam("y") int y) {
		return studentService.add(cid, sid, name, gender, x, y);
	}

	@RequestMapping(value = "/students/update")
	@ResponseBody
	public int update(@RequestParam("id") int id, @RequestParam("cid") int cid, @RequestParam("sid") int sid,
			@RequestParam("name") String name, @RequestParam("gender") String gender, @RequestParam("x") int x, @RequestParam("y") int y) {
		return studentService.update(id, cid, sid, name, gender, x, y);
	}

	@RequestMapping(value = "/students/delete")
	@ResponseBody
	public void delete(@RequestParam("id") int id) {
		studentService.delete(id);
	}
}
