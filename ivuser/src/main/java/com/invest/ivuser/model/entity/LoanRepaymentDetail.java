package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * Created by xugang on 2017/8/3.
 */
@Data
public class LoanRepaymentDetail extends BaseEntity {

    private static final long serialVersionUID = -2324844840272608196L;
    private Long userId;
    private String username; //标编号
    private Integer listingId; //标编号
    private Integer orderId; //还款期数
    private String dueDate; //应还日期 2007-10-10
    private String repayDate; //实际还款日期
    private Double repayPrincipal; //已收本金
    private Double repayInterest; //已收利息
    private Double owingPrincipal; //未收本金
    private Double owingInterest; //未收利息
    private Double owingOverdue; //逾期利息
    private Integer overdueDays; //逾期天数 0
    private Integer repayStatus; //还款状态 0：等待还款 1：准时还款 2：逾期还款 3：提前还款 4：部分还款 @see PPDLoanRepayStatusEnum
}
