package com.invest.ivuser.model.entity.ext;

import com.invest.ivcommons.base.entity.BaseEntity;
import com.invest.ivuser.model.entity.LoanPolicy;
import lombok.Data;

import java.util.Date;

/**
 * UserPolicyLeftJoinLoanPolicy
 * Created by xugang on 2017/9/11.do best.
 */
@Data
public class LoanRepaymentOverdueDetail extends BaseEntity {
    private static final long serialVersionUID = -277869684719592319L;
    private Long userId;
    private String username; //标编号
    private Integer listingId; //标编号
    private Integer orderId; //还款期数
    private Double repay; //已收本金+利息
    private Double owing; //未收本金+利息
    private Double owingOverdue; //逾期利息
    private Integer overdueDays; //逾期天数 0
    private Date bidTime;
    private Long policyId;
    private String policyName;
    private Integer months;
    private String creditCode;
    private Double rate;
    private Integer amount;
    private Integer bidAmount;
}
