package com.invest.ivcommons.session;


public interface SessionRepository <T extends Session>{
	
	T createSession();
	
   	void save(T session);

	T getSession(String id);

	void delete(String id);

}
