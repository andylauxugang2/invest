package com.invest.ivppdgateway.model.request;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class GetUserLoanRepaymentReq {
    private String userName;//ppd用户名

    private Integer listingId; //非必填,按照标的查询输入标的号，否则输入0
    private String periods; //还款期数 参数为空时，获取当前列表所有期数；参数不为空时，获取传参的期数 1|2|3

}
