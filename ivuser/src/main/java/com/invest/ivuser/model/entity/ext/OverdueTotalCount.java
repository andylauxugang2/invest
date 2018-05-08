package com.invest.ivuser.model.entity.ext;

import com.invest.ivuser.model.entity.LoanRepaymentDetail;
import lombok.Data;

/**
 * UserPolicyLeftJoinLoanPolicy
 * Created by xugang on 2017/9/11.do best.
 */
@Data
public class OverdueTotalCount extends LoanRepaymentDetail {
    private Integer overdueCount; //逾期数量
}
