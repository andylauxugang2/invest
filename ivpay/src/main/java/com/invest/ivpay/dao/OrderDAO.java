package com.invest.ivpay.dao;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivpay.dao.query.UserOrderQuery;
import com.invest.ivpay.model.entity.Order;

import java.util.List;

/**
 * Created by xugang on 2017/10/18.do best.
 */
public interface OrderDAO extends BaseDAO {
    List<Order> selectListByQuery(UserOrderQuery query);

    int selectCountByQuery(UserOrderQuery query);

    List<Order> selectListByNo(String orderNo);
}
