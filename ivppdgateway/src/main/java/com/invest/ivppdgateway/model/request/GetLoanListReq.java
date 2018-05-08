package com.invest.ivppdgateway.model.request;

import lombok.Data;

import java.util.Date;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class GetLoanListReq {
    private String userName;//ppd用户名
    private int pageIndex; //页码
    private Date startDateTime; //如果有则查询该时间之后的列表，精确到毫秒
}
