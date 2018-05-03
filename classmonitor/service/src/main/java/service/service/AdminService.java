package service.service;

import java.util.List;

import service.entity.User;

public interface AdminService {

	public List<User> findAll();

	public void add(String uid, String name, String email, String tel, String password);

	public void modify(int id, String uid, String name, String email, String tel, String password);

	public void delete(int id);
}
