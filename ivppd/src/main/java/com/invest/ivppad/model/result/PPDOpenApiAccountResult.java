package com.invest.ivppad.model.result;

import lombok.Data;

/**
 * Created by xugang on 2017/8/1.
 */
@Data
public class PPDOpenApiAccountResult extends PPDOpenApiResult {
    private int balance; //余额 分为单位
}
