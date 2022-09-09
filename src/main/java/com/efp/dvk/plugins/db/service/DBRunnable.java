package com.efp.dvk.plugins.db.service;

import com.efp.dvk.plugins.db.model.DbConnectParam;
import com.intellij.openapi.project.Project;

@FunctionalInterface
public interface DBRunnable {
    abstract void run(Project project, DbConnectParam dbConnectParam);
}