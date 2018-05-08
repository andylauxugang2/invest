package com.invest.ivuser.dao.query;

import lombok.Data;

import java.util.Date;

/**
 * Created by xugang on 2017/8/10.
 */
@Data
public class UserLoanRecordQuery {
    private Long userId;
    private String username;
    private Integer loanId;
    private Date bidLoanBeginTime; //投标开始时间
    private Date bidLoanEndTime; //投标结束时间

    private Short policyType;
    private Short downDetailFlag; //1-下载过详情,0-未下载过详情
    private Short downRepaymentFlag; //1-下载过,0-未下载过

    private boolean paged; //是否分页
    private int pageStart; //页数 从1开始
    private int pageLimit;
}
