package com.invest.ivpaygateway.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xugang on 2016/9/6.
 */
public abstract class BaseController<Req, Res> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

}
