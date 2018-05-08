package com.invest.ivcommons.redis.client.cacheclient;

import com.invest.ivcommons.core.AppProfile;
import com.invest.ivcommons.redis.client.cacheclient.command.*;
import com.invest.ivcommons.redis.client.cacheclient.redisPool.RedisPoolManager;
import org.apache.commons.lang3.StringUtils;

/**
 * ok
 * Created by yanjie on 2016/5/3.
 */
public class CacheClientHA {
    private String cacheName;
    private KeyCommand keyCommand;
    private StringsCommand stringsCommand;
    private HashCommand hashCommand;
    private SetCommand setCommand;
    private ListCommand listCommand;
    private HyperLogLogCommand hyperLogLogCommand;
    private PubSubCommand pubSubCommand;

    /// <summary>
    /// /// 提供Key操作的相关命令
    /// </summary>
    public KeyCommand Key() {
        return keyCommand;
    }

    public StringsCommand String() {
        return stringsCommand;
    }

    public HashCommand Hash() {
        return hashCommand;
    }

    /// <summary>
    /// /// 提供Set操作的相关命令
    /// </summary>
    public SetCommand Set() {
        return setCommand;
    }

    public ListCommand List() {
        return listCommand;
    }

    public HyperLogLogCommand HyperLogLog() {
        return hyperLogLogCommand;
    }

    public PubSubCommand pubsub() {
        return pubSubCommand;
    }

    static {
        RedisSource.buildPool();
    }


    public CacheClientHA(String cacheName, boolean needThrowException) {
        if (!StringUtils.isBlank(cacheName))
            this.cacheName = cacheName;
        else
            throw new NullPointerException("cacheName");
        keyCommand = new KeyCommand(cacheName, needThrowException);
        stringsCommand = new StringsCommand(cacheName, needThrowException);
        hashCommand = new HashCommand(cacheName, needThrowException);
        setCommand = new SetCommand(cacheName, needThrowException);
        listCommand = new ListCommand(cacheName, needThrowException);
        hyperLogLogCommand = new HyperLogLogCommand(cacheName, needThrowException);
        pubSubCommand = new PubSubCommand(cacheName, needThrowException);
    }

    //不要使用xml配置 单机模式
    public CacheClientHA(String address, int port, String password, int timeout, boolean needThrowException) {
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setEnabled(true);
        cacheConfig.setName(AppProfile.getAppName());
        this.cacheName = cacheConfig.getName();
        cacheConfig.setOffline(true);
        cacheConfig.setType(CacheGroupType.S);
        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setEnabled(true);
        redisConfig.setIp(address + ":" + port);
        redisConfig.setMaxPool(25);
        redisConfig.setMinPool(5);
        redisConfig.setPassword(password);
        redisConfig.setTimeOut(timeout);
        cacheConfig.getRedisConfig().add(redisConfig);
        RedisPoolManager.updatePool(cacheConfig);
        keyCommand = new KeyCommand(cacheName, needThrowException);
        stringsCommand = new StringsCommand(cacheName, needThrowException);
        hashCommand = new HashCommand(cacheName, needThrowException);
        setCommand = new SetCommand(cacheName, needThrowException);
        listCommand = new ListCommand(cacheName, needThrowException);
        hyperLogLogCommand = new HyperLogLogCommand(cacheName, needThrowException);
        pubSubCommand = new PubSubCommand(cacheName, needThrowException);
    }
}
