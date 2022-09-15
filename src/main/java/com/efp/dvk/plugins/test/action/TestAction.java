package com.efp.dvk.plugins.test.action;

import com.efp.dvk.cache.service.CacheService;
import com.efp.dvk.plugins.db.entity.Tables;
import com.efp.dvk.plugins.db.model.DbConnectParam;
import com.efp.dvk.plugins.db.model.DbRunEvent;
import com.efp.dvk.plugins.db.service.DBRunnable;
import com.efp.dvk.plugins.db.service.DbService;
import com.efp.dvk.plugins.db.ui.DbConfUI;
import com.efp.dvk.plugins.db.ui.SelectTableUI;
import com.efp.dvk.plugins.generator.model.DatabaseConfig;
import com.efp.dvk.plugins.generator.ui.GeneratorMainUI;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class TestAction extends AnAction implements DBRunnable {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
//        new DbConfUI(e.getProject(),
//                true,
//                "",
//                this,
//                true).show();

        CacheService service = e.getProject().getService(CacheService.class);
        DatabaseConfig build = DatabaseConfig.builder()
                .name("test")
                .build();
        service.hashMapSet("test","test",build);
        DatabaseConfig test = service.hashMapGet("test","test");

        System.out.println(test);

        //new GeneratorMainUI(e.getProject(),true).show();
    }

    @Override
    public void run(DbRunEvent dbRunEvent) {
//        DbService service = project.getService(DbService.class);
//        Connection connection = service.getConnection(dbConnectParam);
//        try {
//            connection.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        new SelectTableUI(dbRunEvent.getProject(), true, dbRunEvent.getDbConnectParam(),
//                dbRunEvent1 -> {
//                    System.out.println(dbRunEvent1.getSelectTables());
//                }
//        ).show();

    }
}
