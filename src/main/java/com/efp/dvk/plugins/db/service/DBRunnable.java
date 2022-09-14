package com.efp.dvk.plugins.db.service;

import com.efp.dvk.plugins.db.model.DbRunEvent;

@FunctionalInterface
public interface DBRunnable {
    abstract void run(DbRunEvent dbRunEvent);
}