package com.invest.ivpay.biz.manager;

import com.invest.ivpay.dao.ProductDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xugang on 2017/10/18.do best.
 */
@Component
public class ProductManager {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected ProductDAO productDAO;
}
