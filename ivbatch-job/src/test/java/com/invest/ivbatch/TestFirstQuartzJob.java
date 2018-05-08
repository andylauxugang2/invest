package com.invest.ivbatch;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by xugang on 2017/8/4.
 */
@RunWith(SpringJUnit4ClassRunner.class) // 使用junit4进行测试
@ContextConfiguration({"classpath:ivbatch-job-quartz.xml"}) // 加载配置文件
public class TestFirstQuartzJob {

    @Test
    public void testCvsFileJob() throws InterruptedException {
        System.out.println("开始跑第一个job");
        String springConfig = "/ivbatch-job-quartz.xml";

        ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

        Thread.sleep(100000);
    }
}
