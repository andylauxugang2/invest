package com.invest.ivuser.model.result;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivuser.model.entity.BlackListThird;
import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class BlackListResult extends Result {
    private static final long serialVersionUID = -6209450697963255965L;

    private List<BlackListThird> blackListThirds;
}
