package com.invest.ivcommons.session.http;

import com.invest.ivcommons.session.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xugang on 2017/7/29.
 */
public interface HttpStrategy {
	
	String getRequestedSessionId(HttpServletRequest request);
	
	void createSession(Session session, HttpServletRequest request, HttpServletResponse response);

	void invalidateSession(HttpServletRequest request, HttpServletResponse response);

}
