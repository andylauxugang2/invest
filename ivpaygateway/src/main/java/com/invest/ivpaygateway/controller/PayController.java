package com.invest.ivpaygateway.controller;

import com.invest.ivpay.biz.service.PayService;
import com.invest.ivpay.common.BuyCountTypeEnum;
import com.invest.ivpay.common.OrderTypeEnum;
import com.invest.ivpay.common.PayStatusEnum;
import com.invest.ivpay.model.param.PayOverParam;
import com.invest.ivpay.model.result.PayResult;
import com.invest.ivpaygateway.base.BaseController;
import com.invest.ivpaygateway.common.constants.CodeEnum;
import com.invest.ivpaygateway.model.request.PayOverReq;
import com.invest.ivpaygateway.model.response.APIResponse;
import com.invest.ivpaygateway.model.vo.EnumVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xugang on 2016/9/6.
 */
@RestController
@RequestMapping(value = "/pay")
public class PayController extends BaseController {

    @Resource
    private PayService payService;


    @RequestMapping(value = "/payOver", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<String> payOver(@RequestBody PayOverReq payOverReq, HttpServletRequest request) {
        APIResponse<String> result = new APIResponse<>();
        Long userId = payOverReq.getUserId();
        //参数
        if (payOverReq.getUserId() == null || payOverReq.getPayway() == null || payOverReq.getPayway() == null) {
            logger.error("参数验证失败");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //验证购买数量选项
        BuyCountTypeEnum buyCountTypeEnum = BuyCountTypeEnum.findByCode(payOverReq.getBuyCountType());
        if (buyCountTypeEnum == BuyCountTypeEnum.UNKNOWN) {
            logger.error("购买数量类型错误,userId={}", userId);
            return APIResponse.createResult(CodeEnum.FAILED);
        }

        //线下支付流程处理 创建订单 不创建流水
        PayOverParam payOverParam = new PayOverParam();
        payOverParam.setUserId(userId);
        payOverParam.setOrderType(OrderTypeEnum.recharge.getCode());
        payOverParam.setPayway(payOverReq.getPayway());
        payOverParam.setBuyCountType(payOverReq.getBuyCountType());
        payOverParam.setBuyCountOther(payOverReq.getBuyCountOther());
        PayResult payResult = payService.payOver(payOverParam);
        if (payResult.isFailed()) {
            logger.error("线下支付流程处理失败,userId={},result={}", userId, payResult.getErrorMsg());
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), payResult.getErrorMsg());
        }

        result.setData(payResult.getOrder().getOrderNo());

        return result;

    }

    @RequestMapping(value = "/getPayStatus", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<List<EnumVO>> getPayStatus() {
        APIResponse<List<EnumVO>> result = new APIResponse<>();

        List<EnumVO> list = new ArrayList<>();
        for (PayStatusEnum payStatusEnum : PayStatusEnum.values()) {
            EnumVO enumVO = new EnumVO();
            enumVO.setCode(payStatusEnum.getCode());
            enumVO.setDesc(payStatusEnum.getName());
            list.add(enumVO);
        }
        result.setData(list);
        return result;
    }


}
