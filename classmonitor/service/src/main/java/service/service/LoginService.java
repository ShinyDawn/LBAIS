package service.service;

import service.entity.User;

public interface LoginService {
	public boolean verifyLogin(User user);

	public String Check(String uid,String password);
}
