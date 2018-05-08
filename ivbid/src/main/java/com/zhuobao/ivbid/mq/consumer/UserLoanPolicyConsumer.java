package com.zhuobao.ivbid.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.constant.SystemConstant;
import com.invest.ivcommons.rocketmq.model.UserLoanPolicyMessage;
import com.invest.ivcommons.util.endecode.Base64Util;
import com.invest.ivcommons.util.serialize.HessianUtil;
import com.zhuobao.ivbid.processor.UserLoanPolicyProcessor;
import lombok.Data;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by xugang on 2017/8/9.
 */
@Data
public class UserLoanPolicyConsumer {
    private static final Logger logger = LoggerFactory.getLogger(UserLoanPolicyConsumer.class);

    private String topic;
    private String tag;
    private String consumerGroup;
    private String namesrvAddr;

    @Autowired
    private UserLoanPolicyProcessor userLoanPolicyProcessor;

    public void init() {
        try {
            consumerStart(topic, tag, consumerGroup, namesrvAddr);
        } catch (Exception e) {
            logger.error("mq consumer 启动失败,topic=" + topic + ",namesrvAddr=" + namesrvAddr, e);
        }
    }

    public void consumerStart(String topic, String tag, String consumerGroup, String namesrvAddr) throws Exception {
        logger.info("开始启动consumer,topic={},tag={},group={},namesrvAddr={}", new Object[]{topic, tag, consumerGroup, namesrvAddr});
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费 如果非第一次启动，那么按照上次消费的位置继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.subscribe(topic, tag);
        //广播消费
        consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.registerMessageListener((List<MessageExt> msgs, ConsumeConcurrentlyContext context) -> {

            if (logger.isDebugEnabled())
                logger.debug("MQ监听到可消费消息,messageExts={}", JSONObject.toJSONString(msgs));
            for (MessageExt msg : msgs) {
                try {
                    UserLoanPolicyMessage userLoanPolicyMessage =
                            HessianUtil.fromBytes(Base64Util.decode(new String(msg.getBody(), SystemConstant.CHARSET_NAME_UTF8)));
                    if (logger.isDebugEnabled()) {
                        logger.debug("准备处理MQ消息:msg={}", JSONObject.toJSONString(userLoanPolicyMessage));
                    }
                    //处理消息 通知用户策略投标
                    userLoanPolicyProcessor.process(userLoanPolicyMessage);
                } catch (Exception e) {
                    logger.error("Consumer处理消息异常", e);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }
}
