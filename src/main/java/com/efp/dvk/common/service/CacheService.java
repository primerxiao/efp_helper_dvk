package com.efp.dvk.common.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.components.Service;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.HashMap;

@Service(Service.Level.APP)
public final class CacheService {

    private final String dbFile = PathManager.getHomePath() + "/help/efp_dvk_cache.db";

    public static CacheService instance() {
        return ApplicationManager.getApplication().getService(CacheService.class);
    }

    public synchronized <K, V> void hashMapSet(NameEnum name, K key, V value) {
        try (DB db = getDb()) {
            db.hashMap(name.name(),
                            Serializer.JAVA,
                            Serializer.JAVA)
                    .createOrOpen()
                    .put(key, value);
        }
    }

    public <K, V> V hashMapGet(NameEnum name, K key) {
        try (DB db = getDb()) {
            return (V) db.hashMap(name.name(),
                            Serializer.JAVA,
                            Serializer.JAVA)
                    .createOrOpen()
                    .get(key);
        }
    }

    public <K, V> HashMap<K, V> hashMapGet(NameEnum name) {
        try (DB db = getDb()) {
            return new HashMap<>(db.hashMap(name.name(),
                            Serializer.JAVA,
                            Serializer.JAVA)
                    .createOrOpen());
        }
    }

    private DB getDb() {
        return DBMaker.fileDB(dbFile)
                .fileMmapEnable()
                .checksumHeaderBypass()
                .fileMmapPreclearDisable()
                .make();
    }

    public static enum NameEnum {
        DatabaseConfig,
        DialogConfig;
    }
}
