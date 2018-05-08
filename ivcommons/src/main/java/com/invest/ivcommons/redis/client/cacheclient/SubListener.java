package com.invest.ivcommons.redis.client.cacheclient;

import com.lambdaworks.redis.pubsub.RedisPubSubListener;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by admin on 2016/9/12.
 *
 */
//ok-xyj
public class SubListener implements RedisPubSubListener<String,byte[]> {

   private List<MessageListener> listeners = new ArrayList<>();

    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MessageListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void message(String s, byte[] bytes) {
        for (MessageListener listener : listeners)
            listener.message(s, bytes);
    }

    @Override
    public void message(String s, String k1, byte[] bytes) {

    }

    @Override
    public void subscribed(String s, long l) {

    }

    @Override
    public void psubscribed(String s, long l) {

    }

    @Override
    public void unsubscribed(String s, long l) {

    }

    @Override
    public void punsubscribed(String s, long l) {

    }
}
