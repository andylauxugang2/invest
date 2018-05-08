package com.invest.ivusergateway.model.request;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class SaveUserThirdLoanPolicyReq {

    private Long userThirdLoanPolicyId;
    private Long userId;
    private Short userThirdLoanPolicyStatus;
    private Integer bidAmount;

}
