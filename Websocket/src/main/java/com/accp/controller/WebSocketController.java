package com.accp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accp.domain.User;
import com.accp.server.WebSocketServer;

@Controller
@ResponseBody
@RequestMapping("/websocket")
public class WebSocketController {

	@Autowired
	WebSocketServer websocketServer;
	
	@RequestMapping("/sendMsg")
	public void sendMsg(String id,String msg,HttpSession session) {
		User user=(User)session.getAttribute("user");
		websocketServer.sendInfo(id, user.getId()+"",msg);
	}
}
