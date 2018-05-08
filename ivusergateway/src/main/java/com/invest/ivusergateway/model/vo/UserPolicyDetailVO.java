package com.invest.ivusergateway.model.vo;

import lombok.Data;

/**
 * Created by xugang on 2017/8/16.
 */
@Data
public class UserPolicyDetailVO extends LoanPolicyVO {
    private String username; //第三方账号
    private Integer bidAmount; //单笔投资金额
    private Short userPolicyStatus; //策略状态 开启,停止
    private Long userPolicyId;
    private String userPolicyCreateTimeFormat;
}
