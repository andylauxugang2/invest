package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * Created by xugang on 2017/8/3.
 */
@Data
public class LoanOverdueDetail extends BaseEntity {

    private static final long serialVersionUID = -1865806323182398341L;
    private Long userId;
    private String username; //标编号
    private Integer listingId; //标编号
    private Integer overdueType; //逾期类型 PPDLoanOverdueTypeEnum
    private Date startInterestDate; //起息日期
    private Long repaymentDetailId; //还款明细id
}
