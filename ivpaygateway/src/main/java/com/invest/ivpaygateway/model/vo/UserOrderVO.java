package com.invest.ivpaygateway.model.vo;

import lombok.Data;

/**
 * Created by xugang on 2017/8/16.
 */
@Data
public class UserOrderVO {

    private Long id;
    private String orderNo;
    private String orderType; //订单类型：1-充值订单
    private String orderStatus;
    private String payStatus; // 支付状态：1-未支付，2-已支付
    private String payTime; // 支付时间
    private String price; // 原价
    private String payPrice; // 应付金额
    private String payway; // 支付方式 1-支付宝 2-微信
    private String couponpayPrice; // 代金券支付金额
    private String buyCount; // 购买数量
    private String createTime;
}
