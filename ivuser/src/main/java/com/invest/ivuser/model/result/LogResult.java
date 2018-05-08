package com.invest.ivuser.model.result;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivuser.model.entity.BizOptLog;
import com.invest.ivuser.model.entity.UserThirdBindInfo;
import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class LogResult extends Result {

    private static final long serialVersionUID = -7436813682311249816L;

    private List<BizOptLog> bizOptLogs;
}
