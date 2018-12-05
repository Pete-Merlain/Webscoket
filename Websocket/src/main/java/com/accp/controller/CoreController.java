package com.accp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CoreController {
		
	
	@RequestMapping("/websocket")
	public String websocket() {
		return "websocket";
	}
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
}
