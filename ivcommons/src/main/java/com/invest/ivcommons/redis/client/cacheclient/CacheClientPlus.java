package com.invest.ivcommons.redis.client.cacheclient;

import com.invest.ivcommons.redis.client.cacheclient.command.*;
import com.invest.ivcommons.redis.client.cacheclient.redisPool.RedisPoolManager;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**ok
 * Created by yanjie on 2016/5/3.
 *
 */
public class CacheClientPlus {
    private String cacheName;
    private KeyCommandPlus keyCommand;
    private StringsCommandPlus stringsCommand;
    private HashCommand hashCommand;
    private SetCommandPlus setCommand;
    private ListCommandPlus listCommand;
    private HyperLogLogCommand hyperLogLogCommand;
    private PubSubCommand pubSubCommand;

    public KeyCommandPlus key()
    {
        return keyCommand;
    }

    public StringsCommandPlus string()
    {
        return stringsCommand;
    }

    public HashCommand hash()
    {
        return hashCommand;
    }
    /// <summary>
    /// /// 提供Set操作的相关命令
    /// </summary>
    public SetCommandPlus set()
    {
        return setCommand;
    }

    public ListCommandPlus list() {
        return listCommand;
    }

    public HyperLogLogCommand hyperLogLog() {
        return hyperLogLogCommand;
    }

    public PubSubCommand pubsub()
    {
        return pubSubCommand;
    }

    static
    {
        RedisSource.buildPool();
    }


    public CacheClientPlus(String cacheName, boolean needThrowException) {
        if (!StringUtils.isBlank(cacheName))
            this.cacheName = cacheName;
        else
            throw new NullPointerException("cacheName");
        keyCommand = new KeyCommandPlus(cacheName, needThrowException);
        stringsCommand = new StringsCommandPlus(cacheName, needThrowException);
        hashCommand = new HashCommand(cacheName, needThrowException);
        setCommand = new SetCommandPlus(cacheName, needThrowException);
        listCommand = new ListCommandPlus(cacheName, needThrowException);
        hyperLogLogCommand=new HyperLogLogCommand(cacheName,needThrowException);
        pubSubCommand=new PubSubCommand(cacheName,needThrowException);
    }

    public CacheClientPlus(String address, int port, String password, int timeout,boolean needThrowException)
    {
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setEnabled(true);
        cacheConfig.setName("TCBase.LocalAddress"+ UUID.randomUUID().toString());
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
        keyCommand = new KeyCommandPlus(cacheName, needThrowException);
        stringsCommand = new StringsCommandPlus(cacheName, needThrowException);
        hashCommand = new HashCommand(cacheName, needThrowException);
        setCommand = new SetCommandPlus(cacheName, needThrowException);
        listCommand = new ListCommandPlus(cacheName, needThrowException);
        hyperLogLogCommand=new HyperLogLogCommand(cacheName,needThrowException);
        pubSubCommand=new PubSubCommand(cacheName,needThrowException);
    }
}
