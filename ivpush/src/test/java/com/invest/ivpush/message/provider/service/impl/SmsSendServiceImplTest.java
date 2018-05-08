package com.invest.ivpush.message.provider.service.impl;

import com.invest.ivpush.TestBase;
import com.invest.ivpush.message.service.SmsSendService;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by xugang on 2017/7/28.
 */
public class SmsSendServiceImplTest extends TestBase {

    @Resource
    private SmsSendService smsSendService;

    @Test
    public void testSend() throws Exception {
        String group = "0";
        String code = "0";
        String mobile = "18611410103";
        //直接发送不使用模板
        String content = "【云片网】您的验证码是1234";
        smsSendService.send(group, code, mobile, content);
    }
}