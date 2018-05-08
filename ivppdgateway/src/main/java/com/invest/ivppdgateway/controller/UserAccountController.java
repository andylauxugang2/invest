package com.invest.ivppdgateway.controller;

import com.invest.ivcommons.util.format.MoneyUtil;
import com.invest.ivppad.biz.service.ppdopenapi.AccountService;
import com.invest.ivppad.model.result.PPDOpenApiAccountResult;
import com.invest.ivppdgateway.base.BaseController;
import com.invest.ivppdgateway.common.CodeEnum;
import com.invest.ivppdgateway.model.request.GetUserBalanceReq;
import com.invest.ivppdgateway.model.response.APIResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xugang on 2016/9/6.
 */
@RestController
@RequestMapping(value = "/ppd/useraccount")
public class UserAccountController extends BaseController {

    @Resource
    private AccountService accountService;

    @RequestMapping(value = "/getUserBalance", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<String> getUserBalance(@RequestBody GetUserBalanceReq getUserBalanceReq,
                                              HttpServletRequest request, HttpServletResponse response) {
        APIResponse<String> result = new APIResponse<>();
        String userName = getUserBalanceReq.getUserName();
        PPDOpenApiAccountResult accountResult = accountService.getAccountBalance(userName);
        if (accountResult.isFailed()) {
            logger.error("获取用户账户余额失败,userName={},error={}", userName, accountResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), accountResult.getErrorMsg());
        }
        int balance = accountResult.getBalance(); //分
        String balanceRMB = MoneyUtil.formatMoneyRMB(balance);
        logger.info("获取用户账户余额成功,balance={},username={}", balanceRMB, userName);
        result.setData(balanceRMB);
        return result;
    }


}
