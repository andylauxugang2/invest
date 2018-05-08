package com.invest.ivpush.message.provider.plus.sms;

import com.invest.ivpush.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xugang on 2017/7/27.
 */
public class SmsPlusYunpianTest extends TestBase {

    @Autowired
    private SmsPlusYunpian smsPlusYunpian;
    private String group = "0";
    private String code = "0";
    private String mobile = "18611410103";

    @Test
    public void testSendSmsMessage() throws Exception {
        smsPlusYunpian.setThirdKey("a18d7c646b28cbcaa189b59b8e084d0e");
        smsPlusYunpian.doSend(mobile, "【云片网】您的验证码是1234");
    }

    @Test
    public void testGetUserInfo() throws Exception {

    }

    public void testSendSms() throws Exception {

    }

    public void testTplSendSms() throws Exception {

    }

    public void testPost() throws Exception {

    }

    public void testDoSend() throws Exception {

    }
}