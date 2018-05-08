package com.invest.ivbatch.job.quartz;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

@Component("jobLauncherQuartzDetails")
public class JobLauncherDetails extends QuartzJobBean {
    private Logger logger = LoggerFactory.getLogger(JobLauncherDetails.class);

    private static final String JOB_NAME = "jobName";

    private JobLocator jobLocator;
    private JobLauncher jobLauncher;

    public void setJobLocator(JobLocator jobLocator) {
        this.jobLocator = jobLocator;
    }

    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {
        Map<String, Object> jobDataMap = context.getMergedJobDataMap();

        String jobName = (String) jobDataMap.get(JOB_NAME);
        if (StringUtils.isEmpty(jobName)) {
            logger.equals("jobName配置为空错误");
            return;
        }

        JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);

        logger.info("开始定时任务：" + jobName + "\t" + LocalDateTime.now() + "\t params:" + jobParameters.toString());
        try {
            jobLauncher.run(jobLocator.getJob(jobName), jobParameters);
        } catch (Exception e) {
            logger.error("定时任务调度运行异常,jobName=" + jobName, e);
        }
        logger.info("结束定时任务：" + jobName + "\t" + LocalDateTime.now());
    }

    //get params from jobDataAsMap property
    private JobParameters getJobParametersFromJobMap(Map<String, Object> jobDataMap) {

        JobParametersBuilder builder = new JobParametersBuilder();

        for (Entry<String, Object> entry : jobDataMap.entrySet()) {
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

}