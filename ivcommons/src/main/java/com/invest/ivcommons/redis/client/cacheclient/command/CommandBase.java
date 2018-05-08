package com.invest.ivcommons.redis.client.cacheclient.command;

import com.invest.ivcommons.core.AppProfile;
import com.invest.ivcommons.redis.client.cacheclient.metric.MetricManager;
import com.invest.ivcommons.redis.client.cacheclient.redisPool.RedisConnectionPoolBase;
import com.invest.ivcommons.redis.client.cacheclient.redisPool.RedisPoolManager;
import com.lambdaworks.redis.api.StatefulConnection;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.BaseRedisCommands;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import com.lambdaworks.redis.pubsub.api.sync.RedisPubSubCommands;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Function;

/**
 * Created by yanjie on 2016/5/3.
 */
public class CommandBase {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected boolean needThrowException;
    protected String cacheName;

    CommandBase(String cacheName, boolean needThrowException) {
        this.cacheName = cacheName;
        this.needThrowException = needThrowException;
        RedisConnectionPoolBase pool = RedisPoolManager.getPool(cacheName);
        if (pool == null)
            throw new RuntimeException("无法找到缓存配置项" + cacheName + ",请检查配置信息!当前的项目名称是:" + AppProfile.getAppName() + ",当前的环境:" + AppProfile.getEnvironment());
    }

    protected <T> T invokeStringCommand(String cmdName, String key, boolean isRead, Function<BaseRedisCommands<String, String>, T> invoke) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key");
        }
        long start = System.currentTimeMillis();
        Throwable exp = null;
        try {
            RedisConnectionPoolBase pool = RedisPoolManager.getPool(cacheName);
            if (pool == null)
                throw new RuntimeException("无法找到缓存配置项" + cacheName + ",请检查配置信息!当前的项目名称是:" + AppProfile.getAppName() + ",当前的环境:" + AppProfile.getEnvironment());
            StatefulConnection<String, String> conn = pool.getStringConnection(isRead);
            if (conn == null || !conn.isOpen()) {
                throw new RuntimeException("缓存:" + pool.getCacheConfig().getName() + "连接中断, 请检查当前配置信息或网络是否正常!当前的项目名称是:" + AppProfile.getAppName() + ",当前的环境:" + AppProfile.getEnvironment());
            }
            BaseRedisCommands<String, String> cmd;
            if (conn instanceof StatefulRedisConnection) {
                cmd = ((StatefulRedisConnection<String, String>) conn).sync();
            } else {
                cmd = ((StatefulRedisClusterConnection<String, String>) conn).sync();
            }
            return invoke.apply(cmd);
        } catch (Throwable ex) {
            exp = ex;
            logger.error(ex.getMessage(), ex);
            if (needThrowException)
                throw ex;
            else
                return null;
        } finally {
            long end = System.currentTimeMillis();
            MetricManager.addKey(key);
            if (exp == null)
                MetricManager.addSuccess();
            else {
                MetricManager.addError();
                MetricManager.addException(cmdName, key, exp);
            }
            long time = end - start;
            if (time > 100)
                MetricManager.addslowCommand(cmdName, key, time);
        }
    }

    <T> T invokeByteCommand(String cmdName, String key, boolean isRead, Function<BaseRedisCommands<String, byte[]>, T> invoke) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key");
        }
        long start = System.currentTimeMillis();
        Throwable exp = null;
        try {
            RedisConnectionPoolBase pool = RedisPoolManager.getPool(cacheName);
            if (pool == null)
                throw new RuntimeException("无法找到缓存配置项" + cacheName + ",请检查配置信息!当前的项目名称是:" + AppProfile.getAppName() + ",当前的环境:" + AppProfile.getEnvironment());
            StatefulConnection<String, byte[]> conn = pool.getByteConnection(isRead);
            if (conn == null || !conn.isOpen()) {
                throw new RuntimeException("缓存:" + pool.getCacheConfig().getName() + "连接中断, 请检查当前配置信息或网络是否正常!当前的项目名称是:" + AppProfile.getAppName() + ",当前的环境:" + AppProfile.getEnvironment());
            }
            BaseRedisCommands<String, byte[]> cmd;
            if (conn instanceof StatefulRedisConnection) {
                cmd = ((StatefulRedisConnection<String, byte[]>) conn).sync();
            } else {
                cmd = ((StatefulRedisClusterConnection<String, byte[]>) conn).sync();
            }
            return invoke.apply(cmd);
        } catch (Throwable ex) {
            exp = ex;
            logger.error(ex.getMessage(), ex);
            if (needThrowException)
                throw ex;
            else
                return null;
        } finally {
            long end = System.currentTimeMillis();
            MetricManager.addKey(key);
            if (exp == null)
                MetricManager.addSuccess();
            else {
                MetricManager.addError();
                MetricManager.addException(cmdName, key, exp);
            }
            long time = end - start;
            if (time > 100)
                MetricManager.addslowCommand(cmdName, key, time);
        }
    }

    Long invokePubCommand(Function<RedisPubSubCommands<String, byte[]>, Long> invoke) {
        try {
            RedisConnectionPoolBase pool = RedisPoolManager.getPool(cacheName);
            if (pool == null)
                throw new RuntimeException("无法找到缓存配置项" + cacheName + ",请检查配置信息!当前的项目名称是:" + AppProfile.getAppName() + ",当前的环境:" + AppProfile.getEnvironment());
            StatefulRedisPubSubConnection<String, byte[]> pubConnection = pool.getPubConnection();
            if (pubConnection == null || !pubConnection.isOpen()) {
                throw new RuntimeException("缓存:" + pool.getCacheConfig().getName() + "连接中断 请检查当前配置信息或网络是否正常!当前的项目名称是:" + AppProfile.getAppName() + ",当前的环境:" + AppProfile.getEnvironment());
            }
            RedisPubSubCommands<String, byte[]> cmd = pubConnection.sync();
            return invoke.apply(cmd);
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
            if (needThrowException)
                throw ex;
            return 0L;
        }
    }

    void checkStringArray(String name, String[] array) {
        if (array == null || array.length == 0) {
            throw new RuntimeException(name + " can not is null or empty");
        }
        for (int i = 0; i < array.length; i++) {
            if (StringUtils.isEmpty(array[i]))
                throw new RuntimeException(name + " can not contains null or empty,index:" + i);
        }
    }

    void checkStringMap(Map<String, String> map) {
        int index = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (StringUtils.isEmpty(entry.getKey())) {
                throw new RuntimeException("key can not is null or empty,index:" + index);
            }
            if (StringUtils.isEmpty(entry.getValue())) {
                throw new RuntimeException("value can not is null or empty,key:" + entry.getKey());
            }
        }
    }

    void checkByteMap(Map<String, byte[]> map) {
        int index = 0;
        for (Map.Entry<String, byte[]> entry : map.entrySet()) {
            if (StringUtils.isEmpty(entry.getKey())) {
                throw new RuntimeException("key can not is null or empty,index:" + index);
            }
            if (entry.getValue() == null || entry.getValue().length == 0) {
                throw new RuntimeException("value can not is null or empty,key:" + entry.getKey());
            }
        }
    }

    void checkByteArray(String name, byte[][] array) {
        if (array == null || array.length == 0) {
            throw new RuntimeException(name + " can not is null");
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null)
                throw new RuntimeException(name + " can not contains null ,index:" + i);
        }
    }

    protected void checkString(String name, String item) {
        if (StringUtils.isEmpty(item)) {
            throw new RuntimeException(name + " can not is null or empty");
        }
    }

    void checkByte(String name, byte[] item) {
        if (item == null || item.length == 0) {
            throw new RuntimeException(name + " can not is null or empty");
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T CastCommand(BaseRedisCommands baseRedisCommands) {
        return (T) baseRedisCommands;
    }
}
