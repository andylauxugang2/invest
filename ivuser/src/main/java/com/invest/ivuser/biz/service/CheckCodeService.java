package com.invest.ivuser.biz.service;

import com.invest.ivuser.model.result.CheckCodeResult;

/**
 * Created by xugang on 2017/7/28.
 */
public interface CheckCodeService {

    /**
     * 获取短信验证码
     * @param mobile
     * @return
     */
    CheckCodeResult getSmsCheckCode(String mobile);

    /**
     * 获取图形验证码
     * @return
     */
    CheckCodeResult getSmsCheckCode();
}
