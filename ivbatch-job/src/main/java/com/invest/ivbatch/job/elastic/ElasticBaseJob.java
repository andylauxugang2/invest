package com.invest.ivbatch.job.elastic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.invest.ivbatch.common.error.ElasticExecuteError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

/**
 * @Component Created by xugang on 2017/8/5.
 */
public abstract class ElasticBaseJob implements SimpleJob {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(ShardingContext shardingContext) {
        String jobName = shardingContext.getJobName();
        logger.info("开始执行[{}],shardingCount={},shardingItem={},jobParams", new Object[]{jobName,
                shardingContext.getShardingTotalCount(), shardingContext.getShardingItem(), shardingContext.getJobParameter()});
        StopWatch stopWatch = new StopWatch("运行job[" + jobName + "]耗时统计");
        stopWatch.start();
        try {
            doExecute(shardingContext);
        } catch (ElasticExecuteError e) {
            logger.error("运行job[" + jobName + "]失败", e);
        } finally {
            stopWatch.stop();
            logger.info("[ElasticBaseJob]{}", stopWatch.prettyPrint());
        }
    }

    protected JSONObject parseJobParam(String jobParameter) {
        if (StringUtils.isEmpty(jobParameter)) {
            return null;
        }

        JSONObject paramsJSON = JSON.parseObject(jobParameter);
        return paramsJSON;
    }

    public abstract void doExecute(ShardingContext shardingContext) throws ElasticExecuteError;
}
