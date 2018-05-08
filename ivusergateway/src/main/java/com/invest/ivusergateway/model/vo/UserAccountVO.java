package com.invest.ivusergateway.model.vo;

import lombok.Data;

/**
 * Created by xugang on 2017/8/16.
 */
@Data
public class UserAccountVO {

    private Long userId;
    private String zhuobaoBalance; //捉宝币
    private String cashBalance;
    private String bidAmountBalance;
    private String status;
    private String createTime;
}
