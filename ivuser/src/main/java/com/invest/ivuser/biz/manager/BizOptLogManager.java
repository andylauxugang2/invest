package com.invest.ivuser.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.model.entity.BizOptLog;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class BizOptLogManager extends BaseManager {

    public List<BizOptLog> getBizOptLogList(BizOptLog bizOptLog) {
        List<BizOptLog> result;
        try {
            result = bizOptLogDAO.selectListBySelective(bizOptLog);
        } catch (Exception e) {
            logger.error("查询操作日志失败,bizOptLog=" + JSONObject.toJSONString(bizOptLog), e);
            throw new IVDAOException(e);
        }
        return result;
    }

    public void saveBizOptLog(BizOptLog bizOptLog) {
        try {
            int line = bizOptLogDAO.insert(bizOptLog);
            if (line == 1) {
                logger.info("插入操作日志成功,bizOptLog={}", JSONObject.toJSONString(bizOptLog));
            } else {
                throw new IVDAOException("插入操作日志数据库返回行数不为1");
            }
        } catch (Exception e) {
            logger.error("插入操作日志失败,bizOptLog=" + JSONObject.toJSONString(bizOptLog), e);
            throw new IVDAOException(e);
        }
    }
}
