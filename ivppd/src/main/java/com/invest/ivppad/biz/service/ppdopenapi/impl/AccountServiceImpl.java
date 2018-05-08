package com.invest.ivppad.biz.service.ppdopenapi.impl;

import com.invest.ivcommons.constant.SystemErrorEnum;
import com.invest.ivcommons.util.format.MoneyUtil;
import com.invest.ivppad.biz.manager.PPDOpenApiUserManager;
import com.invest.ivppad.biz.service.ppdopenapi.AccountService;
import com.invest.ivppad.common.IVPPDErrorEnum;
import com.invest.ivppad.common.exception.PPDOpenApiInvokeException;
import com.invest.ivppad.model.http.response.PPDOpenApiQueryUserBalanceResponse;
import com.invest.ivppad.model.result.PPDOpenApiAccountResult;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xugang on 2017/8/1.
 */
@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private PPDOpenApiUserManager openApiAuthManager;

    @Override
    public PPDOpenApiAccountResult getAccountBalance(String userName) {
        PPDOpenApiAccountResult result = new PPDOpenApiAccountResult();

        try {
            //获取余额
            PPDOpenApiQueryUserBalanceResponse response = openApiAuthManager.getUserBalance(userName);
            if (!response.success()) {
                SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
                logger.error("获取用户账户余额失败,userName={},response={}", userName, response);
                return result;
            }

            List<PPDOpenApiQueryUserBalanceResponse.Balance> balanceList = response.getBalanceList();
            if (CollectionUtils.isEmpty(balanceList)) {
                SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
                logger.error("获取用户账户余额为空,userName={},response={}", userName, response);
                return result;
            }

            //过滤余额 用户备付金.用户现金余额
            PPDOpenApiQueryUserBalanceResponse.Balance balance = filterUserBalance(balanceList);
            if (balance == null) {
                IVPPDErrorEnum.CAN_NOT_GET_ACCOUNT_BALANCE_ERROR.fillResult(result);
                logger.error("获取用户账户余额为空-用户备付金.用户现金余额,userName={},response={}", userName, response);
                return result;
            }

            result.setBalance(MoneyUtil.toSmallDenomination(balance.getBalance().intValue())); //单位:分

        } catch (Exception e) {
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            result.setErrorMsg(e.getMessage());
            logger.error("获取用户账户余额失败,userName=" + userName + ",msg=" + e.getMessage());
        }

        return result;
    }

    private PPDOpenApiQueryUserBalanceResponse.Balance filterUserBalance(List<PPDOpenApiQueryUserBalanceResponse.Balance> balanceList) {
        PPDOpenApiQueryUserBalanceResponse.Balance result = null;
        for (PPDOpenApiQueryUserBalanceResponse.Balance balance : balanceList) {
            if (balance.getAccountCategory().equals("用户备付金.用户现金余额")) {
                result = balance;
                break;
            }
        }
        return result;
    }
}
