package com.efp.dvk.common.service;

import com.efp.dvk.common.lang.enums.CacheNameEnum;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.components.Service;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.util.HashMap;

/**
 * 存储处理服务
 */
@Service(Service.Level.APP)
public final class CacheService {

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
