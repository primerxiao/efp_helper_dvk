package com.efp.dvk.common.service;

import com.efp.dvk.common.lang.enums.CacheNameEnum;
import com.ejlchina.searcher.BeanSearcher;
import com.ejlchina.searcher.MapSearcher;
import com.ejlchina.searcher.SearcherBuilder;
import com.ejlchina.searcher.implement.DefaultSqlExecutor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.components.Service;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.util.HashMap;

/**
 * 存储处理服务
 */
@Service(Service.Level.APP)
public final class CacheService {

    static {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName("SQLiteConnectionPool");
        hikariConfig.setDriverClassName("org.sqlite.JDBC");
        hikariConfig.setJdbcUrl("jdbc:sqlite:" + "d://sqlite.db");
        hikariDataSource = new HikariDataSource(hikariConfig);

    }

    private static final HikariDataSource hikariDataSource;

    private final static DefaultSqlExecutor sqlExecutor = new DefaultSqlExecutor(hikariDataSource);

    // 构建 Map 检索器
    private final MapSearcher mapSearcher = SearcherBuilder.mapSearcher()
            .sqlExecutor(sqlExecutor)
            .build();

    // 构建 Bean 检索器
    public final BeanSearcher beanSearcher = SearcherBuilder.beanSearcher()
            .sqlExecutor(sqlExecutor)
            .build();

    private final String dbFile = String.join(PathManager.getHomePath(), "/help/efp_dvk_cache.db");

    public static CacheService instance() {
        return ApplicationManager.getApplication().getService(CacheService.class);
    }

    public synchronized <K, V> void hashMapSet(CacheNameEnum name, K key, V value) {
        try (DB db = getDb()) {
            db.hashMap(name.name(),
                            Serializer.JAVA,
                            Serializer.JAVA)
                    .createOrOpen()
                    .put(key, value);
        }
    }

    public <K, V> V hashMapGet(CacheNameEnum name, K key) {
        try (DB db = getDb()) {
            return (V) db.hashMap(name.name(),
                            Serializer.JAVA,
                            Serializer.JAVA)
                    .createOrOpen()
                    .get(key);
        }
    }

    public <K, V> HashMap<K, V> hashMapGet(CacheNameEnum name) {
        try (DB db = getDb()) {
            return new HashMap<>(db.hashMap(name.name(),
                            Serializer.JAVA,
                            Serializer.JAVA)
                    .createOrOpen());
        }
    }

    private DB getDb() {
        return DBMaker.fileDB(dbFile)
                .closeOnJvmShutdown()
                .fileMmapEnable()
                .checksumHeaderBypass()
                .fileMmapPreclearDisable()
                .make();
    }

}
