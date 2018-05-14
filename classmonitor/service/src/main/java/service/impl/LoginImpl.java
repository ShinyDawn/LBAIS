package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.entity.User;
import service.repository.UserRepository;
import service.service.LoginService;

import java.util.List;

@Service
public class LoginImpl implements LoginService {
	@Autowired
	private UserRepository userRepo;

	@Override
	public String Check(String uid, String password) {
		User user = userRepo.findByUidAndPassword(uid, password);
		
		if(user==null)
			return "null";
		else if(user.getUid().equals("admin"))
			return "admin";
		else
			return user.getName();


	}

	public boolean verifyLogin(User user){

		List<User> userList = userRepo.findAllByUidAndAndPassword(user.getUid(), user.getPassword());
		return userList.size()>0;
	}

}
