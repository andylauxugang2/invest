package com.invest.ivpaygateway.model.request;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class PayOverReq {

    private Long userId;
    private Short buyCountType;
    private Integer buyCountOther;//其他购买数量
    private Short payway;//支付方式
}
