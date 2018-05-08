package com.invest.ivpay.model.param;

import com.invest.ivcommons.base.param.BaseParam;
import lombok.Data;

import java.util.Date;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class GetUserOrderParam extends BaseParam {

    private static final long serialVersionUID = -8218452920575421154L;

    private Long userId;
    private Short orderType;
    private Short payStatus;
    private Date orderBeginTime;
    private Date orderEndTime;

    private boolean paged;
    private int page;
    private int limit;
}
