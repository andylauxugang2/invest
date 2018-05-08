package com.invest.ivpay.model.result;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivpay.model.entity.Order;
import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class OrderResult extends Result {

    private static final long serialVersionUID = -7456891147156431232L;
    private Order order;
    private List<Order> orders;

    private int count;
}
