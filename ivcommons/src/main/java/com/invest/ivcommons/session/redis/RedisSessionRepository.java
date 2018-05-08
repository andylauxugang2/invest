package com.invest.ivcommons.session.redis;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.redis.client.cacheclient.CacheClientHA;
import com.invest.ivcommons.session.SessionRepository;
import com.invest.ivcommons.session.utils.HttpConstants;
import com.invest.ivcommons.util.serialize.HessianUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xugang on 2017/7/29.
 */
public class RedisSessionRepository implements SessionRepository<RedisSession> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CacheClientHA cacheClientHA;

    static final String DEFAULT_SESSION_REDIS_PREFIX = "iv:session:";

    static final String CREATE_TIME = "createTime";

    static final String EXPIRED_TIME = "expiredTime";

    static final String LAST_ACCESSED_TIME = "lastAccessedTime";

    private String appName;

    private Integer maxIntervalTime = 1800;

    public RedisSessionRepository(CacheClientHA cacheClientHA) {
        this.cacheClientHA = cacheClientHA;
    }

    @Override
    public RedisSession createSession() {

        RedisSession session = new RedisSession();
        session.setFirst(true);
        session.setExpiredSeconds(maxIntervalTime);
        return session;
    }

    @Override
    public void save(RedisSession redisSession) {

        flush(redisSession);
        redisSession.setFirst(false);

    }

    @Override
    public RedisSession getSession(String sessionId) {
        return getSession(sessionId, true);
    }

    public RedisSession getSession(String sessionId, boolean isExpired) {
        String sessionKey = getSessionKey(sessionId);
        byte[] sessionMapBit = cacheClientHA.String().getBit(sessionKey);
        if(sessionMapBit == null){
            return null;
        }
        Map<String, Object> sessionMap;
        try {
            sessionMap = HessianUtil.fromBytes(sessionMapBit);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if (sessionMap == null || sessionMap.entrySet().isEmpty()) return null;
        RedisSession redisSession = loadRedisSession(sessionId, sessionMap);
        if (isExpired && redisSession.isExpired()) return null;
        redisSession.setOriginalLastAccessTime(redisSession.getLastAccessedTime());
        return redisSession;
    }

    @Override
    public void delete(String sessionId) {
        cacheClientHA.Key().del(DEFAULT_SESSION_REDIS_PREFIX + sessionId);

    }

    private RedisSession loadRedisSession(String sessionId, Map<String, Object> sessionMap) {

        RedisSession redisSession = new RedisSession(sessionId);
        for (String key : sessionMap.keySet()) {
            if (CREATE_TIME.equals(key)) {
                redisSession.setCreationTime((Long) (sessionMap.get(key)));
            } else if (EXPIRED_TIME.equals(key)) {
                redisSession.setExpiredSeconds((Integer) (sessionMap.get(key)));
            } else if (LAST_ACCESSED_TIME.equals(key)) {
                redisSession.setLastAccessedTime((Long) (sessionMap.get(key)));
            } else if (StringUtils.isNotBlank(key) && key.startsWith(HttpConstants.SESSION_ATTR_PREFIX)) {
                redisSession.setAttribute(key.substring(HttpConstants.SESSION_ATTR_PREFIX.length()),
                        sessionMap.get(key));
            }
        }
        redisSession.getChangeAttrs().clear();
        return redisSession;
    }


    private void flush(RedisSession redisSession) {

        Map<String, Object> attrs = redisSession.getChangeAttrs();
        try {
            cacheClientHA.String().setexBit(getSessionKey(redisSession.getId()), maxIntervalTime + 10 * 60, HessianUtil.toBytes(attrs));
            logger.info("刷新RedisSession成功,key={},value={}", getSessionKey(redisSession.getId()), JSONObject.toJSONString(attrs));
        } catch (IOException e) {
            e.printStackTrace();
        }

        redisSession.setChangeAttrs(new HashMap<>());
    }

    private String getSessionKey(String sessionId) {
        String sessionKey = DEFAULT_SESSION_REDIS_PREFIX;
        if (StringUtils.isNotBlank(appName)) sessionKey += appName;
        return (sessionKey + sessionId);
    }


    public Integer getMaxIntervalTime() {
        return maxIntervalTime;
    }

    public void setMaxIntervalTime(Integer maxIntervalTime) {
        this.maxIntervalTime = maxIntervalTime;
    }

}
