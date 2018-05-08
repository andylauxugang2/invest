package com.invest.ivbatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xugang on 2017/8/4.
 */
@RunWith(SpringJUnit4ClassRunner.class) // 使用junit4进行测试
@ContextConfiguration({"classpath:spring-ivbatch-job-test.xml"}) // 加载配置文件
public class TestFirstStep extends TestFirstElasticJob {

    @Resource
    private Job testJob;

    @Autowired
    protected JobLauncher jobLauncher;

    public void runJob(Job job, JobParameters jobParameters) {
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCvsFileJob() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        JobParameters jobParameters = new JobParametersBuilder().addString("batchId", "TRADIT_POLICY_TO_BIS_" + sdf.format(now)).toJobParameters();
        runJob(testJob, jobParameters);
        runJob(testJob, jobParameters);
    }

    @Test
    public void testCvsFileJob_相同参数和名成JOB只插入一个DB行() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        Date now = new Date();
        JobParameters jobParameters = new JobParametersBuilder().addString("batchId", "TRADIT_POLICY_TO_BIS_" + sdf.format(now)).toJobParameters();
        //runJob(testJob, jobParameters);

        //Spring Batch需要每次唯一的作业参数来执行。 如果 job 参数 设置一样,同时跑2个job 会报错
        // A job instance already exists and is complete for parameters={batchId=TRADIT_POLICY_TO_BIS_201708041521}.  If you want to run this job again, change the parameters.
        // runJob(testJob, jobParameters);

        Map<String, JobParameter> parameterMap = new LinkedHashMap<>();
        parameterMap.put("userId", new JobParameter(8110332L, true));
        parameterMap.put("nick", new JobParameter("zhangsan", true));
        parameterMap.put("birthday", new JobParameter(new Date(), true));
        jobParameters = new JobParameters(parameterMap);
        runJob(testJob, jobParameters);

        //当修改report.cvs文件行数后，batch_step_execution表version字段升级，batch_job_execution表version不变
    }

    @Test
    public void testCvsFileJob_相同参数和名成JOB可重复执行() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        JobParameters jobParameters = new JobParametersBuilder().addString("batchId", "TRADIT_POLICY_TO_BIS_" + sdf.format(now)).toJobParameters();
        runJob(testJob, jobParameters);
    }
}
