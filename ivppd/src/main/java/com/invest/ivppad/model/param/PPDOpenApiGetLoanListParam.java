package com.invest.ivppad.model.param;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.Map;

/**
 * Created by xugang on 16/11/2.
 */
@Data
public class PPDOpenApiGetLoanListParam extends PPDOpenApiBaseParam {
    private static final long serialVersionUID = -3148889180208129245L;

    private int pageIndex; //页码
    private DateTime startDateTime; //如果有则查询该时间之后的列表，精确到毫秒

}
