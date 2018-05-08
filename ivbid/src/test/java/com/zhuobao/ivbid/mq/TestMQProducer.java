package com.zhuobao.ivbid.mq;

import com.invest.ivcommons.rocketmq.MQProducer;
import com.invest.ivcommons.rocketmq.model.LoanableDetailMessage;
import com.invest.ivcommons.util.endecode.Base64Util;
import com.invest.ivcommons.util.serialize.HessianUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;


/**
 * 可投标详情列表MQ推送
 * Created by xugang on 2017/8/8.
 */
public class TestMQProducer implements MQProducer<LoanableDetailMessage> {
    private static final Logger logger = LoggerFactory.getLogger(TestMQProducer.class);

    @Resource
    private DefaultMQProducer defaultMQProducer;

    @Getter
    @Setter
    private String topic;
    @Getter
    @Setter
    private String tag;

    @Override
    public boolean sendMsg(LoanableDetailMessage msg) {
        //String text = msg.getEncodeMessage();
        try {
            String text = Base64Util.encodeToString(HessianUtil.toBytes(msg));
            if (StringUtils.isBlank(text)) {
                logger.error("MQ Message is null!");
                return true;
            }
            Message message = new Message(topic, tag, text.getBytes("UTF-8"));
            SendResult sendResult = defaultMQProducer.send(message);
            logger.debug("send loanable detail result,msgId={}", sendResult.getMsgId());
        } catch (Exception e) {
            logger.error("mq数据发送失败, msg:{}", msg, e);
            return false;
        }

        logger.info("Notify UPolicy MQ OK!");

        return true;
    }
}
