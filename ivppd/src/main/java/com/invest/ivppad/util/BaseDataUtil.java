package com.invest.ivppad.util;

import com.invest.ivdata.datacache.SchoolTypeDataCache;
import com.invest.ivdata.model.entity.SchoolType;
import com.invest.ivppad.common.SchoolTypeEnum;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 基础数据 工具类
 * Created by xugang on 2017/8/9.
 */
@Component
public class BaseDataUtil {
    public static Logger logger = LoggerFactory.getLogger(BaseDataUtil.class);

    @Autowired
    private SchoolTypeDataCache schoolTypeDataCache;
    private static SchoolTypeDataCache schoolTypeDataCacheBean;

    @PostConstruct
    public void init() {
        schoolTypeDataCacheBean = schoolTypeDataCache;
    }

    //查询学校所属类型标值 211 985
    public static long getSchoolTypeFlag(String schoolName) {
        try {
            SchoolType schoolType = schoolTypeDataCacheBean.getSchoolTypeByName(schoolName);
            //如果学校数据源中查询不到学校 则表示职高(最低)
            if (schoolType == null) return SchoolTypeEnum.typezhigao.getCode();
            //非职高
            Short type = schoolType.getType();
            SchoolTypeEnum schoolTypeEnum = SchoolTypeEnum.findByType(type);
            //type不在枚举中 程序bug 需要改之
            if (schoolTypeEnum == null) {
                logger.error("schoolTypeEnum.findByType查询为空,type=" + type);
                //宁可放过 也不能错投
                return NumberUtils.LONG_ZERO;
            }
            return schoolTypeEnum.getCode();
        } catch (Exception e) {
            logger.warn("参数学校名称为空,schoolName={}", schoolName);
            //如果传给数据的参数name为空 抛异常 表示不存在 返回0 投标匹配时要匹配学校类型无限制的策略
            return NumberUtils.LONG_ZERO;
        }
    }
}
