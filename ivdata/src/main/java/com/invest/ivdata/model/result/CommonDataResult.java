package com.invest.ivdata.model.result;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivdata.model.entity.SchoolType;
import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class CommonDataResult extends Result {

    private static final long serialVersionUID = 7741416906401405370L;
    private List<SchoolType> schoolTypes;
}
