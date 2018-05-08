package com.invest.ivuser.dao.query;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by xugang on 2017/8/10.
 */
@Data
public class LoanRepaymentDetailQuery {
    private Long userId;
    private String username;
    private Date dueDateBegin;
    private Date dueDateEnd;
    private Integer overdueDays; //逾期天数

    private List<Integer> repayStatusList;

    private boolean paged; //是否分页
    private int pageStart; //页数 从1开始
    private int pageLimit;
}
