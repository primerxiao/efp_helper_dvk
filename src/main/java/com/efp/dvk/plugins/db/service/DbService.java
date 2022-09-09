package com.efp.dvk.plugins.db.service;

import com.efp.dvk.common.util.PluginNotifierUtils;
import com.efp.dvk.plugins.db.model.DbConnectParam;
import com.intellij.openapi.components.Service;

import java.sql.Connection;
import java.sql.DriverManager;

@Service
public class DbService {
    /**
     * 获取数据库连接
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
            PluginNotifierUtils.notifyError(null, exception.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
