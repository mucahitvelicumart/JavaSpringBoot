package com.example.demo.user.sessionlistener;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionCounter implements HttpSessionListener{
	public static final String COUNTER ="SESSION-COUNTER";
	private final List<String> sessions = new ArrayList<>();
	
	
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		sessions.add(session.getId());
		session.setAttribute(SessionCounter.COUNTER, this);
		System.out.println("Online Users: " + getActiveSessionNumber());
	}
	
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		sessions.remove(session.getId());
		session.setAttribute(SessionCounter.COUNTER, this);
		System.out.println("Online Users: " + getActiveSessionNumber());
	}
	
	public int getActiveSessionNumber() {
		return sessions.size();
	}
	
	
	
	

}
