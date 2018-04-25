package service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.dao.User;
import service.repository.UserRepository;
import service.service.AdminService;

@Service
public class AdminImpl implements AdminService{
	@Autowired
	private UserRepository userRepo;

	@Override
	public List<User> findAll() {
		return userRepo.findAll();
	}

	@Override
	public void add(String uid, String name, String email, String tel, String password) {
		User user = new User();
		user.setUid(uid);
		user.setEmail(email);
		user.setName(name);
		user.setTel(tel);
		user.setPassword(password);
		userRepo.save(user);
	}

	@Override
	public void modify(int id, String uid, String name, String email, String tel, String password) {
		userRepo.updateById(id, uid, name, email, tel, password);
	}

	@Override
	public void delete(int id) {
		userRepo.deleteById(id);
	}
}
