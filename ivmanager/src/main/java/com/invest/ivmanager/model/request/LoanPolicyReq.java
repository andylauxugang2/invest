package com.invest.ivmanager.model.request;

import lombok.Data;

/**
 * Created by xugang on 2017/10/25.do best.
 */
@Data
public class LoanPolicyReq {
    private Long userId;
    private Short policyType;
    private Short status;

}
