package com.efp.dvk.plugins.generator.model;

import com.efp.dvk.plugins.db.model.DbType;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Owen on 5/13/16.
 */
@Data
@Builder
public class DatabaseConfig implements Serializable {

    private DbType dbType;

    private String name;

    private String host;

    private String port;

    private String schema;

    private String username;

    private String password;

    private String encoding;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatabaseConfig that = (DatabaseConfig) o;
        return Objects.equals(dbType, that.dbType) &&
                Objects.equals(name, that.name) &&
                Objects.equals(host, that.host) &&
                Objects.equals(port, that.port) &&
                Objects.equals(schema, that.schema) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(encoding, that.encoding);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                dbType,
                name,
                host,
                port,
                schema,
                username,
                password,
                encoding);
    }

    @Override
    public String toString() {
        return "DatabaseConfig{" +
                ", dbType='" + dbType + '\'' +
                ", name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", schema='" + schema + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", encoding='" + encoding + '\'' +
                '}';
    }
}
