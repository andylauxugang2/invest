package com.invest.ivpay.biz.service;

import com.invest.ivpay.model.param.GetUserOrderParam;
import com.invest.ivpay.model.result.OrderResult;

/**
 * Created by xugang on 2017/10/18.do best.
 */
public interface OrderService {

    OrderResult getUserOrders(GetUserOrderParam getUserOrderParam);

    OrderResult getUserOrderByNo(String orderNo);

    OrderResult modifyUserOrderStatus(Long id, Short orderStatus);
}
