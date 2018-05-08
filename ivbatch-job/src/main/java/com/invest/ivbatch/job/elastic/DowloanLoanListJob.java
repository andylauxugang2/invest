package com.invest.ivbatch.job.elastic;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.invest.ivbatch.biz.manager.LoanManager;
import com.invest.ivbatch.common.error.ElasticExecuteError;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivcommons.util.format.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * 下载ppd散标列表
 * {"downloadPages":1,"intervel":100,"stop":"true"}
 * 拍拍贷限速 100ms内只能刷1页数据,否则喝茶
 *
 * @Component Created by xugang on 2017/8/5.
 */
@Component
public class DowloanLoanListJob extends ElasticBaseJob {

    //每个分片负责下载的页数
    private static final String JOB_PARAM_NAME_DOWNLOAD_PAGES = "downloadPages";
    private static final int JOB_PARAM_VALUE_DOWNLOAD_PAGES = 1;

    //job调度间隔时长 ms
    private static final String JOB_PARAM_NAME_JOB_RUN_INTERVEL = "intervel";
    private static final int JOB_PARAM_VALUE_JOB_RUN_INTERVEL = 100;

    //job调度间隔时长 ms
    private static final String JOB_PARAM_NAME_JOB_STOP = "stop";
    private static final boolean JOB_PARAM_VALUE_JOB_STOP = false;

    @Autowired
    private LoanManager loanManager;

    @Override
    public void doExecute(ShardingContext shardingContext) throws ElasticExecuteError {
        int shardingCount = shardingContext.getShardingTotalCount();
        int shardingItem = shardingContext.getShardingItem();

        JSONObject param = super.parseJobParam(shardingContext.getJobParameter());

        long intervel = JSONUtil.safeGetInt(param, JOB_PARAM_NAME_JOB_RUN_INTERVEL, JOB_PARAM_VALUE_JOB_RUN_INTERVEL);
        String stop = JSONUtil.safeGetString(param, JOB_PARAM_NAME_JOB_STOP, String.valueOf(JOB_PARAM_VALUE_JOB_STOP));

        Date startDateTime = DateUtil.getCurrentDatetime();
        while (!Boolean.valueOf(stop)) {
            try {
                Thread.sleep(intervel);

                int downloadPages = JSONUtil.safeGetInt(param, JOB_PARAM_NAME_DOWNLOAD_PAGES, JOB_PARAM_VALUE_DOWNLOAD_PAGES);
                //开始下载 异步 等待100ms 发起下次请求
                loanManager.downloadLoanList(downloadPages, shardingItem, startDateTime);
                startDateTime = DateUtil.dateAdd(startDateTime, (int)intervel, Calendar.MILLISECOND);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }

}
