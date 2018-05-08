package com.zhuobao.ivbid.dao.h2;

import org.apache.commons.io.output.NullWriter;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;

/**
 * 初始化库表结构
 * Created by xugang on 2017/8/7.
 */
@Component
public class H2DaoInitBean implements InitializingBean {
    public static Logger logger = LoggerFactory.getLogger(H2DaoInitBean.class);

    private static final String H2DBINIT_SQL_PATH = "ivbid.config/h2dbinit.sql";
    @Resource
    private DataSource ivbidH2DataSource;

    @Override
    public void afterPropertiesSet() throws Exception {
        Connection conn = ivbidH2DataSource.getConnection();
        createSchema(conn);
    }

    public void createSchema(Connection connection) {
        executeScript(connection, H2DBINIT_SQL_PATH);
        logger.info("初始化数据表结构[{}]成功", H2DBINIT_SQL_PATH);
    }

    public void executeScript(Connection connection, String path) {
        ScriptRunner scriptRunner = newScriptRunner(connection);
        try {
            scriptRunner.runScript(Resources.getResourceAsReader(path));
            connection.commit();
        } catch (Exception e) {
            throw new IllegalStateException("Fail to restore: " + path, e);
        }
    }

    private ScriptRunner newScriptRunner(Connection connection) {
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setDelimiter(";");
        scriptRunner.setStopOnError(true);
        scriptRunner.setLogWriter(new PrintWriter(new NullWriter()));
        return scriptRunner;
    }
}
