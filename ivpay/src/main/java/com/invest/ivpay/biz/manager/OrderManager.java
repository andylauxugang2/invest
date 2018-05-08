package com.invest.ivpay.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivcommons.util.string.StringUtil;
import com.invest.ivpay.common.OrderTypeEnum;
import com.invest.ivpay.dao.OrderDAO;
import com.invest.ivpay.dao.query.UserOrderQuery;
import com.invest.ivpay.model.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by xugang on 2017/10/18.do best.
 */
@Component
public class OrderManager {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected OrderDAO orderDAO;

    public void createOrder(Order order) {
        try {
            int line = orderDAO.insert(order);
            if (line == 1) {
                logger.info("新增订单成功,userId={},orderNo={}", order.getUserId(), order.getOrderNo());
            } else {
                throw new IVDAOException("新增订单成功数据库返回行数不为1");
            }
        } catch (Exception e) {
            logger.error("新增订单失败,order=" + JSONObject.toJSONString(order), e);
            throw new IVDAOException(e);
        }
    }

    /**
     * 20150826040061749694
     * date + 04random + 100orderNoTypeEnum + HHmmss + 94random
     */
    public String genOrderNo(OrderTypeEnum orderNoTypeEnum) {
        Date now = DateUtil.getCurrentDatetime();
        String date = DateUtil.dateToString(now, "yyyyMMdd");
        String seconds = DateUtil.dateToString(now, "HHmmss");
        StringBuilder orderNo = new StringBuilder();
        orderNo.append(date).
                append(StringUtil.genRandomNumString(2)).
                append(orderNoTypeEnum.getCode()).
                append(seconds).
                append(StringUtil.genRandomNumString(2));
        return orderNo.toString();
    }

    public List<Order> getUserOrderList(UserOrderQuery query) {
        List<Order> orderList;
        try {
            orderList = orderDAO.selectListByQuery(query);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return orderList;
    }

    public int getUserOrderTotalCount(UserOrderQuery query) {
        int count = 0;
        try {
            count = orderDAO.selectCountByQuery(query);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return count;
    }

    public List<Order> getUserOrderByNo(String orderNo) {
        List<Order> orderList;
        try {
            orderList = orderDAO.selectListByNo(orderNo);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return orderList;
    }

    public void updateUserOrderById(Order order) {
        try {
            orderDAO.updateByPrimaryKey(order);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }
}
