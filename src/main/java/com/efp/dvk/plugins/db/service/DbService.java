package com.efp.dvk.plugins.db.service;

import com.efp.dvk.common.util.NotifyUtils;
import com.efp.dvk.plugins.db.entity.Tables;
import com.efp.dvk.plugins.db.model.DbConnectParam;
import com.efp.dvk.plugins.db.model.DbType;
import com.efp.dvk.plugins.generator.model.DatabaseConfig;
import com.intellij.database.util.DbUtil;
import com.intellij.openapi.components.Service;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service(Service.Level.PROJECT)
public final class DbService {

    private static final Map<DbType, Driver> drivers = new HashMap<>();

    private static final int DB_CONNECTION_TIMEOUTS_SECONDS = 1;

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

    public void testConnection(DatabaseConfig config) {
        if (config == null) {
            return;
        }
        if (StringUtils.isAnyEmpty(config.getName(),
                config.getHost(),
                config.getPort(),
                config.getUsername(),
                config.getEncoding(),
                config.getSchema())) {
            NotifyUtils.warn(null, "密码以外其他字段必填");
            return;
        }

        try {
            try (Connection connection = getConnection(config);) {
                NotifyUtils.info(null, "连接成功");
            }
        } catch (RuntimeException e) {
            NotifyUtils.warn(null, "连接失败, " + e.getMessage());
        } catch (Exception e) {
            NotifyUtils.warn(null, "连接失败");
        }
    }

    public Connection getConnection(DatabaseConfig config) throws ClassNotFoundException, SQLException {
        if (drivers.get(config.getDbType()) == null) {
            loadDbDriver(config.getDbType());
        }

        String url = getConnectionUrlWithSchema(config);
        Properties props = new Properties();

        //$NON-NLS-1$
        props.setProperty("user", config.getUsername());
        //$NON-NLS-1$
        props.setProperty("password", config.getPassword());

        DriverManager.setLoginTimeout(DB_CONNECTION_TIMEOUTS_SECONDS);
        return drivers.get(config.getDbType()).connect(url, props);
    }

    public String getConnectionUrlWithSchema(DatabaseConfig dbConfig) throws ClassNotFoundException {

        return String.format(dbConfig.getDbType().getConnectionUrlPattern(),
                dbConfig.getHost(), dbConfig.getPort(), dbConfig.getSchema(), dbConfig.getEncoding());
    }


    private void loadDbDriver(DbType dbType) {
        try {
            Class<?> clazz = Class.forName(dbType.getDriverClass());
            Driver driver = (Driver) clazz.newInstance();
            drivers.put(dbType, driver);
        } catch (Exception e) {
            throw new RuntimeException("找不到" + dbType.getDriverClass() + "驱动");
        }
    }
}
