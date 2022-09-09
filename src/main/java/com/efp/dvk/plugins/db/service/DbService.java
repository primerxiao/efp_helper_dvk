package com.efp.dvk.plugins.db.service;

import com.efp.dvk.common.util.PluginNotifierUtils;
import com.efp.dvk.plugins.db.model.DbConnectParam;
import com.intellij.openapi.components.Service;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.DriverManager;

@Service
public class DbService {
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
