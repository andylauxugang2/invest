package com.invest.ivpay.biz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivcommons.util.format.MoneyUtil;
import com.invest.ivpay.biz.manager.OrderManager;
import com.invest.ivpay.biz.service.PayService;
import com.invest.ivpay.common.*;
import com.invest.ivpay.model.entity.Order;
import com.invest.ivpay.model.param.PayOverParam;
import com.invest.ivpay.model.result.PayResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 * Created by xugang on 2017/10/18.do best.
 */
@Service
public class PayServiceImpl implements PayService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderManager orderManager;

    @Override
    public PayResult payOver(PayOverParam payOverParam) {
        PayResult result = new PayResult();
        //支付完成后 创建支付订单
        try {
            //防止重复下单 TODO
            Order order = buildOrder(payOverParam);
            orderManager.createOrder(order);
            result.setOrder(order);
            logger.info("线下支付处理成功,userId={},orderNo={}", payOverParam.getUserId(), order.getOrderNo());
        } catch (Exception e) {
            logger.error("线下支付处理异常,payOverParam=" + JSONObject.toJSONString(payOverParam), e);
            IVPayErrorEnum.PAY_UNLINE_HANDLE_ERROR.fillResult(result);
        }

        return result;
    }

    private Order buildOrder(PayOverParam payOverParam) {
        Order order = new Order();
        order.setUserId(payOverParam.getUserId());
        order.setOrderNo(orderManager.genOrderNo(OrderTypeEnum.findByCode(payOverParam.getOrderType())));
        order.setOrderType(payOverParam.getOrderType());
        order.setPayTime(DateUtil.getCurrentDatetime());
        order.setPayway(payOverParam.getPayway());
        order.setOrderStatus(OrderStatusEnum.bzj_confirming_pay.getCode());
        //目前产品表 用类型code
        order.setProductId((long) OrderTypeEnum.recharge.getCode());

        int buyCount = 0;
        int price = 0;
        Short buyCountType = payOverParam.getBuyCountType();
        if (buyCountType == null) {
            throw new IllegalArgumentException("充值捉宝币失败,购买数量类型获取不到");
        }
        BuyCountTypeEnum buyCountTypeEnum = BuyCountTypeEnum.findByCode(buyCountType);
        if (buyCountTypeEnum != BuyCountTypeEnum.buycountType10) {
            buyCount = buyCountTypeEnum.getValue();
            price = buyCountTypeEnum.getPrice();
        } else {
            Integer buyCountOther = payOverParam.getBuyCountOther();
            if (buyCountOther != null && buyCountOther > 0) {
                price = buyCountOther;
                buyCount = buyCountOther + BigDecimal.valueOf(buyCountOther).multiply(BigDecimal.valueOf(Order.ZHUOBAOBI_DISCOUNT)).intValue();
            }
        }

        if (buyCount == 0 || price == 0) {
            throw new IllegalArgumentException("充值捉宝币数量或金额错误");
        }
        order.setBuyCount(buyCount);
        //原价 单位:分
        order.setPrice(MoneyUtil.toSmallDenomination(price));
        //实际支付金额:初始化0
        order.setPayPrice(Order.PAYPRICE_DEFAULT);
        order.setPayStatus(PayStatusEnum.paid.getCode());
        return order;
    }
}
