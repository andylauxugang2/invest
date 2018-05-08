package com.invest.ivcommons.redis.client.cacheclient;

/**
 * Created by admin on 2016/9/7.
 */
@FunctionalInterface
public interface MessageListener {
    void message(String channel, byte[] value);
}
