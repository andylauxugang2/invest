package com.invest.ivpush.message.provider.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.invest.ivpush.message.model.entity.SmsLog;
import com.invest.ivpush.message.model.entity.SmsTemplate;
import com.invest.ivpush.message.provider.exception.MessageException;
import com.invest.ivpush.message.provider.plus.MessagePlus;
import com.invest.ivpush.message.provider.plus.SmsMessagePlus;
import com.invest.ivpush.message.provider.service.MessagePlusService;
import com.invest.ivpush.message.provider.service.SmsLogService;
import com.invest.ivpush.message.service.SmsSendService;
import com.invest.ivpush.message.service.SmsTemplateService;
import com.invest.ivpush.message.service.exception.PushException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service("smsSendService")
public class SmsSendServiceImpl implements SmsSendService {

    private static final Logger logger = LoggerFactory.getLogger(SmsSendServiceImpl.class);

    @Resource
    private SmsLogService smsLogService;

    @Resource
    private SmsTemplateService smsTemplateService;

    @Resource
    private MessagePlusService messagePlusService;

    @Value(value = "${ivpush.switch.mock}")
    private boolean switchMock;

    /**
     * 直接发送 template表里无需指定template内容 需要寻找第三方资源
     *
     * @param group
     * @param code
     * @param mobile
     * @param content
     */
    @Async
    @Override
    public void send(String group, String code, String mobile, String content) {
        if (StringUtils.isBlank(content)) {
            throw new PushException("sms直接发送需要指定发送内容content,mobile=" + mobile);
        }
        this.doSend(group, code, mobile, content, null);
    }

    /**
     * 使用模板发送,template表里指定template内容用data替换, 需要寻找第三方资源
     *
     * @param group
     * @param code
     * @param mobile
     * @param data
     * @throws PushException
     */
    @Async
    @Override
    public void send(String group, String code, String mobile, Map<String, String> data) throws PushException {
        this.doSend(group, code, mobile, null, data);
    }

    private void doSend(String group, String code, String mobile, String content, Map<String, String> data) throws PushException {
        //根据group和code查询模板
        SmsTemplate smsTemplate = smsTemplateService.getByGroupAndCode(group, code);
        if (smsTemplate == null) {
            logger.error("sms未查到模板,group={},code={},mobile={}", group, code, mobile);
            throw new PushException("未查到模板");
        }
        //直接发送 不使用模板
        if (StringUtils.isNotBlank(content)) {
            logger.info("sms直接发送,不使用模板,group={},code={},mobile={}", group, code, mobile);
        } else {
            content = smsTemplateService.buildContent(smsTemplate, data);
        }

        //获取短信第三方资源
        List<Long> thirdAuthIds = smsTemplate.transferThirdAuthIds(smsTemplate.getThirdAuthIds());
        if (CollectionUtils.isEmpty(thirdAuthIds)) {
            logger.error("sms无第三方资源配置,group={},code={},mobile={}", group, code, mobile);
            throw new PushException("sms无第三方资源配置");
        }

        SmsLog log = new SmsLog();
        log.setTmpCode(code);
        log.setTmpGroup(group);
        log.setContent(content);
        log.setMobile(mobile);
        //直接发送不使用data
        if (null != data) {
            log.setData(JSON.toJSONString(data));
        }

        //使用第三方所有资源尝试发送短信
        List<MessagePlus> messagePlusList = messagePlusService.genrate(thirdAuthIds);
        for (MessagePlus messagePlus : messagePlusList) {
            try {
                SmsMessagePlus plus = (SmsMessagePlus) messagePlus;
                String msgId = StringUtils.EMPTY;
                if(!switchMock){
                    msgId = plus.doSend(mobile, content);
                }
                log.setMsgId(msgId);
                logger.info("sms发送短信成功,mobile={},msgId={},plus={}", mobile, msgId, plus);
                log.setCountOk(log.getCountOk() + 1);
                log.setState((short) 1);
                break;
            } catch (MessageException e) {
                logger.info("sms发送短信失败,mobile={},plus={}, error={}", mobile, messagePlus, e);
                log.setCountFail(log.getCountFail() + 1);
                if (null != e.getCode())
                    log.setMsgReturn(e.getCode());
                else
                    log.setMsgReturn(e.getMessage());
            }
        }

        //保存log
        try {
            smsLogService.save(log);
        } catch (PushException e) {
            logger.error("存储SMS日志失败,mobile={},error={}", mobile, e.getMessage());
            //spring事务管理-marked as rollback-only,抛出用于回滚 throw Exception 否则不打印详细错误信息
        }
    }

}
