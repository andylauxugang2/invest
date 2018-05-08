package com.invest.ivcommons.redis.client.cacheclient.redisPool;

import com.google.gson.Gson;
import com.invest.ivcommons.core.AppProfile;
import com.invest.ivcommons.redis.client.cacheclient.CacheClientConstant;
import com.invest.ivcommons.redis.client.cacheclient.CacheConfig;
import com.invest.ivcommons.redis.client.cacheclient.RedisConfig;
import com.invest.ivcommons.redis.client.cacheclient.StringByteCodec;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulConnection;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;

/**
 * ok--关于发布订阅,有可能因为replace或者updatepool的原因,导致整体pool销毁,订阅消失是有可能的,可以看下要不要提供一个事件,供他人订阅,可以在连接断开或者重连之后重新订阅
 * Created by yanjie on 2016/5/3.
 */
abstract public class RedisConnectionPoolBase {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    CacheConfig cacheConfig;
    private String masterAddress;
    private String password;
    private Gson gson = new Gson();
    private int timeOut;
    private RedisConfig singleConfig;
    StatefulRedisConnection<String, String> stringConnection;
    StatefulRedisConnection<String, byte[]> byteConnection;
    StatefulRedisPubSubConnection<String, byte[]> pubConnection;
    final Semaphore isStringConnectionSemaphore = new Semaphore(1, true);
    final Semaphore isByteConnectionSemaphore = new Semaphore(1, true);
    final Semaphore isPubConnectionSemaphore = new Semaphore(1, true);

    RedisClient client;

    public RedisClient getClient() {
        return client;
    }

    public void setClient(RedisClient client) {
        this.client = client;
    }

    public CacheConfig getCacheConfig() {
        return cacheConfig;
    }

    RedisConnectionPoolBase(CacheConfig cacheConfig) {
        if (cacheConfig.getRedisConfig() == null || cacheConfig.getRedisConfig().size() == 0) {
            RuntimeException ex = new RuntimeException("请提供至少一个有效的缓存信息");
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
        singleConfig = cacheConfig.getRedisConfig().stream().findFirst().orElse(null);
        if (singleConfig == null) {
            RuntimeException ex = new RuntimeException("请提供至少一个redis的配置信息!");
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
        masterAddress = singleConfig.getIp();
        password = singleConfig.getPassword();
        timeOut = singleConfig.getTimeOut();
        this.cacheConfig = cacheConfig;
    }

    public StatefulConnection<String, String> getStringConnection(boolean isRead) {
        if (stringConnection != null)
            return stringConnection;
        if (isStringConnectionSemaphore.tryAcquire()) {
            try {
                if (client == null)
                    return null;
                stringConnection = client.connect();
            } catch (Throwable throwable) {
                logger.error(throwable.getMessage(), throwable, gson.toJson(cacheConfig));
            } finally {
                isStringConnectionSemaphore.release();
            }
        }
        if (stringConnection != null)
            return stringConnection;
        else {
            return null;
        }
    }

    public StatefulConnection<String, byte[]> getByteConnection(boolean isRead) {
        if (byteConnection != null)
            return byteConnection;
        if (isByteConnectionSemaphore.tryAcquire()) {
            try {
                if (client == null)
                    return null;
                byteConnection = client.connect(new StringByteCodec());
            } catch (Throwable throwable) {
                logger.error(throwable.getMessage(), throwable, gson.toJson(cacheConfig));
            } finally {
                isByteConnectionSemaphore.release();
            }
        }
        if (byteConnection != null)
            return byteConnection;
        else {
            return null;
        }
    }

    public StatefulRedisPubSubConnection<String, byte[]> getPubConnection() {
        if (pubConnection != null)
            return pubConnection;
        if (isPubConnectionSemaphore.tryAcquire()) {
            try {
                if (client == null)
                    return null;
                pubConnection = client.connectPubSub(new StringByteCodec());
            } catch (Throwable throwable) {
                logger.error(throwable.getMessage(), throwable, gson.toJson(cacheConfig));
            } finally {
                isPubConnectionSemaphore.release();
            }
        }
        if (pubConnection != null)
            return pubConnection;
        else {
            return null;
        }
    }


    public StatefulRedisPubSubConnection<String, byte[]> getSubByteConnection() {
        try {
            if (client == null)
                return null;
            return client.connectPubSub(new StringByteCodec());
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public void dispose() {
        try {
            if (stringConnection != null)
                stringConnection.close();
            if (byteConnection != null)
                byteConnection.close();
            if (pubConnection != null)
                pubConnection.close();
            if (client != null)
                client.shutdown();
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
    }

//    public static String getClientName() {
//        return CacheClientConstant.CacheClientName;
//    }

    @SuppressWarnings("unchecked")
    public void setClientName() {
        try {
            if (byteConnection != null)
                byteConnection.sync().clientSetname(AppProfile.getAppName() + "_" + AppProfile.getEnvironment() + "_" + CacheClientConstant.version);
            if (stringConnection != null)
                stringConnection.sync().clientSetname(AppProfile.getAppName() + "_" + AppProfile.getEnvironment() + "_" + CacheClientConstant.version);
            if (pubConnection != null)
                pubConnection.sync().clientSetname(AppProfile.getAppName() + "_" + AppProfile.getEnvironment() + "_" + CacheClientConstant.version);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
    }
}
