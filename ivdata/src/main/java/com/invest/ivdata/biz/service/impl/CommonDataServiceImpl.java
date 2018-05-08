package com.invest.ivdata.biz.service.impl;

import com.invest.ivcommons.constant.SystemErrorEnum;
import com.invest.ivdata.biz.manager.SchoolTypeManager;
import com.invest.ivdata.biz.service.CommonDataService;
import com.invest.ivdata.model.entity.SchoolType;
import com.invest.ivdata.model.result.CommonDataResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xugang on 2017/9/13.do best.
 */
@Service
public class CommonDataServiceImpl implements CommonDataService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchoolTypeManager schoolTypeManager;

    @Override
    public CommonDataResult getSchoolTypes(SchoolType schoolType) {
        CommonDataResult result = new CommonDataResult();
        if (schoolType == null) {
            logger.error("参数schoolType为空");
            SystemErrorEnum.PARAM_IS_INVALID.fillResult(result);
            return result;
        }
        List<SchoolType> schoolTypeList = schoolTypeManager.getSchoolTypeList(schoolType);
        result.setSchoolTypes(schoolTypeList);
        return result;
    }
}
