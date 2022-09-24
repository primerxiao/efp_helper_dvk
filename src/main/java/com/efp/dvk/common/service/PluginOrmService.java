package com.efp.dvk.common.service;

import com.efp.dvk.common.orm.dao.PluginDialogConfDao;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.PluginUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.extensions.PluginId;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.fastsql.core.DefaultObjectFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Objects;

@Service(Service.Level.APP)
public final class PluginOrmService {

    private static final HikariDataSource hikariDataSource;

    //定义工厂
    private static final DefaultObjectFactory factory = new DefaultObjectFactory();

    static {
        //获取db文件路径
        String dbFilePath = PathManager.getHomePath() + "/help/plugin.db";
        File file = new File(dbFilePath);
        if (!file.exists()) {
            copyDbfile(dbFilePath);
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
        //设置数据源
        factory.setDataSource(hikariDataSource);
        //注册dao
    }

    public static PluginOrmService instance() {
        return ApplicationManager.getApplication().getService(PluginOrmService.class);
    }

    public PluginDialogConfDao pluginDialogConfDao() {
        return factory.getBean(PluginDialogConfDao.class);
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

    private static void copyDbfile(String dbFilePath) {
        PluginId pluginId = PluginUtil.getInstance().findPluginId(new Throwable());
        assert pluginId != null;
        IdeaPluginDescriptor enabledPlugin = PluginManager.getInstance().findEnabledPlugin(pluginId);
        assert enabledPlugin != null;
        try (InputStream inputStream = Objects.requireNonNull(Objects.requireNonNull(enabledPlugin.getPluginClassLoader())).getResourceAsStream("/db/plugin.db")) {
            assert inputStream != null;
            IOUtil.copyCompletely(inputStream, new FileOutputStream(dbFilePath));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
