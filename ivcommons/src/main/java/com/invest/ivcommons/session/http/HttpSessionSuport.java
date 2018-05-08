package com.invest.ivcommons.session.http;

/**
 * Created by xugang on 2017/7/29.
 */

import com.invest.ivcommons.session.error.ParamError;
import com.invest.ivcommons.session.redis.RedisSession;
import com.invest.ivcommons.session.utils.ErrorMessageConstants;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

public class HttpSessionSuport {


    private static ThreadLocal<RedisSession> threadLocal = new ThreadLocal<>();


    public static void set(RedisSession redisSession) {

        threadLocal.set(redisSession);
    }

    public static RedisSession get() {

        return threadLocal.get();
    }

    public static void clear() {

        threadLocal.set(null);
    }

    public static void reCreate(String sessionID) {
        RedisSession redisSession = get();
        if (redisSession != null && !redisSession.isFirst()) {
            redisSession.setSessionId(sessionID);
            redisSession.setFirst(true);
            Set<String> attributeNames = redisSession.getAttributeNames();
            for (String attr : attributeNames) {
                redisSession.setAttribute(attr, redisSession.getAttribute(attr));
            }
        }
    }

    public static void setAttribute(String attributeName, Object attributeValue) {
        if (StringUtils.isBlank(attributeName)
                || attributeValue == null
                ) {
            throw new ParamError(ErrorMessageConstants.PARRAM_ERROR);
        }
        RedisSession redisSession = get();
        redisSession.setAttribute(attributeName, attributeValue);
    }

    public static void removeAttribute(String attributeName) {
        if (StringUtils.isBlank(attributeName)
                ) {
            throw new ParamError(ErrorMessageConstants.PARRAM_ERROR);
        }
        RedisSession redisSession = get();
        redisSession.removeAttribute(attributeName);
    }

    public static <T> T getAttribute(String attributeName) {
        if (StringUtils.isBlank(attributeName)) {
            throw new ParamError(ErrorMessageConstants.PARRAM_ERROR);
        }
        RedisSession redisSession = get();
        //threadLocal跨线程返回为null
        if (redisSession == null) {
            return null;
        }
        return (T) redisSession.getAttribute(attributeName);
    }

    public static void setSessionId(String sessionId, HttpServletResponse response) {
        RedisSession redisSession = get();
        redisSession.setSessionId(sessionId);
        redisSession.handlClientStore(response);
    }

    public static void delSession() {
        RedisSession redisSession = get();
        redisSession.setDelete(true);
    }

}

