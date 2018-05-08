package com.invest.ivbatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // 使用junit4进行测试
@ContextConfiguration({"classpath:spring-ivbatch-elasticjob-test.xml"}) // 加载配置文件
public class TestFirstElasticJob {
    protected Logger logger = LoggerFactory.getLogger(TestFirstElasticJob.class);

    @Test
    public void testFirst() throws InterruptedException {
        System.out.println("ok");
        Thread.sleep(1000000);
    }
}
