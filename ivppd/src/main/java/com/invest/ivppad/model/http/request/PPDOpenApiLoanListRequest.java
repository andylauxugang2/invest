package com.invest.ivppad.model.http.request;

import lombok.Data;

/**
 * Created by xugang on 2017/8/1.
 */
@Data
public class PPDOpenApiLoanListRequest extends PPDOpenApiBaseRequest {
    public static final String PARAM_NAME_PAGEINDEX = "PageIndex";
    public static final String PARAM_NAME_STARTDATETIME = "StartDateTime";

    private int pageIndex; //页码
    private String startDateTime; //如果有则查询该时间之后的列表，精确到毫秒 2015-11-11 12:00:00.000


}
