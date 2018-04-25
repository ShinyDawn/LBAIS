package service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.service.CurriculumService;
import service.vo.CurriculumVO;

@Controller
public class CurriculumController {
	@Autowired
	private CurriculumService curriculumService;

	@RequestMapping(value = "/curriculum")
	@ResponseBody
	public List<CurriculumVO> index(@RequestParam("cid") int cid) {
		return curriculumService.getCurriculum(cid);
	}

	@RequestMapping(value = "/curriculum/ac")
	@ResponseBody
	public void addCurriculum(@RequestParam("cid") int cid, @RequestParam("tid") int tid, @RequestParam("day") int day,
			@RequestParam("course") String course) {
		curriculumService.addCourse(cid, tid, day, course);
	}

	@RequestMapping(value = "/curriculum/at")
	@ResponseBody
	public int addTime(@RequestParam("id") int id, @RequestParam("cid") int cid, @RequestParam("time") String time) {
		return curriculumService.addTime(id, cid, time);
	}

	@RequestMapping(value = "/curriculum/delete")
	@ResponseBody
	public void deleteLine(@RequestParam("cid") int cid, @RequestParam("tid") int tid) {
		curriculumService.deleteOneLine(cid, tid);
	}
}
