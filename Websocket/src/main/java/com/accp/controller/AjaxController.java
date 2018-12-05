package com.accp.controller;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accp.domain.User;
import com.accp.service.UserService;

@Controller
@RestController
@RequestMapping("/ajax")
public class AjaxController{

	@Autowired
	HttpSession session;
	@Autowired
	UserService userService;
	
	@RequestMapping("/login")
	public boolean login(User user) {
		User user2=userService.login(user);
		session.setAttribute("user", user2);
		System.out.println("登陆成功!username:"+user2.getName());
		return user2!=null;
	}
	
		
	@RequestMapping("/users")
	public List<User> users(int id){
		return userService.users(id);
	}
	
}
