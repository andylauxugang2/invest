package com.invest.ivppad.model.param;

import lombok.Data;

/**
 * Created by xugang on 16/11/2.
 */
@Data
public class PPDOpenApiGetUserLoanRepaymentParam extends PPDOpenApiBaseParam {
    private static final long serialVersionUID = -3148889180208129245L;

    private int listingId; //必填
    private String periods; //还款期数 参数为空时，获取当前列表所有期数；参数不为空时，获取传参的期数 1|2|3

}
