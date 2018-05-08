package com.invest.ivpaygateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivcommons.util.format.MoneyUtil;
import com.invest.ivpay.biz.service.OrderService;
import com.invest.ivpay.common.OrderStatusEnum;
import com.invest.ivpay.common.OrderTypeEnum;
import com.invest.ivpay.common.PayStatusEnum;
import com.invest.ivpay.common.PaywayEnum;
import com.invest.ivpay.model.entity.Order;
import com.invest.ivpay.model.param.GetUserOrderParam;
import com.invest.ivpay.model.result.OrderResult;
import com.invest.ivpaygateway.base.BaseController;
import com.invest.ivpaygateway.common.constants.CodeEnum;
import com.invest.ivpaygateway.model.response.APIResponse;
import com.invest.ivpaygateway.model.vo.EnumVO;
import com.invest.ivpaygateway.model.vo.UserOrderVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xugang on 2016/9/6.
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/getOrderType", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<List<EnumVO>> getPolicyType() {
        APIResponse<List<EnumVO>> result = new APIResponse<>();

        List<EnumVO> list = new ArrayList<>();
        for (OrderTypeEnum orderTypeEnum : OrderTypeEnum.values()) {
            EnumVO enumVO = new EnumVO();
            enumVO.setCode(orderTypeEnum.getCode());
            enumVO.setDesc(orderTypeEnum.getName());
            list.add(enumVO);
        }
        result.setData(list);
        return result;
    }

    @RequestMapping(value = "/getUserOrder", method = RequestMethod.GET, produces = {"application/json"})
    public APIResponse<List<UserOrderVO>> getUserOrder(@RequestParam(name = "userId") Long userId,
                                                       @RequestParam(name = "orderType", required = false) Short orderType,
                                                       @RequestParam(name = "payStatus", required = false) Short payStatus,
                                                       @RequestParam(name = "orderBeginTime", required = false) String orderBeginTime,
                                                       @RequestParam(name = "orderEndTime", required = false) String orderEndTime,
                                                       @RequestParam(name = "page") Integer page,
                                                       @RequestParam(name = "limit") Integer limit,
                                                       HttpServletRequest request) throws Exception {
        APIResponse<List<UserOrderVO>> result = new APIResponse<>();
        if (userId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        GetUserOrderParam getUserOrderParam = new GetUserOrderParam();
        getUserOrderParam.setUserId(userId);
        getUserOrderParam.setOrderType(orderType);
        getUserOrderParam.setPayStatus(payStatus);
        getUserOrderParam.setOrderBeginTime(DateUtil.stringToDate(orderBeginTime, DateUtil.DATE_FORMAT_DATETIME_COMMON));
        getUserOrderParam.setOrderEndTime(DateUtil.stringToDate(orderEndTime, DateUtil.DATE_FORMAT_DATETIME_COMMON));
        getUserOrderParam.setPaged(true);
        getUserOrderParam.setPage(page);
        getUserOrderParam.setLimit(limit);
        OrderResult orderResult = orderService.getUserOrders(getUserOrderParam);
        if (orderResult.isFailed()) {
            logger.error("查询用户订单失败,param={}", JSONObject.toJSONString(getUserOrderParam));
            return APIResponse.createResult(CodeEnum.FAILED);
        }

        List<Order> orderList = orderResult.getOrders();
        if (CollectionUtils.isEmpty(orderList)) {
            result.setData(Lists.newArrayList());
            return result;
        }
        List<UserOrderVO> userOrderVOs = new ArrayList<>(orderList.size());
        userOrderVOs.addAll(orderList.stream().map(this::buildUserOrderVO).collect(Collectors.toList()));
        result.setData(userOrderVOs);
        result.setCount(orderResult.getCount());
        return result;
    }

    private UserOrderVO buildUserOrderVO(Order order) {
        UserOrderVO vo = new UserOrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setOrderType(OrderTypeEnum.findByCode(order.getOrderType()).getName());
        vo.setOrderStatus(OrderStatusEnum.findByCode(order.getOrderStatus()).getName());
        vo.setPayStatus(PayStatusEnum.findByCode(order.getPayStatus()).getName());
        vo.setPrice(MoneyUtil.formatMoneyRMB(order.getPrice()));
        vo.setPayPrice(MoneyUtil.formatMoneyRMB(order.getPayPrice()));
        vo.setPayway(PaywayEnum.findByCode(order.getPayway()).getName());
        vo.setPayTime(DateUtil.dateToString(order.getPayTime(), DateUtil.DATE_FORMAT_DATETIME_COMMON));
        vo.setCreateTime(DateUtil.dateToString(order.getCreateTime(), DateUtil.DATE_FORMAT_DATETIME_COMMON));
        return vo;
    }

}
