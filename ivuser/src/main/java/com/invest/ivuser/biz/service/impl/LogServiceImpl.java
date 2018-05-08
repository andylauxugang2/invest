package com.invest.ivuser.biz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.constant.SystemErrorEnum;
import com.invest.ivuser.biz.manager.BizOptLogManager;
import com.invest.ivuser.biz.service.LogService;
import com.invest.ivuser.model.entity.BizOptLog;
import com.invest.ivuser.model.param.LogParam;
import com.invest.ivuser.model.result.LogResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Service
public class LogServiceImpl implements LogService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BizOptLogManager bizOptLogManager;

    @Override
    public LogResult getBizOptLogs(LogParam logParam) {
        LogResult result = new LogResult();
        try {
            BizOptLog bizOptLog = new BizOptLog();
            List<BizOptLog> bizOptLogs = bizOptLogManager.getBizOptLogList(bizOptLog);
            result.setBizOptLogs(bizOptLogs);
        } catch (Exception e) {
            logger.error("查询操作日志失败,logParam=" + JSONObject.toJSONString(logParam), e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            return result;
        }

        return result;
    }

    @Override
    public LogResult addBizOptLog(BizOptLog bizOptLog) {
        LogResult result = new LogResult();
        try {
            bizOptLogManager.saveBizOptLog(bizOptLog);
        } catch (Exception e) {
            logger.error("插入bizOptLog失败,bizOptLog=" + JSONObject.toJSONString(bizOptLog), e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            return result;
        }
        return result;
    }
}
