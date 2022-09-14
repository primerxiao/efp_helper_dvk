package com.efp.dvk.plugins.db.service;

import com.efp.dvk.common.util.NotifyUtils;
import com.efp.dvk.plugins.db.entity.Columns;
import com.efp.dvk.plugins.db.entity.Schemata;
import com.efp.dvk.plugins.db.entity.Tables;
import com.efp.dvk.plugins.db.model.DbConnectParam;
import com.intellij.openapi.components.Service;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

@Service(Service.Level.PROJECT)
public final class DbService {

    private final QueryRunner queryRunner = new QueryRunner();

    //开启下划线->驼峰转换所用
    private final BeanProcessor beanProcessor = new GenerousBeanProcessor();

    private final RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);

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
        } catch (Exception e) {
            NotifyUtils.error(null, e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public List<Tables> fetchTablses(DbConnectParam dbConnectParam) {
        try {
            Class.forName(dbConnectParam.getDriverClass());
            try (Connection conn = getConnection(dbConnectParam)) {
                String selectTablesSql = """
                        select * from TABLES
                        where
                        TABLE_TYPE='BASE TABLE';
                        """;
                return queryRunner.query(conn, selectTablesSql,
                        new BeanListHandler<>(Tables.class, rowProcessor));
            }
        } catch (Exception e) {
            NotifyUtils.error(null, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
