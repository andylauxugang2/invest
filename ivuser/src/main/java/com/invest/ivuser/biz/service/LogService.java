package com.invest.ivuser.biz.service;

import com.invest.ivuser.model.entity.BizOptLog;
import com.invest.ivuser.model.param.LogParam;
import com.invest.ivuser.model.result.LogResult;

/**
 * Created by xugang on 2017/7/28.
 */
public interface LogService {

    LogResult getBizOptLogs(LogParam logParam);

    LogResult addBizOptLog(BizOptLog bizOptLog);
}
