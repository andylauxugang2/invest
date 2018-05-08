package com.invest.ivuser.model.entity.ext;

import com.invest.ivuser.model.entity.UserLoanRecord;
import lombok.Data;

/**
 * UserPolicyLeftJoinLoanPolicy
 * Created by xugang on 2017/9/11.do best.
 */
@Data
public class LoanTotalCount extends UserLoanRecord {
    private Integer bidCount; //投标数量
}
