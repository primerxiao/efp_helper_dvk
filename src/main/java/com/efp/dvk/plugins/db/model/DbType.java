package com.efp.dvk.plugins.db.model;

/**
 * Created by Owen on 6/14/16.
 */
public enum DbType {

    MySQL("com.mysql.jdbc.Driver", "jdbc:mysql://%s:%s/%s?useUnicode=true&useSSL=false&characterEncoding=%s"),
    MySQL_8("com.mysql.cj.jdbc.Driver", "jdbc:mysql://%s:%s/%s?serverTimezone=UTC&useUnicode=true&useSSL=false&characterEncoding=%s"),
    Oracle("oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@//%s:%s/%s"),
    PostgreSQL("org.postgresql.Driver", "jdbc:postgresql://%s:%s/%s"),
    SQL_Server("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://%s:%s;databaseName=%s"),
    Sqlite("org.sqlite.JDBC", "jdbc:sqlite:%s");

    private final String driverClass;
    private final String connectionUrlPattern;

    DbType(String driverClass, String connectionUrlPattern) {
        this.driverClass = driverClass;
        this.connectionUrlPattern = connectionUrlPattern;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public String getConnectionUrlPattern() {
        return connectionUrlPattern;
    }


    @Override
    public String toString() {
        return name();
    }
}