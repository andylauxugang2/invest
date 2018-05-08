package com.invest.ivuser.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.dao.query.UserLoanRecordQuery;
import com.invest.ivuser.model.entity.UserLoanRecord;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xugang on 2017/8/10.
 */
@Component
public class UserLoanManager extends BaseManager {

    /**
     * 查询ppd账户投资记录
     */
    public List<UserLoanRecord> getUserLoanRecordList(UserLoanRecordQuery query) {
        List<UserLoanRecord> userLoanRecords;
        try {
            userLoanRecords = userLoanRecordDAO.selectListByQuery(query);
        } catch (Exception e) {
            logger.error("查询ppd账户投资记失败,query=" + JSONObject.toJSONString(query), e);
            throw new IVDAOException(e);
        }
        return userLoanRecords;
    }

    /**
     * 查询ppd账户投资记录 总数
     */
    public int getUserLoanRecordTotalCount(UserLoanRecordQuery query) {
        int count = 0;
        try {
            count = userLoanRecordDAO.selectCountByQuery(query);
        } catch (Exception e) {
            logger.error("查询ppd账户投资记录总数失败,query=" + JSONObject.toJSONString(query), e);
            throw new IVDAOException(e);
        }
        return count;
    }

    /**
     * 插入新ppd账户投资记录
     */
    public void saveUserLoanRecord(UserLoanRecord userLoanRecord) {
        try {
            int line = userLoanRecordDAO.insert(userLoanRecord);
            if (line == 1) {
                logger.info("插入ppd账户投资记录成功,username={},record={}", userLoanRecord.getUsername(), JSONObject.toJSONString(userLoanRecord));
            } else {
                throw new IVDAOException("插入ppd账户投资记录数据库返回行数不为1");
            }
        } catch (Exception e) {
            logger.error("插入ppd账户投资记录失败,username=" + userLoanRecord.getUsername(), e);
            throw new IVDAOException(e);
        }
    }
}
