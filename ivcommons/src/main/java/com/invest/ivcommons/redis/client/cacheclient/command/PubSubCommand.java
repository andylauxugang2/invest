package com.invest.ivcommons.redis.client.cacheclient.command;

import com.invest.ivcommons.core.AppProfile;
import com.invest.ivcommons.redis.client.cacheclient.CacheClientConstant;
import com.invest.ivcommons.redis.client.cacheclient.MessageListener;
import com.invest.ivcommons.redis.client.cacheclient.SubListener;
import com.invest.ivcommons.redis.client.cacheclient.redisPool.RedisConnectionPoolBase;
import com.invest.ivcommons.redis.client.cacheclient.redisPool.RedisPoolManager;
import com.lambdaworks.io.netty.util.internal.ConcurrentSet;
import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import com.lambdaworks.redis.pubsub.api.sync.RedisPubSubCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by admin on 2016/9/6.
 */
public class PubSubCommand extends CommandBase {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    SubListener subListener = new SubListener();
    Set<String> channelSet = new HashSet<>();
    static ExecutorService executorService;
    static ConcurrentSet<PubSubCommand> commandList = new ConcurrentSet<>();

    RedisPubSubCommands<String, byte[]> subCommand;

    public PubSubCommand(String cacheName, boolean needThrowException) {

        super(cacheName, needThrowException);
        commandList.add(this);
    }

    static {
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(PubSubCommand::staticCheck);
    }

    private static void staticCheck() {
        while (true) {
            for (PubSubCommand command : commandList)
                try {
                    command.check();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void check() {
        if (channelSet.size() == 0)
            return;
        if (subCommand != null && subCommand.isOpen())
            return;
        buildStringSubCommand();
    }


    private synchronized void buildStringSubCommand() {
        if (subCommand != null && subCommand.isOpen()) {
            return;
        }
        try {
            RedisConnectionPoolBase pool = RedisPoolManager.getPool(cacheName);
            if (pool == null)
                throw new RuntimeException("can not find cache " + cacheName + ",please check config info!");
            StatefulRedisPubSubConnection<String, byte[]> conn = pool.getSubByteConnection();
            subCommand = conn.sync();
            subCommand.clientSetname(AppProfile.getAppName() + "_" + AppProfile.getEnvironment() + "_" + CacheClientConstant.version);
            if (channelSet.size() > 0) {
                subCommand.subscribe(channelSet.toArray(new String[channelSet.size()]));
            }
            subCommand.addListener(subListener);
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
            if (needThrowException)
                throw ex;
        }
    }

    private RedisPubSubCommands<String, byte[]> getSubCommand() {
        buildStringSubCommand();
        return subCommand;
    }

    public void subscribe(String... channel) {
        checkStringArray("channel", channel);
        getSubCommand().subscribe(channel);
    }

    public void unsubscribe(String... channel) {
        checkStringArray("channel", channel);
        getSubCommand().unsubscribe(channel);
    }


    public void addListener(MessageListener listener) {
        subListener.addListener(listener);
    }

    public void removeListener(MessageListener listener) {
        subListener.removeListener(listener);
    }

    public Long publish(String channel, String message) {
        checkString("message", message);
        return this.publish(channel, message.getBytes());
    }

    public Long publish(String channel, byte[] message) {
        checkString("channel", channel);
        checkByte("message", message);
        return this.invokePubCommand(cmd -> cmd.publish(channel, message));
    }


    /**
     * PSUBSCRIBE
     * 订阅一个或多个符合给定模式的频道
     * 每个模式以 * 作为匹配符，比如 it* 匹配所有以 it 开头的频道( it.news 、 it.blog 、 it.tweets 等等)， news.* 匹配所有以 news. 开头的频道( news.it 、 news.global.today 等等)，诸如此类。
     * 时间复杂度：O(1)
     */
    public void psubscribe(String... pattern) {
        checkStringArray("pattern", pattern);
        getSubCommand().psubscribe(pattern);
    }

    public void punsubscribe(String... pattern) {
        checkStringArray("pattern", pattern);
        getSubCommand().punsubscribe(pattern);
    }
}
