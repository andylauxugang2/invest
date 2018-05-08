package com.invest.ivpay.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * Created by xugang on 2017/10/18.do best.
 */
@Data
public class Order extends BaseEntity {
    private static final long serialVersionUID = 5180403358944565697L;

    public static final int PAYPRICE_DEFAULT = 0;
    //充值折扣 > 800生效
    public static final double ZHUOBAOBI_DISCOUNT = 0.8;

    private String orderNo;
    private Long userId;
    private Long productId;
    private Short orderType; //订单类型：1-充值订单
    private Short orderStatus; //订单状态：
    private Short payStatus; // 支付状态：1-未支付，2-已支付
    private Date payTime; // 支付时间
    private Integer price; // 原价（分）
    private Integer payPrice; // 应付价格（分）
    private Short payway; // 支付方式 1-支付宝 2-微信
    private Integer couponpayPrice; // 代金券支付金额
    private Integer buyCount; // 购买数量

}
