package com.invest.ivuser.mq.producer;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.constant.SystemConstant;
import com.invest.ivcommons.rocketmq.MQProducer;
import com.invest.ivcommons.rocketmq.model.UserLoanPolicyMessage;
import com.invest.ivcommons.util.endecode.Base64Util;
import com.invest.ivcommons.util.serialize.HessianUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;


/**
 * 用户策略变更 h2本地缓存 MQ推送
 * Created by xugang on 2017/8/8.
 */
public class UserLoanPolicyProducer implements MQProducer<UserLoanPolicyMessage> {
    private static final Logger logger = LoggerFactory.getLogger(UserLoanPolicyProducer.class);

    @Resource
    private DefaultMQProducer ivuserMQProducer;

    @Getter
    @Setter
    private String topic;
    @Getter
    @Setter
    private String tag;

    @Override
    public boolean sendMsg(UserLoanPolicyMessage msg) {
        if (msg == null) {
            logger.error("MQ Message is null!");
            return false;
        }
        try {
            String messageBody = Base64Util.encodeToString(HessianUtil.toBytes(msg));
            Message message = new Message(topic, tag, messageBody.getBytes(SystemConstant.CHARSET_NAME_UTF8));
            SendResult sendResult = ivuserMQProducer.send(message);
            logger.debug("send user loan policy result,msgId={}", sendResult.getMsgId());
        } catch (Exception e) {
            logger.error("mq数据发送失败, msg:{}", JSONObject.toJSONString(msg), e);
            return false;
        }
        logger.info("Notify ULoanPolicy [{}] MQ OK!", msg.getOptType());

        return true;
    }
}
