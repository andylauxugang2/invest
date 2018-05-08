package com.invest.ivppdgateway.model.request;

import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class GetUserLoanBidListReq {
    private String userName;//ppd用户名

    private int listingId; //非必填,按照标的查询输入标的号，否则输入0
    private String startTime; //非必填,开始时间 2016-03-21
    private String endTime; //非必填,结束时间 2016-03-21
    private int pageIndex; //非必填,页码 1
    private int pageSize; //非必填,每页数 20

}
