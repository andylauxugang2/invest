package com.invest.ivgateway.controller;

import com.invest.ivcommons.validate.simple.LoginPasswordValidator;
import com.invest.ivcommons.validate.simple.MobileValidator;
import com.invest.ivusergateway.common.constants.HttpConstant;
import com.invest.ivusergateway.common.constants.SessionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by xugang on 2016/9/6.
 */
public abstract class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected MobileValidator mobileValidator;

    @Resource
    protected LoginPasswordValidator loginPasswordValidator;


    /**
     * 获取会话信息-userId
     *
     * @return
     */
    protected Long getSessionUserId(HttpServletRequest request) {
        String token = request.getHeader(HttpConstant.COOKIE_TOKEN);
        Object userIdObj = SessionConstants.getAttributeUserId(request.getSession());
        if (userIdObj == null) {
            logger.error("获取用户session-userId失败,token={}", token);
            return null;
        }
        Long userId = Long.valueOf(userIdObj.toString());
        return userId;
    }
}
