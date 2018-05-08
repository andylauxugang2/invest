package com.zhuobao.ivbid;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhuobao.ivbid.model.entity.UserPolicy;
import org.h2.tools.Server;

import java.sql.*;
import java.util.List;

/**
 * Created by xugang on 2017/8/7.
 */
public class TestH2 {

    private static Server server;
    private static String port = "8082";
    private static String sourceURL1 = "jdbc:h2:mem:h2db";
    private static String sourceURL2 = "jdbc:h2:tcp://localhost:8082/mem:h2db";
    private static String user = "sa";
    private static String password = "";

    public static void main2(String[] a) throws Exception {
//        System.out.println("正在启动h2...");
//        server = Server.createTcpServer(new String[]{"-tcpPort", port}).start();

        Class.forName("org.h2.Driver");
//        Connection conn = DriverManager.getConnection("jdbc:h2:mem:h2db", "sa", "");
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:h2dbtest;INIT=create schema if not exists h2dbtest", "root", "root");
//        Connection conn = DriverManager.getConnection(sourceURL2, "sa", "");
        // add application code here
        Statement stmt = conn.createStatement();
//        stmt.execute("create table t_user_policy(`id` bigint(20) NOT NULL auto_increment,PRIMARY KEY  (`id`)) CHARSET=utf8;");
        stmt.execute("CREATE TABLE `t_user_policy` (\n" +
                "  `id` bigint(20) NOT NULL,\n" +
                "  `type` bit(15) NOT NULL,\n" +
                "  `valid_time` datetime NOT NULL,\n" +
                "  `user_id` bigint(20) NOT NULL COMMENT '用户ID',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") CHARSET=utf8;");
        stmt.execute("insert into t_user_policy(id,type,valid_time,user_id) values(1212,1,now(),1)");
        ResultSet rs = stmt.executeQuery("SELECT * FROM t_user_policy ");
        while (rs.next()) {
            System.out.println(rs.getInt("ID"));
        }
        conn.close();

        Thread.sleep(1000000);
    }

    public static void main(String[] args) throws Exception {
        Class.forName("org.h2.Driver");
//        Connection conn = DriverManager.getConnection("jdbc:h2:mem:h2db", "sa", "");
        Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:h2db;MODE=MySQL", "root", "Muxs9^sdf");
        Statement stmt = conn.createStatement();
        String sql = "select username,min(rn) from (\n" +
                "select ROWNUM() as rn,id,type,user_id,username,bid_amount,policy_id \n" +
                "from t_user_policy\n" +
                "where 1=1 and rate_begin > 10\n" +
                "order by user_id,username,type desc ) as a\n" +
                "group by username";

//        String sql = "select * from t_user_policy ";
//        String sql = "select id from t_user_policy order by id desc limit 1 ";

        ResultSet rs = stmt.executeQuery(sql);

        // 获取列名
        ResultSetMetaData metaData = rs.getMetaData();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            // resultSet数据下标从1开始
            String columnName = metaData.getColumnName(i + 1);
            int type = metaData.getColumnType(i + 1);
            if (Types.INTEGER == type) {
                // int
            } else if (Types.VARCHAR == type) {
                // String
            }
            System.out.print(columnName.toLowerCase() + "\t\t\t\t");
        }
        System.out.println();
        // 获取数据
        while (rs.next()) {
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i + 1);
                // resultSet数据下标从1开始
                System.out.println(columnName.toLowerCase() + ":" + rs.getString(i + 1));
            }
            System.out.println();

        }


        conn.close();
    }

    public static void main3(String[] args) {
        String json = "[{\"ageBegin\":19,\"ageEnd\":43,\"amountBegin\":400,\"amountEnd\":1000,\"amountToReceiveBegin\":0,\"amountToReceiveEnd\":100000000,\"bidAmount\":53,\"certificate\":30,\"creditCode\":0,\"graduateSchoolType\":4160749568,\"id\":1,\"lastSuccessBorrowDaysBegin\":0,\"lastSuccessBorrowDaysEnd\":10000,\"loanerSuccessCountBegin\":0,\"loanerSuccessCountEnd\":1,\"monthBegin\":3,\"monthEnd\":12,\"normalCountBegin\":4,\"normalCountEnd\":5,\"overdueLessCountBegin\":0,\"overdueLessCountEnd\":0,\"overdueMoreCountBegin\":0,\"overdueMoreCountEnd\":0,\"owingPrincipalBegin\":0,\"owingPrincipalEnd\":0,\"policyId\":9,\"rateBegin\":20,\"rateEnd\":24,\"studyStyle\":0,\"thirdAuthInfo\":262144,\"totalPrincipalBegin\":0,\"totalPrincipalEnd\":100000000,\"type\":2,\"userId\":1,\"username\":\"lixiangnan1\",\"wasteCountBegin\":0,\"wasteCountEnd\":10000},{\"ageBegin\":20,\"ageEnd\":43,\"amountBegin\":500,\"amountEnd\":10000,\"amountToReceiveBegin\":0,\"amountToReceiveEnd\":100000000,\"bidAmount\":53,\"certificate\":0,\"creditCode\":0,\"graduateSchoolType\":0,\"id\":2,\"lastSuccessBorrowDaysBegin\":0,\"lastSuccessBorrowDaysEnd\":10000,\"loanerSuccessCountBegin\":0,\"loanerSuccessCountEnd\":2,\"monthBegin\":3,\"monthEnd\":12,\"normalCountBegin\":24,\"normalCountEnd\":25,\"overdueLessCountBegin\":0,\"overdueLessCountEnd\":1,\"overdueMoreCountBegin\":0,\"overdueMoreCountEnd\":0,\"owingPrincipalBegin\":0,\"owingPrincipalEnd\":0,\"policyId\":11,\"rateBegin\":20,\"rateEnd\":24,\"studyStyle\":0,\"thirdAuthInfo\":0,\"totalPrincipalBegin\":0,\"totalPrincipalEnd\":100000000,\"type\":2,\"userId\":1,\"username\":\"lixiangnan1\",\"wasteCountBegin\":0,\"wasteCountEnd\":10000},{\"ageBegin\":17,\"ageEnd\":40,\"amountBegin\":500,\"amountEnd\":3000,\"amountToReceiveBegin\":0,\"amountToReceiveEnd\":100000000,\"bidAmount\":55,\"certificate\":0,\"creditCode\":8160,\"graduateSchoolType\":0,\"id\":3,\"lastSuccessBorrowDaysBegin\":0,\"lastSuccessBorrowDaysEnd\":10000,\"loanerSuccessCountBegin\":0,\"loanerSuccessCountEnd\":1,\"monthBegin\":3,\"monthEnd\":12,\"normalCountBegin\":5,\"normalCountEnd\":6,\"overdueLessCountBegin\":0,\"overdueLessCountEnd\":0,\"overdueMoreCountBegin\":0,\"overdueMoreCountEnd\":0,\"owingPrincipalBegin\":0,\"owingPrincipalEnd\":0,\"policyId\":6,\"rateBegin\":20,\"rateEnd\":24,\"studyStyle\":0,\"thirdAuthInfo\":0,\"totalPrincipalBegin\":0,\"totalPrincipalEnd\":100000000,\"type\":2,\"userId\":1,\"username\":\"lixiangnan1\",\"wasteCountBegin\":0,\"wasteCountEnd\":10000},{\"ageBegin\":17,\"ageEnd\":29,\"amountBegin\":400,\"amountEnd\":3000,\"amountToReceiveBegin\":0,\"amountToReceiveEnd\":100000000,\"bidAmount\":53,\"certificate\":31,\"creditCode\":8160,\"graduateSchoolType\":7381975040,\"id\":4,\"lastSuccessBorrowDaysBegin\":0,\"lastSuccessBorrowDaysEnd\":10000,\"loanerSuccessCountBegin\":0,\"loanerSuccessCountEnd\":1,\"monthBegin\":3,\"monthEnd\":12,\"normalCountBegin\":0,\"normalCountEnd\":10000,\"overdueLessCountBegin\":0,\"overdueLessCountEnd\":0,\"overdueMoreCountBegin\":0,\"overdueMoreCountEnd\":0,\"owingPrincipalBegin\":0,\"owingPrincipalEnd\":0,\"policyId\":12,\"rateBegin\":20,\"rateEnd\":22,\"studyStyle\":0,\"thirdAuthInfo\":8388608,\"totalPrincipalBegin\":0,\"totalPrincipalEnd\":100000000,\"type\":2,\"userId\":1,\"username\":\"lixiangnan1\",\"wasteCountBegin\":0,\"wasteCountEnd\":10000},{\"ageBegin\":17,\"ageEnd\":43,\"amountBegin\":400,\"amountEnd\":3000,\"amountToReceiveBegin\":0,\"amountToReceiveEnd\":100000000,\"bidAmount\":53,\"certificate\":31,\"creditCode\":0,\"graduateSchoolType\":8455716864,\"id\":5,\"lastSuccessBorrowDaysBegin\":0,\"lastSuccessBorrowDaysEnd\":10000,\"loanerSuccessCountBegin\":0,\"loanerSuccessCountEnd\":1,\"monthBegin\":3,\"monthEnd\":12,\"normalCountBegin\":0,\"normalCountEnd\":10000,\"overdueLessCountBegin\":0,\"overdueLessCountEnd\":0,\"overdueMoreCountBegin\":0,\"overdueMoreCountEnd\":0,\"owingPrincipalBegin\":0,\"owingPrincipalEnd\":0,\"policyId\":10,\"rateBegin\":20,\"rateEnd\":24,\"studyStyle\":0,\"thirdAuthInfo\":262144,\"totalPrincipalBegin\":0,\"totalPrincipalEnd\":100000000,\"type\":2,\"userId\":1,\"username\":\"lixiangnan1\",\"wasteCountBegin\":0,\"wasteCountEnd\":10000},{\"ageBegin\":17,\"ageEnd\":29,\"amountBegin\":500,\"amountEnd\":3000,\"amountToReceiveBegin\":0,\"amountToReceiveEnd\":100000000,\"bidAmount\":55,\"certificate\":31,\"creditCode\":8160,\"graduateSchoolType\":8455716864,\"id\":6,\"lastSuccessBorrowDaysBegin\":0,\"lastSuccessBorrowDaysEnd\":10000,\"loanerSuccessCountBegin\":0,\"loanerSuccessCountEnd\":0,\"monthBegin\":3,\"monthEnd\":12,\"normalCountBegin\":0,\"normalCountEnd\":10000,\"overdueLessCountBegin\":0,\"overdueLessCountEnd\":10000,\"overdueMoreCountBegin\":0,\"overdueMoreCountEnd\":10000,\"owingPrincipalBegin\":0,\"owingPrincipalEnd\":100000000,\"policyId\":8,\"rateBegin\":20,\"rateEnd\":24,\"studyStyle\":0,\"thirdAuthInfo\":8388608,\"totalPrincipalBegin\":0,\"totalPrincipalEnd\":100000000,\"type\":2,\"userId\":1,\"username\":\"lixiangnan1\",\"wasteCountBegin\":0,\"wasteCountEnd\":10000}]";
        List<UserPolicy> list = JSON.parseArray(json, UserPolicy.class);
        for(UserPolicy userPolicy : list){
            System.out.println(JSONObject.toJSONString(userPolicy));
        }
    }


}

