package com.invest.ivuser.biz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.result.Result;
import com.invest.ivcommons.constant.SystemErrorEnum;
import com.invest.ivuser.biz.manager.BlackListThirdManager;
import com.invest.ivuser.biz.service.BlackListService;
import com.invest.ivuser.model.entity.BlackListThird;
import com.invest.ivuser.model.result.BlackListResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Service
public class BlackListServiceImpl implements BlackListService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BlackListThirdManager blackListThirdManager;

    @Override
    public Result addBlackListThird(BlackListThird blackListThird) {
        Result result = new Result();
        try {
            blackListThirdManager.saveBlackListThird(blackListThird);
        } catch (Exception e) {
            logger.error("保存第三方账户黑名单失败");
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public BlackListResult getBlackListThirdList(BlackListThird blackListThird) {
        BlackListResult result = new BlackListResult();
        try {
            List<BlackListThird> list = blackListThirdManager.getBlackListThirdList(blackListThird);
            result.setBlackListThirds(list);
        } catch (Exception e) {
            logger.error("查询第三方账户黑名单失败,blackListThird=" + JSONObject.toJSONString(blackListThird), e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public Result removeBlackList(String type, String value) {
        Result result = new Result();
        try {
            blackListThirdManager.deleteBlackListThird(type, value);
        } catch (Exception e) {
            logger.error("删除第三方账户黑名单失败,type=" + type + ",value=" + value);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }
}
