package com.efp.dvk.plugins.db.service;

import com.efp.dvk.common.util.NotifyUtils;
import com.efp.dvk.plugins.db.model.DbConnectParam;
import com.intellij.openapi.components.Service;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

@Service(Service.Level.PROJECT)
public final class DbService {
    /**
     * 获取数据库连接
     *
     * @param dbConnectParam 连接参数
     * @return Connection
     */
    public Connection getConnection(DbConnectParam dbConnectParam) {
        try {
            Class.forName(dbConnectParam.getDriverClass());
            return DriverManager.getConnection(dbConnectParam.getConnectUrl(),
                    dbConnectParam.getUserName(),
                    dbConnectParam.getPassword());
        } catch (Exception exception) {
            NotifyUtils.error(null, exception.getMessage());
            throw new RuntimeException(exception);
        }
    }


    public List<String> getSchemas(DbConnectParam dbConnectParam) {
        String selectSql = """
                select TABLE_SCHEMA
                from TABLES
                group by TABLE_SCHEMA;""";
        try {
            Class.forName(dbConnectParam.getDriverClass());
            try (Connection conn = getConnection(dbConnectParam)) {
                QueryRunner queryRunner = new QueryRunner();
                return queryRunner.query(conn, selectSql, new ColumnListHandler<>("TABLE_SCHEMA"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
