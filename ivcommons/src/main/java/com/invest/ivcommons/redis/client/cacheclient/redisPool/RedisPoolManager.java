package com.invest.ivcommons.redis.client.cacheclient.redisPool;

import com.invest.ivcommons.redis.client.cacheclient.CacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ok
 * Created by yanjie on 2016/5/3.
 */
public class RedisPoolManager {
    protected static Logger logger = LoggerFactory.getLogger(RedisPoolManager.class);

    private static ConcurrentHashMap<String, RedisConnectionPoolBase> poolDic =
            new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, RedisConnectionPoolBase> offlinePoolDic =
            new ConcurrentHashMap<>();

    private static ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    static {
        scheduledExecutor.schedule(RedisPoolManager::updateName, 5, TimeUnit.SECONDS);
    }

    public static RedisConnectionPoolBase getPool(String name) {
        RedisConnectionPoolBase pool = poolDic.getOrDefault(name, null);
        if (pool != null)
            return pool;
        pool = offlinePoolDic.getOrDefault(name, null);
        if (pool != null)
            return pool;
        return null;
    }


    private static void updateName() {
        try {
            for (String key : poolDic.keySet()) {
                RedisConnectionPoolBase pool = poolDic.getOrDefault(key, null);
                if (pool != null)
                    pool.setClientName();
            }
            for (String key : offlinePoolDic.keySet()) {
                RedisConnectionPoolBase pool = offlinePoolDic.getOrDefault(key, null);
                if (pool != null)
                    pool.setClientName();
            }
        } catch (Throwable ignored) {

        }
    }


    public static void updatePool(CacheConfig config) {
        RedisConnectionPoolBase pool = null;
        switch (config.getType()) {
            case S:
                pool = new SingleRedisConnectionPool(config);
                break;
            case M:
                pool = new MasterSlaveConnectionPool(config);
                break;
            case C:
                pool = new ClusterConnectionPool(config);
                break;
            case P:
                pool = new SingleRedisConnectionPool(config);
                break;
//            case R:
//                pool = new ReadWriteConnectionPool(config);
//                break;
        }
        if (pool == null)
            return;
        RedisConnectionPoolBase oldPool = poolDic.put(config.getName(), pool);
        if (oldPool != null) {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    oldPool.dispose();
                } catch (Throwable throwable) {
                    logger.error(throwable.getMessage(), throwable);
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    public static void replacePool(CacheConfig[] configList) {
        for (CacheConfig config : configList) {
            updatePool(config);
        }
        //更新已存在的配置信息,并将删除的配置的pool清理掉
        Enumeration<String> keys = poolDic.keys();
        List<RedisConnectionPoolBase> removeList = new ArrayList<>();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (Arrays.stream(configList).filter(p -> p.getName().equals(key)).count() == 0) {
                RedisConnectionPoolBase pool = poolDic.get(key);
                if (pool != null && pool.cacheConfig.isOffline())
                    continue;
                removeList.add(poolDic.remove(key));
            }
        }
        if (removeList.size() > 0) {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(10000);
                } catch (Throwable throwable) {
                    logger.error(throwable.getMessage(), throwable);
                }
                for (RedisConnectionPoolBase removepool : removeList) {
                    try {
                        removepool.dispose();
                    } catch (Throwable throwable) {
                        logger.error(throwable.getMessage(), throwable);
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    public static void updateOfflinePool(CacheConfig config) {
        RedisConnectionPoolBase pool = null;
        switch (config.getType()) {
            case S:
                pool = new SingleRedisConnectionPool(config);
                break;
            case M:
                pool = new MasterSlaveConnectionPool(config);
                break;
            case C:
                pool = new ClusterConnectionPool(config);
                break;
            case P:
                pool = new SingleRedisConnectionPool(config);
                break;
//            case R:
//                pool = new ReadWriteConnectionPool(config);
//                break;
        }
        if (pool == null)
            return;
        RedisConnectionPoolBase oldPool = offlinePoolDic.put(config.getName(), pool);
        if (oldPool != null) {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    oldPool.dispose();
                } catch (Throwable throwable) {
                    logger.error(throwable.getMessage(), throwable);
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }
}
