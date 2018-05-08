package com.invest.ivuser.mq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例
 * Producer对象在使用之前必须要调用start初始化，初始化一次即可 不可以在每次发送消息时，都调用start方法
 * Created by xugang on 2017/8/8.
 */
public class MyDefaultMQProducer extends DefaultMQProducer {
    private static final Logger logger = LoggerFactory.getLogger(MyDefaultMQProducer.class);

    @Override
    public void start() throws MQClientException {
        super.start();
        logger.info("BaseMQProducer Start OK!");
    }

    @Override
    public void shutdown() {
        super.shutdown();
        logger.info("BaseMQProducer Shutdown OK!");
    }
}
