package com.accp.service;

import java.util.List;

import com.accp.domain.User;

public interface UserService {

	User queryById(int id);
	
	User login(User user);
	
	List<User> users(int id);
}
