package com.efp.dvk.common.service;

import com.efp.dvk.common.lang.annation.Exclude;
import com.efp.dvk.common.lang.annation.Id;
import com.efp.dvk.common.lang.util.StrUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.components.Service;
import com.intellij.util.ReflectionUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service(Service.Level.APP)
public final class PluginOrmService implements IPluginOrmService {

    private static final HikariDataSource hikariDataSource;

    static {
        //获取db文件路径
        String dbFilePath = PathManager.getHomePath() + "/help/plugin.db";
        File file = new File(dbFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName("SQLiteConnectionPool");
        hikariConfig.setDriverClassName("org.sqlite.JDBC");
        hikariConfig.setJdbcUrl("jdbc:sqlite:" + dbFilePath);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setMaximumPoolSize(5);
        hikariDataSource = new HikariDataSource(hikariConfig);
        try {
            initTableOrDate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static PluginOrmService instance() {
        return ApplicationManager.getApplication().getService(PluginOrmService.class);
    }

    private static void initTableOrDate() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(hikariDataSource);
        String pluginDialogConf = """
                create table if not exists plugin_dialog_conf(
                    conf_key text primary key,
                    conf_value text
                );
                """;
        queryRunner.update(pluginDialogConf);

    }

    private final QueryRunner queryRunner = new QueryRunner(hikariDataSource);

    private final BasicRowProcessor basicRowProcessor = new BasicRowProcessor(new GenerousBeanProcessor());

    @Override
    public <T> T selectById(T t) {
        try {
            Class<?> tClass = t.getClass();
            String tableName = getTableNameByClass(tClass);
            Field field = Arrays.stream(tClass.getDeclaredFields())
                    .filter(f -> f.getAnnotation(Id.class) != null)
                    .findFirst().orElse(null);
            if (field == null) {
                throw new RuntimeException("not find id column");
            }
            String idFieldName = field.getName();
            String idColumnName = StrUtils.convertLineToHump(idFieldName);
            field.setAccessible(true);
            Object idValue = field.get(t);
            String sql = """
                    select * from %s
                    where
                    %s = ?
                    """;
            String formattedSql = sql.formatted(tableName, idColumnName);
            Object query = queryRunner.query(formattedSql,
                    new BeanHandler<>(tClass, basicRowProcessor),
                    idValue);
            return (T) query;
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> int insert(T t) {
        try {
            Class<?> tClass = t.getClass();
            String tableName = getTableNameByClass(tClass);
            List<Field> fields = ReflectionUtil.collectFields(tClass)
                    .stream()
                    .filter(f -> f.getAnnotation(Exclude.class) == null).toList();
            List<String> paramNameBuilder = new ArrayList<>();
            List<String> paramValueBuilder = new ArrayList<String>();
            List<Object> params = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                Object fieldVal = field.get(t);
                if (fieldVal == null) {
                    continue;
                }
                paramNameBuilder.add(StrUtils.convertLineToHump(field.getName()));
                paramValueBuilder.add("?");
                params.add(fieldVal);
            }
            String sql = """
                    insert into %s(%s)
                    values(%s);
                    """;
            String formattedSql = sql.formatted(
                    tableName,
                    String.join(",", paramNameBuilder),
                    String.join(",", paramValueBuilder));
            return queryRunner.update(formattedSql,
                    params);
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> int deleteById(T t) {
        try {
            Class<?> tClass = t.getClass();
            String tableName = getTableNameByClass(tClass);
            Field idField = ReflectionUtil.collectFields(tClass)
                    .stream()
                    .filter(f -> f.getAnnotation(Id.class) != null).findAny().orElse(null);
            if (idField == null) {
                throw new RuntimeException("id field not found");
            }
            idField.setAccessible(true);
            Object idValue = idField.get(t);
            if (idValue == null) {
                throw new RuntimeException("id value is null");
            }
            String sql = """
                    delete from %s where %s=?;
                    """;
            String formattedSql = sql.formatted(
                    tableName,
                    getColumnNameByField(idField));
            return queryRunner.update(formattedSql, idValue);
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> selectAll(Class<T> tClass) {
        try {
            String tableName = getTableNameByClass(tClass);
            String sql = """
                    select * from %s;
                    """;
            String formattedSql = sql.formatted(tableName);
            List<Object> querys = queryRunner.query(formattedSql,
                    new BeanListHandler<>(tClass, basicRowProcessor));
            return (List<T>) querys;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
