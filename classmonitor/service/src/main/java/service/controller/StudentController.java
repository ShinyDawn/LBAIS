package service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.dao.Student;
import service.service.StudentService;

@Controller
public class StudentController {
	@Autowired
	private StudentService studentService;

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
