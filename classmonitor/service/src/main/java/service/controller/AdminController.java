package service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.entity.User;
import service.service.AdminService;

@Controller
public class AdminController {

	@Autowired
	private AdminService admin;

	@RequestMapping(value = "/admin")
	@ResponseBody
	public List<User> index() {
		return admin.findAll();
	}

	@RequestMapping(value = "/admin/add")
	@ResponseBody
	public void add(@RequestParam("id") int id,@RequestParam("uid") String uid, @RequestParam("name") String name,
			@RequestParam("email") String email, @RequestParam("tel") String tel,
			@RequestParam("password") String password) {
		
		admin.add(uid, name, email, tel, password);
		return;
	}

	@RequestMapping(value = "/admin/modify")
	@ResponseBody
	public void modify(@RequestParam("id") int id, @RequestParam("uid") String uid, @RequestParam("name") String name,
			@RequestParam("email") String email, @RequestParam("tel") String tel,
			@RequestParam("password") String password) {
		
		admin.modify(id, uid, name, email, tel, password);
		return;
	}

	@RequestMapping(value = "/admin/delete")
	@ResponseBody
	public void delete(@RequestParam("id") int id) {
		admin.delete(id);
		return;
	}
}
