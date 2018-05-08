package com.invest.ivpay.model.param;

import com.invest.ivcommons.base.param.BaseParam;
import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class PayOverParam extends BaseParam {
    private static final long serialVersionUID = 3691807777342403837L;

    private Long userId;
    private Short orderType;
    private Short buyCountType;
    private Integer buyCountOther;//其他购买数量
    private Short payway;//支付方式
}
