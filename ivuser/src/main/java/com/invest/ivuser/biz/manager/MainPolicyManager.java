package com.invest.ivuser.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.model.entity.MainPolicy;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class MainPolicyManager extends BaseManager {


    public List<MainPolicy> getMainPolicyList(MainPolicy mainPolicy) {
        List<MainPolicy> result;
        try {
            result = mainPolicyDAO.selectListBySelective(mainPolicy);
        } catch (Exception e) {
            logger.error("查询主策略失败,mainPolicy=" + JSONObject.toJSONString(mainPolicy), e);
            throw new IVDAOException(e);
        }
        int size = 0;
        if (CollectionUtils.isNotEmpty(result)) size = result.size();
        logger.info("查询主策略成功,size={}", size);
        return result;
    }

    public void saveMainPolicy(MainPolicy mainPolicy) {
        try {
            int line = mainPolicyDAO.insert(mainPolicy);
            if (line == 1) {
                logger.info("插入主策略策略成功,mainPolicy={}", JSONObject.toJSONString(mainPolicy));
            } else {
                throw new IVDAOException("插入主策略数据库返回行数不为1");
            }
        } catch (Exception e) {
            logger.error("插入主策略失败,mainPolicy=" + JSONObject.toJSONString(mainPolicy), e);
            throw new IVDAOException(e);
        }
    }

    public MainPolicy getMainPolicyById(Long id) {
        MainPolicy result;
        try {
            result = mainPolicyDAO.selectByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("查询主策略失败,id=" + id, e);
            throw new IVDAOException(e);
        }
        return result;
    }
}
