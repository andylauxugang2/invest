package com.zhuobao.ivbid;

import com.alibaba.fastjson.JSONObject;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // 使用junit4进行测试
@ContextConfiguration({"classpath:spring-ivbid-test.xml"}) // 加载配置文件
public class TestBase {
    protected Logger logger = LoggerFactory.getLogger(TestBase.class);

    public void print(Object obj) {
        logger.info(JSONObject.toJSONString(obj));
    }


}
