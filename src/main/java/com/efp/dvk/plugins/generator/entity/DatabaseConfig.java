package com.efp.dvk.plugins.generator.entity;

import com.efp.dvk.common.lang.annation.Id;
import com.efp.dvk.plugins.db.model.DbType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseConfig implements Serializable {

    /**
     * 配置名称
     */
    @Id
    private String name;

    /**
     * 数据库类型
     */
    private String dbTypeName;

    /**
     * 主机
     */
    private String host;

    /**
     * 端口
     */
    private String port;

    /**
     * 数据库
     */
    private String schema;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 编码
     */
    private String encoding;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatabaseConfig that = (DatabaseConfig) o;
        return Objects.equals(dbTypeName, that.dbTypeName) &&
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
                dbTypeName,
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
        return getName();
    }

    public DbType getDbTypeEnum(){
        return DbType.valueOf(getDbTypeName());
    }
}
