package com.invest.ivppad.model.param;

import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 16/11/2.
 */
@Data
public class PPDOpenApiGetLoanListingDetailBatchParam extends PPDOpenApiBaseParam {
    private static final long serialVersionUID = -3148889180208129245L;

    //单次获取的标的详情总数 10
    public static final int FETCH_BATCH_DETAIL_SIZE = 10;

    private List<Integer> listingIds; //可投标列表页返回 ListingId
}
