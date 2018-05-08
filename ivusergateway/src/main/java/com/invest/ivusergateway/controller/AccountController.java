package com.invest.ivusergateway.controller;

import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivcommons.util.format.MoneyUtil;
import com.invest.ivcommons.util.format.NumberUtil;
import com.invest.ivpay.biz.service.OrderService;
import com.invest.ivpay.common.OrderStatusEnum;
import com.invest.ivpay.common.OrderTypeEnum;
import com.invest.ivpay.common.PayStatusEnum;
import com.invest.ivpay.model.entity.Order;
import com.invest.ivpay.model.result.OrderResult;
import com.invest.ivuser.biz.service.UserAccountService;
import com.invest.ivuser.model.entity.UserAccount;
import com.invest.ivuser.model.result.UserAccountResult;
import com.invest.ivusergateway.base.BaseController;
import com.invest.ivusergateway.common.constants.CodeEnum;
import com.invest.ivusergateway.model.response.APIResponse;
import com.invest.ivusergateway.model.vo.UserAccountVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by xugang on 2016/9/6.
 */
@RestController
@RequestMapping(value = "/account")
public class AccountController extends BaseController {

    @Resource
    private UserAccountService userAccountService;

    @Resource
    private OrderService orderService;

    /**
     * 我的账户
     */
    @RequestMapping(value = "/getUserAccountInfo", method = RequestMethod.GET, produces = {"application/json"})
    public APIResponse<UserAccountVO> getUserAccountInfo(@RequestParam(name = "userId") Long userId,
                                                         HttpServletRequest request) throws Exception {
        APIResponse<UserAccountVO> result = new APIResponse<>();
        if (userId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        UserAccountResult userAccountResult = userAccountService.getUserAccount(userId);
        if (userAccountResult.isFailed()) {
            logger.error("查询用户账户信息失败,userId={},error={}", userId, userAccountResult.getErrorMsg());
            return APIResponse.createResult(CodeEnum.FAILED, userAccountResult.getErrorMsg());
        }

        UserAccount userAccount = userAccountResult.getUserAccount();
        result.setData(buildUserAccountVO(userAccount));
        return result;
    }

    private UserAccountVO buildUserAccountVO(UserAccount userAccount) {
        UserAccountVO userAccountVO = new UserAccountVO();
        String pattern = "###,###,###";
        userAccountVO.setZhuobaoBalance(NumberUtil.formatPattern(Double.valueOf(userAccount.getZhuobaoBalance()), pattern));
        userAccountVO.setCreateTime(DateUtil.dateToString(userAccount.getCreateTime(), DateUtil.DATE_FORMAT_DATETIME_COMMON));
        userAccountVO.setBidAmountBalance(MoneyUtil.formatMoneyRMB(userAccount.getBidAmountBalance() * 100));
        return userAccountVO;
    }

    /**
     * 手工 同步 支付完成的订单到用户账户余额中
     *
     * @return
     */
    @RequestMapping(value = "/syncUserAccount", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<UserAccount> syncUserAccount(@RequestParam(name = "orderNo") String orderNo,
                                                    @RequestParam(name = "synctoken") String synctoken) {
        APIResponse<UserAccount> result = new APIResponse<>();
        if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(synctoken)) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }
        if (!synctoken.equals("bzj12345ABC")) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        ///查询订单支付状态是否为 已支付
        OrderResult orderResult = orderService.getUserOrderByNo(orderNo);
        if (orderResult.isFailed()) {
            logger.error("查询订单信息失败,orderNo={},error={}", orderNo, orderResult.getErrorMsg());
            return APIResponse.createResult(CodeEnum.FAILED, orderResult.getErrorMsg());
        }

        Order order = orderResult.getOrder();
        if (order.getPayStatus() == null || order.getPayStatus() != PayStatusEnum.unpaid.getCode()) {
            logger.error("订单支付状态有误,orderNo={},payStatus={}", orderNo, order.getPayStatus());
            return APIResponse.createResult(CodeEnum.FAILED, "订单支付状态有误");
        }

        if (order.getOrderType() == null || order.getOrderType() != OrderTypeEnum.recharge.getCode()) {
            logger.error("订单类型有误非充值订单,orderNo={},orderType={}", orderNo, order.getOrderType());
            return APIResponse.createResult(CodeEnum.FAILED, "订单类型有误非充值订单");
        }

        if (order.getOrderStatus() == null || order.getOrderStatus() != OrderStatusEnum.bzj_confirmd_pay.getCode()) {
            logger.error("订单状态有误,orderNo={},orderStatus={}", orderNo, order.getOrderStatus());
            return APIResponse.createResult(CodeEnum.FAILED, "订单状态有误");
        }

        //查询账户信息
        UserAccountResult userAccountResult = userAccountService.getUserAccount(order.getUserId());
        if (userAccountResult.isFailed()) {
            logger.error("查询用户账户信息失败,userId={},error={}", order.getUserId(), userAccountResult.getErrorMsg());
            return APIResponse.createResult(CodeEnum.FAILED, userAccountResult.getErrorMsg());
        }
        UserAccount userAccount = userAccountResult.getUserAccount();

        //充值账户捉宝币
        //保证原子性操作 使用sql事务控制并发update
        UserAccountResult userAccountResult1 = userAccountService.addUserAccountBalance(userAccount.getUserId(), order.getBuyCount());
        if(userAccountResult1.isFailed()){
            logger.error("增加捉宝币失败,orderNo={},error={}", orderNo, userAccountResult1.getErrorMsg());
            return APIResponse.createResult(CodeEnum.FAILED, userAccountResult1.getErrorMsg());
        }

        //更新订单状态
        OrderResult orderResult1 = orderService.modifyUserOrderStatus(order.getId(), OrderStatusEnum.bzj_handled.getCode());
        if(orderResult1.isFailed()){
            logger.error("支付完毕,并处理完毕,更新订单状态失败,orderNo={},error={}", orderNo, orderResult1.getErrorMsg());
            return APIResponse.createResult(CodeEnum.FAILED, orderResult1.getErrorMsg());
        }
        result.setData(userAccount);
        return result;
    }

}
