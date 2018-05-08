package com.invest.ivppad.model.param;

import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 16/11/2.
 */
@Data
public class PPDOpenApiGetLoanListingBidRecordBatchParam extends PPDOpenApiBaseParam {
    private static final long serialVersionUID = -3148889180208129245L;

    private List<Integer> listingIds;
}
