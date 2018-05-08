package com.invest.ivpay.model.result;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivpay.model.entity.Order;
import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class PayResult extends Result {

    private static final long serialVersionUID = 6833287644417430083L;

    private Order order;
}
