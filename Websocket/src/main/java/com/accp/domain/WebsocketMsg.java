package com.accp.domain;

public class WebsocketMsg {

	//状态码
	private int state;
	//推送内容
	private String message;
	//true为信息,false为通知
	private Boolean type;
	private final static String SUCCESS = "对方已接受信息";
	private final static String OFF_LINE = "对方离线，发送失败";
	private final static String ERROE = "未知异常，请联系管理员";

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getType() {
		return type;
	}

	public void setType(Boolean type) {
		this.type = type;
	}
	
	public WebsocketMsg() {

	}

	public WebsocketMsg(int state) {
		this.state = state;
		switch (state) {
		case 200:
			this.message=SUCCESS;
			break;
		case 400:
			this.message=OFF_LINE;
			break;
		default:
			this.message=ERROE;
			break;
		}
		this.type = false;
	}

	public WebsocketMsg(String msg) {
		this.state = 200;
		this.message = msg;
		this.type = true;
	}

	public WebsocketMsg(String msg,boolean type) {
		this.state = 200;
		this.message = msg;
		this.type = type;
	}

}
