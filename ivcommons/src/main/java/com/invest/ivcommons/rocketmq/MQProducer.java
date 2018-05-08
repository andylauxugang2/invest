package com.invest.ivcommons.rocketmq;

/**
 * Created by xugang on 2017/8/8.
 */
public interface MQProducer<T extends BaseMQMessage> {
    /**
     * 发送消息
     *
     * @param msg
     */
    boolean sendMsg(T msg);
}
