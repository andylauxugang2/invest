package com.invest.ivppad.model.http.request;

import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/8/1.
 */
@Data
public class PPDOpenApiLoanListingBidRecordBatchRequest extends PPDOpenApiBaseRequest {
    public static final String PARAM_NAME_LISTINGIDS = "ListingIds";

    private List<Integer> listIds; //可投标列表页返回 ListingId


}
