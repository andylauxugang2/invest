package com.zhuobao.ivbid.biz.manager;

import com.invest.ivcommons.redis.client.cacheclient.CacheClientHA;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by xugang on 2017/9/20.do best.
 */
@Component
public class DistributedLockManager {
    @Resource(name = "ppdCacheClientHA")
    protected CacheClientHA ppdCacheClientHA;

    private static final String LOCK_PREFIX = "DistributedLock-";

    /**
     * 获取请求默认分布式锁,超时1分钟
     *
     * @param bizKey 业务key 如listingId
     * @param lockExpireInSeconds 过期时间 单位秒
     * @return true-成功，false-失败
     */
    public boolean acquireDistributedLock(String bizKey, int lockExpireInSeconds) {
        return redisSetNX(distributedLockKey(bizKey), "1", lockExpireInSeconds);
    }

    /**
     * 请求分布式锁
     */
    private static String distributedLockKey(String bizKey) {
        return LOCK_PREFIX + bizKey;
    }

    private boolean redisSetNX(String key, String value, long expireInSeconds) {
        return ppdCacheClientHA.String().setnx(key, expireInSeconds, value);
    }
}
