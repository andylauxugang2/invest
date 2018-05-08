package com.invest.ivbatch.job.elastic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

@Component("jobLauncherDetails")
public class JobLauncherDetails implements SimpleJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String JOB_NAME = "jobName";

    //需要显示注入
    private JobLocator jobLocator;
    private JobLauncher jobLauncher;

    public void setJobLocator(JobLocator jobLocator) {
        this.jobLocator = jobLocator;
    }

    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @Override
    public void execute(ShardingContext shardingContext) {
        int shardingCount = shardingContext.getShardingTotalCount();
        int shardingValue = shardingContext.getShardingItem();

        logger.info("shardingCount=" + shardingCount + ",shardingValue=" + shardingValue);

        String jobParameter = shardingContext.getJobParameter();
        JSONObject paramsJSON = parseAndValidateParam(jobParameter);
        if(paramsJSON == null){
            logger.error("job运行失败,jobParameter不合法");
            return;
        }

        String jobName = (String) paramsJSON.get(JOB_NAME);
        JobParameters jobParameters = getJobParametersFromJobMap(paramsJSON);
        logger.info("开始定时任务：" + jobName + "\t" + LocalDateTime.now() + "\t params:" + jobParameters.toString());

        try {
            jobLauncher.run(jobLocator.getJob(jobName), jobParameters);
        } catch (Exception e) {
            logger.error("定时任务调度运行异常,jobName=" + jobName, e);
        }
        logger.info("结束定时任务：" + jobName + "\t" + LocalDateTime.now());

    }


    //get params from jobDataAsMap property
    private JobParameters getJobParametersFromJobMap(Map<String, Object> jobData) {

        JobParametersBuilder builder = new JobParametersBuilder();

        for (Entry<String, Object> entry : jobData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String && !key.equals(JOB_NAME)) {
                builder.addString(key, (String) value);
            } else if (value instanceof Float || value instanceof Double) {
                builder.addDouble(key, ((Number) value).doubleValue());
            } else if (value instanceof Integer || value instanceof Long) {
                builder.addLong(key, ((Number) value).longValue());
            } else if (value instanceof Date) {
                builder.addDate(key, (Date) value);
            } else {
                // JobDataMap contains values which are not job parameters
                // (ignoring)
            }
        }

        //need unique job parameter to rerun the same job
        builder.addDate("run date", new Date());

        return builder.toJobParameters();
    }

    protected JSONObject parseAndValidateParam(String jobParameter) {
        if (StringUtils.isEmpty(jobParameter)) {
            logger.error(JOB_NAME + " job param must set");
            return null;
        }

        JSONObject paramsJSON = JSON.parseObject(jobParameter);
        if (!paramsJSON.containsKey(JOB_NAME)) {
            logger.error(JOB_NAME + " job param must set");
            return null;
        }

        return paramsJSON;
    }
}