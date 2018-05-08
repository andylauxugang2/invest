package com.zhuobao.ivbid.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.rocketmq.model.LoanableDetailMessage;
import com.invest.ivcommons.util.endecode.Base64Util;
import com.invest.ivcommons.util.serialize.HessianUtil;
import com.invest.ivppad.model.http.response.PPDOpenApiLoanListingDetailBatchResponse;
import com.zhuobao.ivbid.processor.UserPolicyBidLoanProcessor;
import lombok.Data;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by xugang on 2017/8/9.
 */
@Data
public class PPDConsumer {
    private static final Logger logger = LoggerFactory.getLogger(PPDConsumer.class);

    private String topic;
    private String tag;
    private String consumerGroup;
    private String namesrvAddr;

    @Autowired
    private UserPolicyBidLoanProcessor userPolicyBidLoanProcessor;

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
        consumer.registerMessageListener((List<MessageExt> msgs, ConsumeConcurrentlyContext context) -> {
            if (logger.isDebugEnabled())
                logger.debug("MQ监听到可消费消息,messageExts={}", JSONObject.toJSONString(msgs));
            for (MessageExt msg : msgs) {
                try {
                    LoanableDetailMessage loanableDetailMessage = HessianUtil.fromBytes(Base64Util.decode(new String(msg.getBody())));
                    List<PPDOpenApiLoanListingDetailBatchResponse.LoanListingDetail> loanListingDetails = HessianUtil.fromBytes(Base64Util.decode(loanableDetailMessage.getEncodeMessage()));
                    if (logger.isDebugEnabled()) {
                        logger.debug("准备处理MQ消息:msg={}", JSONObject.toJSONString(loanListingDetails));
                    }
                    //该散标详情所属标页数
                    int pageIndex = loanableDetailMessage.getPageIndex();
                    //处理消息 通知用户策略投标
                    userPolicyBidLoanProcessor.process(loanListingDetails, pageIndex);
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
