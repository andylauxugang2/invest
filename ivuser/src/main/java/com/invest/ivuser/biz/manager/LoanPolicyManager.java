package com.invest.ivuser.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.model.entity.LoanPolicy;
import com.invest.ivuser.model.entity.ext.UserPolicyDetail;
import com.invest.ivuser.model.param.UserPolicyParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class LoanPolicyManager extends BaseManager {

    public List<LoanPolicy> getLoanPolicyList(LoanPolicy loanPolicy) {
        List<LoanPolicy> loanPolicyList;
        try {
            loanPolicyList = loanPolicyDAO.selectListBySelective(loanPolicy);
        } catch (Exception e) {
            logger.error("查询自定义散标策略失败,loanPolicy=" + JSONObject.toJSONString(loanPolicy), e);
            throw new IVDAOException(e);
        }
        return loanPolicyList;
    }

    public LoanPolicy getLoanPolicyById(Long id) {
        LoanPolicy loanPolicy;
        try {
            loanPolicy = loanPolicyDAO.selectByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("查询自定义散标策略失败,id=" + id, e);
            throw new IVDAOException(e);
        }
        return loanPolicy;
    }

    public List<UserPolicyDetail> getUserPolicyDetailList(UserPolicyParam param) {
        List<UserPolicyDetail> result;
        try {
            result = loanPolicyDAO.selectUserPolicyLeftJoinLoanPolicy(param.getUserId(), param.getThirdUserUUID(),
                    param.getPolicyType(), param.getRiskLevel(), param.getStatus(), param.getPolicyId());
        } catch (Exception e) {
            logger.error("查询散标策略详情失败,param=" + JSONObject.toJSONString(param), e);
            throw new IVDAOException(e);
        }
        return result;
    }

    public void addLoanPolicy(LoanPolicy loanPolicy) {
        try {
            int line = loanPolicyDAO.insert(loanPolicy);
            if (line == 1) {
                logger.info("插入自定义散标策略成功,loanPolicy={}", JSONObject.toJSONString(loanPolicy));
            } else {
                throw new IVDAOException("插入自定义散标策略数据库返回行数不为1");
            }
        } catch (Exception e) {
            logger.error("插入自定义散标策略失败,loanPolicy=" + JSONObject.toJSONString(loanPolicy), e);
            throw new IVDAOException(e);
        }
    }

    public void updateLoanPolicyById(LoanPolicy loanPolicy) {
        try {
            int line = loanPolicyDAO.updateByPrimaryKey(loanPolicy);
            logger.info("更新自定义散标策略成功,line={},loanPolicy={}", line, JSONObject.toJSONString(loanPolicy));
        } catch (Exception e) {
            logger.error("更新自定义散标策略失败,loanPolicy=" + JSONObject.toJSONString(loanPolicy), e);
            throw new IVDAOException(e);
        }
    }

    public void deleteBatchById(List<Long> loanPolicyIds) {
        try {
            loanPolicyDAO.batchDeleteByPrimaryKeys(loanPolicyIds);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }

    public void updateDeletedBatchById(List<Long> loanPolicyIds) {
        try {
            loanPolicyDAO.batchUpdateDeletedByPrimaryKeys(loanPolicyIds);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }


    public void updateLoanPolicyCommonById(LoanPolicy loanPolicy) {
        try {
            int line = loanPolicyDAO.updateCommonByPrimaryKey(loanPolicy);
            logger.info("更新散标策略成功,line={},loanPolicy={}", line, JSONObject.toJSONString(loanPolicy));
        } catch (Exception e) {
            logger.error("更新散标策略失败,loanPolicy=" + JSONObject.toJSONString(loanPolicy), e);
            throw new IVDAOException(e);
        }
    }
}
