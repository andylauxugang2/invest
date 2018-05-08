package com.invest.ivdata.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivdata.dao.SchoolTypeDAO;
import com.invest.ivdata.model.entity.SchoolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class SchoolTypeManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchoolTypeDAO schoolTypeDAO;

    public List<SchoolType> getSchoolTypeList(SchoolType schoolType) {
        List<SchoolType> result;
        try {
            result = schoolTypeDAO.selectListBySelective(schoolType);
        } catch (Exception e) {
            logger.error("查询学校类型失败,schoolType=" + JSONObject.toJSONString(schoolType), e);
            throw new IVDAOException(e);
        }
        return result;
    }

    public void saveSchoolType(SchoolType schoolType) {
        try {
            int line = schoolTypeDAO.insert(schoolType);
            if (line == 1) {
                logger.info("插入学校类型成功,schoolType={}", JSONObject.toJSONString(schoolType));
            } else {
                throw new IVDAOException("插入学校类型数据库返回行数不为1");
            }
        } catch (Exception e) {
            logger.error("插入学校类型失败,schoolType=" + JSONObject.toJSONString(schoolType), e);
            throw new IVDAOException(e);
        }
    }
}
