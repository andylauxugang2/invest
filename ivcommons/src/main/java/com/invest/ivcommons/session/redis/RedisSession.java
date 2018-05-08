package com.invest.ivcommons.session.redis;

import com.invest.ivcommons.session.ExpiredSession;
import com.invest.ivcommons.session.Session;
import com.invest.ivcommons.session.utils.HttpConstants;
import com.invest.ivcommons.session.utils.HttpStrategyUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by xugang on 2017/7/29.
 */
@SuppressWarnings("unchecked")
public class RedisSession implements ExpiredSession {

    private String id;
    private Map<String, Object> sessionAttrs = new HashMap<>();
    private long creationTime = System.currentTimeMillis();
    private long lastAccessedTime = creationTime;
    private int expiredTime;
    private long originalLastAccessTime;
    private Map<String, Object> changeAttrs = new HashMap<>();
    private boolean isFirst = false;
    private boolean delete = false;

    public RedisSession() {
        this(UUID.randomUUID().toString().replace("-", ""));
    }

    public RedisSession(String id) {
        this.id = id;
    }


    @Override
    public String getId() {

        return id;
    }

    @Override
    public <T> T getAttribute(String attributeName) {

        return (T) sessionAttrs.get(attributeName);
    }

    @Override
    public Set<String> getAttributeNames() {

        return sessionAttrs.keySet();
    }

    @Override
    public void setAttribute(String attributeName, Object attributeValue) {

        sessionAttrs.put(attributeName, attributeValue);
        changeAttrs.put(HttpConstants.SESSION_ATTR_PREFIX + attributeName, attributeValue);

    }

    @Override
    public void removeAttribute(String attributeName) {

        sessionAttrs.remove(attributeName);
        changeAttrs.remove(HttpConstants.SESSION_ATTR_PREFIX + attributeName);

    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public void setExpiredSeconds(int expiredTime) {
        this.expiredTime = expiredTime;

    }

    @Override
    public int getExpiredSeconds() {
        return expiredTime;
    }

    @Override
    public boolean isExpired() {
        if (expiredTime < 0) {
            return false;
        }
        return (TimeUnit.SECONDS.toMillis(expiredTime) + lastAccessedTime) <= System
                .currentTimeMillis();
    }

    @Override
    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }


    public boolean isFirst() {
        return isFirst;
    }


    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }


    public boolean equals(Object obj) {

        return (obj instanceof Session) && id.equals(((Session) obj).getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    public Map<String, Object> getChangeAttrs() {
        return changeAttrs;
    }


    public void setChangeAttrs(Map<String, Object> changeAttrs) {
        this.changeAttrs = changeAttrs;
    }


    public long getOriginalLastAccessTime() {
        return originalLastAccessTime;
    }


    public void setOriginalLastAccessTime(long originalLastAccessTime) {
        this.originalLastAccessTime = originalLastAccessTime;
    }

    public void setSessionId(String sessionId) {
        this.id = sessionId;
    }


    public void handlClientStore(HttpServletResponse response) {

        if (HttpConstants.FILTER_COOKIE.equals(getAttribute(HttpConstants.HEADER__FILTER_KEY))) {
            HttpStrategyUtil.cookieStrategy.createSession(this, null, response);
        }
        if (HttpConstants.FILTER_HEADER.equals(getAttribute(HttpConstants.HEADER__FILTER_KEY))) {
            HttpStrategyUtil.headerStrategy.createSession(this, null, response);
        }

    }

    public void cleanClientStore() {
        removeAttribute(HttpConstants.HEADER__FILTER_KEY);
    }

    public void addBasicAttrs() {
        changeAttrs.put(RedisSessionRepository.CREATE_TIME, getCreationTime());
        changeAttrs.put(RedisSessionRepository.LAST_ACCESSED_TIME, getLastAccessedTime());
        changeAttrs.put(RedisSessionRepository.EXPIRED_TIME, getExpiredSeconds());
    }


}
