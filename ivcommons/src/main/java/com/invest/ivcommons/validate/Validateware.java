package com.invest.ivcommons.validate;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivcommons.validate.model.ValidParam;

/**
 * Created by xugang on 2017/7/28.
 */
public interface Validateware<R extends Result, P extends ValidParam> {
    R valid(P validParam);
}
