package service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.dao.Classroom;
import service.service.ClassroomService;

@Controller
public class ClassroomController {

	@Autowired
	private ClassroomService classroom;

	@RequestMapping(value = "/classroom")
	@ResponseBody
	public List<Classroom> index() {
		return classroom.findAllClasses();
	}
	
	@RequestMapping(value = "/classroom/info")
	@ResponseBody
	public Classroom getInfo(@RequestParam("id") int id) {
		return classroom.getInfo(id);
	}

	@RequestMapping(value = "/classroom/add")
	@ResponseBody
	public void add(@RequestParam("id") int id, @RequestParam("cname") String cname,
			@RequestParam("teacher") String teacher, @RequestParam("num") int num, @RequestParam("grade") String grade,
			@RequestParam("date") String date) {

		classroom.addOne(cname, teacher, num, grade, date);
		return;
	}

	@RequestMapping(value = "/classroom/update")
	@ResponseBody
	public void update(@RequestParam("id") int id, @RequestParam("cname") String cname,
			@RequestParam("teacher") String teacher, @RequestParam("num") int num, @RequestParam("grade") String grade,
			@RequestParam("date") String date) {

		classroom.updateOne(id, cname, teacher, num, grade, date);
		return;
	}

	@RequestMapping(value = "/classroom/delete")
	@ResponseBody
	public void delete(@RequestParam("id") int id) {

		classroom.deleteOne(id);
		;
		return;
	}
}
