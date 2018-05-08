package com.invest.ivuser.model.param;

import lombok.Data;

import java.util.Date;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserLoanRecordParam extends UserBaseParam {
    private String username;
    private Short policyType;
    private Long policyId;
    private Date bidLoanBeginTime;
    private Date bidLoanEndTime;
    private boolean paged;
    private int page;
    private int limit;
}
