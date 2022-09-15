package com.efp.dvk.cache.service;

import com.efp.dvk.plugins.generator.model.DatabaseConfig;
import com.intellij.openapi.components.Service;
import lombok.Getter;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.concurrent.ConcurrentMap;

@Service(Service.Level.PROJECT)
public final class CacheService {

    public <K, V> void hashMapSet(String name, K key, V value) {
        try (DB db = getDb()) {
            ConcurrentMap<K, V> orOpen = db.hashMap(name,
                            Serializer.JAVA,
                            Serializer.JAVA)
                    .createOrOpen();
            orOpen.put(key, value);
        }
    }

    public <K, V> V hashMapGet(String name, K key) {
        try (DB db = getDb()) {
            ConcurrentMap<K, V> orOpen = db.hashMap(name,
                            Serializer.JAVA,
                            Serializer.JAVA)
                    .createOrOpen();
            return orOpen.get(key);
        }
    }

    private DB getDb() {
        return DBMaker.fileDB("d://file.db")
                .fileMmapEnable()
                .checksumHeaderBypass()
                .fileMmapPreclearDisable()
                .make();
    }
}
