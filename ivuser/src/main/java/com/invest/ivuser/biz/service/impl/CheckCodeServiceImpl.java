package com.invest.ivuser.biz.service.impl;

import com.invest.ivpush.message.client.constants.CodeEnum;
import com.invest.ivpush.message.client.constants.GroupEnum;
import com.invest.ivpush.message.service.SmsSendService;
import com.invest.ivpush.message.service.exception.PushException;
import com.invest.ivuser.biz.service.CheckCodeService;
import com.invest.ivuser.common.IVUserErrorEnum;
import com.invest.ivuser.model.result.CheckCodeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by xugang on 2017/7/28.
 */
@Service
public class CheckCodeServiceImpl implements CheckCodeService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 后期改为rpc服务
     */
    @Resource
    private SmsSendService smsSendService;

    @Override
    public CheckCodeResult getSmsCheckCode(String mobile) {
        CheckCodeResult result = new CheckCodeResult();
        try {
            smsSendService.send(GroupEnum.common.getCode(), CodeEnum.common.getCode(), mobile, "【云片网】您的验证码是1234");
        } catch (PushException e) {
            logger.error("获取短信验证码失败,mobile=" + mobile, e);
            IVUserErrorEnum.GET_SMS_CHECK_CODE_ERROR.fillResult(result);
            return result;
        }
        return result;
    }

    @Override
    public CheckCodeResult getSmsCheckCode() {
        return null;
    }
}
