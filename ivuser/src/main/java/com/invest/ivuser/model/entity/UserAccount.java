package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserAccount extends BaseEntity {
    private static final long serialVersionUID = 8562804316273543680L;

    public  static final int ZHUOBAOBI_BID_AMOUNT_BASE = 500;
    private Long userId;
    private Integer zhuobaoBalance; //捉宝币
    private Integer bidAmountBalance; //剩余可投标金额 与捉宝币对应
    private Integer cashBalance;
    private Short status;
}
