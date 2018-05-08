package com.invest.ivuser.model.entity.ext;

import com.invest.ivuser.model.entity.LoanPolicy;
import lombok.Data;

import java.util.Date;

/**
 * UserPolicyLeftJoinLoanPolicy
 * Created by xugang on 2017/9/11.do best.
 */
@Data
public class UserPolicyDetail extends LoanPolicy {
    private Long userPolicyId;
    private String thirdUserUUID;
    private Integer bidAmount;
    private Short userPolicyStatus;
    private Date userPolicyCreateTime;
}
