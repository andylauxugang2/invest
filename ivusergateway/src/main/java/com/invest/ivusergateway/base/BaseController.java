package com.invest.ivusergateway.base;

import com.invest.ivcommons.validate.simple.LoginPasswordValidator;
import com.invest.ivcommons.validate.simple.MobileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created by xugang on 2016/9/6.
 */
public abstract class BaseController<Req, Res> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected MobileValidator mobileValidator;

    @Resource
    protected LoginPasswordValidator loginPasswordValidator;


}
