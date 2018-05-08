package com.invest.ivusergateway.model.request;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class SaveModifyUserLoanPolicyReq {

    private Long userLoanPolicyId;
    private Long userId;
    private String thirdUserUUID;
    private Short userPolicyStatus;
    private Integer bidAmount;
    private Long policyId;

}
