package com.invest.ivdata.datacache;

import com.google.common.collect.Maps;
import com.invest.ivcommons.localcache.TableCache;
import com.invest.ivdata.biz.service.CommonDataService;
import com.invest.ivdata.model.entity.SchoolType;
import com.invest.ivdata.model.result.CommonDataResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 学校分类本地缓存
 * Created by xugang on 17/1/8.
 */
@Component
public class SchoolTypeDataCache extends TableCache<Map<String, SchoolType>> {

    @Autowired
    private CommonDataService commonDataService;

    @Override
    protected long getRefreshIntervalInSec() {
        //需要异步刷新 TODO
        return -1;
    }

    public SchoolType getSchoolTypeByName(String schoolName) {
        if (StringUtils.isEmpty(schoolName)) {
            logger.error("输入参数不能为空");
            throw new IllegalStateException("schoolName为空");
        }

        Map<String, SchoolType> schoolTypeEnumMap = super.get();
        SchoolType result = schoolTypeEnumMap.get(schoolName);
        return result;
    }

    @Override
    protected Map<String, SchoolType> load() throws Exception {
        Map<String, SchoolType> result = Maps.newHashMap();
        SchoolType param = new SchoolType();
        CommonDataResult commonDataResult = commonDataService.getSchoolTypes(param);
        if (commonDataResult.isFailed()) {
            logger.error("加载学校分类数据失败,error=" + commonDataResult);
            throw new IllegalStateException(commonDataResult.getErrorMsg());
        }

        List<SchoolType> schoolTypeList = commonDataResult.getSchoolTypes();
        if (CollectionUtils.isEmpty(schoolTypeList)) {
            logger.warn("加载学校分类数据为空");
            return result;
        }
        schoolTypeList.stream().forEach(schoolType -> result.put(schoolType.getName().trim(), schoolType));

        logger.info("SUCCESS load all SchoolType records, total db size: {}, key size: {}, expected key size: no limit",
                schoolTypeList.size(), result.keySet().size());
        return result;
    }
}
