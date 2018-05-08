package com.invest.ivpay.biz.service;

import com.invest.ivpay.model.param.PayOverParam;
import com.invest.ivpay.model.result.PayResult;

/**
 * Created by xugang on 2017/10/18.do best.
 */
public interface PayService {

    /**
     * 支付完成处理 下线支付流程
     */
    PayResult payOver(PayOverParam payOverParam);
}
