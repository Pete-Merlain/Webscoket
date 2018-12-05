package com.accp.server;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.accp.config.WsGetHttpSessionConfig;
import com.accp.domain.User;
import com.accp.domain.WebsocketMsg;
import com.alibaba.fastjson.JSON;

@Component
@ServerEndpoint(value="/we/{id}",configurator=WsGetHttpSessionConfig.class)
public final class WebSocketServer {

	private String id;

	private HttpSession httpSession;

	private Session session;

	/*总连接人数*/
	private static int onlineCount=0;	
	
	static Log log=LogFactory.getLog(WebSocketServer.class);

	/**
	 * CopyOnWriteArraySet容器用于处理线程安全问题
	 * 用线程安全的list或 map不能处理线程存储安全的问题
	 * 因为即使list或map是线程安全
	 * 但list或map操作的数据有可能是脏的
	 */
	private static CopyOnWriteArraySet<WebSocketServer> websocketMap=new CopyOnWriteArraySet<>();

	@OnOpen
	public void onOpen(Session session,EndpointConfig config,@PathParam("id") String id) {
		online();
		this.id=id;
		this.session=session;
		this.httpSession=(HttpSession)config.getUserProperties().get("httpsession");
		User user=(User)httpSession.getAttribute("user");
		String string=user.getName()+"上线了! 当前连接人数:"+onlineCount;
		sendInfo(new WebsocketMsg(string,false));
		websocketMap.add(this);
		log.info("有用户加入连接,id:"+id+" 当前连接人数:"+onlineCount);		
	}

	@OnClose
	public void onClose() {
		User user=(User)httpSession.getAttribute("user");
		offline();
		websocketMap.remove(this);
		String string=user.getName()+"下线了 ! 当前连接人数:"+onlineCount;
		sendInfo(new WebsocketMsg(string,false));
		log.info("有一连接关闭! 当前在线人数:"+onlineCount);
	}

	@OnError
	public void onError(Throwable error) throws IOException {
		log.error("连接错误");
		error.printStackTrace();
	}

	public void sendInfo(String id,String sid,String message){
		if(id!=null) {
			WebSocketServer sender=getServer(sid);
			WebSocketServer receiver=getServer(id);
			if(receiver==null) {
				sender.sendMsg(new WebsocketMsg(400));
			}else {
				receiver.sendMsg(new WebsocketMsg(message));
				sender.sendMsg(new WebsocketMsg(200));
			}
		}else {
			log.info("id为空");
		}
	}
	
	private void sendMsg(WebsocketMsg websocketMsg) {
		if(this.session==null) {
			log.info("session对象为空");
			return;
		}
		this.session.getAsyncRemote().sendText(JSON.toJSONString(websocketMsg));
	}
	
	private WebSocketServer getServer(String id) {
		for (WebSocketServer webSocketServer : websocketMap) {
			if(id.equals(webSocketServer.getId())) {
				return webSocketServer;
			}
		}
		return null;
	}
	
	private void sendInfo(WebsocketMsg websocketMsg) {
		for (WebSocketServer webSocketServer : websocketMap) {
			webSocketServer.sendMsg(websocketMsg);
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public synchronized int getOnlineCount() {
		return onlineCount;
	}
	
	public synchronized void online() {
		onlineCount++;
	}
	
	public synchronized void offline() {
		--onlineCount;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
}
