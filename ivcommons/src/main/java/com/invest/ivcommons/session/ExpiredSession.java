package com.invest.ivcommons.session;


public interface ExpiredSession extends Session{

	long getCreationTime();
	
	void setCreationTime(long creationTime);

	void setLastAccessedTime(long lastAccessedTime);

	long getLastAccessedTime();
	
	void setExpiredSeconds(int expiredTime);

	int getExpiredSeconds();

	boolean isExpired();

}
