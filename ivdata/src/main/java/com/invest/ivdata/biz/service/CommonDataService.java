package com.invest.ivdata.biz.service;

import com.invest.ivdata.model.entity.SchoolType;
import com.invest.ivdata.model.result.CommonDataResult;

/**
 * Created by xugang on 2017/9/13.do best.
 */
public interface CommonDataService {

    CommonDataResult getSchoolTypes(SchoolType schoolType);
}
