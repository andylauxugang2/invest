package com.invest.ivppdgateway.model.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class GetLoanListDetailBatchReq {
    private Long userId;
    private String userName;//ppd用户名
    private List<Integer> listingIds;
}
