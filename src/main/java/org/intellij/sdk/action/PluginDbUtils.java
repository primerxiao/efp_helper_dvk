package org.intellij.sdk.action;

import java.sql.*;

public class PluginDbUtils {

    public static String getTableComment(String driverClassName,
                                         String url,
                                         String name,
                                         String password,
                                         String tableName,
                                         String schemaName) {
        String comment = "";
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url,
                    name,
                    password);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql = "select TABLE_COMMENT\n" +
                    "from TABLES\n" +
                    "where TABLE_SCHEMA = '" + schemaName + "'\n" +
                    "  and TABLE_NAME = '" + tableName + "'\n" +
                    "  and TABLE_TYPE = 'BASE TABLE';";

            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                comment = rs.getString("TABLE_COMMENT");
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
            return comment;
        } catch (Exception se) {
            // 处理 JDBC 错误
            return comment;
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
