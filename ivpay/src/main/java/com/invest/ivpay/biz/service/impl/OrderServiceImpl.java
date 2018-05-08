package com.invest.ivpay.biz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivpay.biz.manager.OrderManager;
import com.invest.ivpay.biz.service.OrderService;
import com.invest.ivpay.common.IVPayErrorEnum;
import com.invest.ivpay.dao.query.UserOrderQuery;
import com.invest.ivpay.model.entity.Order;
import com.invest.ivpay.model.param.GetUserOrderParam;
import com.invest.ivpay.model.result.OrderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by xugang on 2017/10/18.do best.
 */
@Service
public class OrderServiceImpl implements OrderService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderManager orderManager;

    @Override
    public OrderResult getUserOrders(GetUserOrderParam param) {
        OrderResult result = new OrderResult();
        try {
            UserOrderQuery query = new UserOrderQuery();
            query.setUserId(param.getUserId());
            query.setOrderType(param.getOrderType());
            query.setPayStatus(param.getPayStatus());
            query.setOrderBeginTime(param.getOrderBeginTime());
            query.setOrderEndTime(param.getOrderEndTime());

            boolean paged = param.isPaged();
            query.setPaged(paged);
            int pageStart = (param.getPage() - 1) * param.getLimit();
            query.setPageStart(pageStart);
            query.setPageLimit(param.getLimit());
            List<Order> list = orderManager.getUserOrderList(query);

            int count = list.size();
            if (paged && count != 0) {
                count = orderManager.getUserOrderTotalCount(query);
            }

            result.setOrders(list);
            result.setCount(count);
            return result;
        } catch (Exception e) {
            logger.error("查询用户订单失败,param=" + JSONObject.toJSONString(param), e);
            IVPayErrorEnum.QUERY_USER_ORDER_ERROR.fillResult(result);
            return result;
        }
    }

    @Override
    public OrderResult getUserOrderByNo(String orderNo) {
        OrderResult result = new OrderResult();
        try {
            List<Order> list = orderManager.getUserOrderByNo(orderNo);
            if (CollectionUtils.isEmpty(list)) {
                logger.error("查询用户订单失败,未查询到订单,orderNo=" + orderNo);
                IVPayErrorEnum.QUERY_USER_ORDER_NULL_ERROR.fillResult(result);
                return result;
            }
            if (list.size() > 1) {
                logger.error("查询用户订单失败,结果订单不唯一,orderNo=" + orderNo);
                IVPayErrorEnum.QUERY_USER_ORDER_UNIQUE_ERROR.fillResult(result);
                return result;
            }
            result.setOrder(list.get(0));
        } catch (Exception e) {
            logger.error("查询订单失败,orderNo=" + orderNo, e);
            IVPayErrorEnum.QUERY_USER_ORDER_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public OrderResult modifyUserOrderStatus(Long id, Short orderStatus) {
        OrderResult result = new OrderResult();
        try {
            Order order = new Order();
            order.setId(id);
            order.setOrderStatus(orderStatus);
            order.setCreateTime(null);
            orderManager.updateUserOrderById(order);
        } catch (Exception e) {
            IVPayErrorEnum.UPDATE_USER_ORDER_ERROR.fillResult(result);
        }
        return result;
    }
}
