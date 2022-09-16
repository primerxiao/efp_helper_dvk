package com.efp.dvk.plugins.test.action;

import com.efp.dvk.plugins.db.model.DbRunEvent;
import com.efp.dvk.plugins.db.service.DBRunnable;
import com.efp.dvk.plugins.generator.ui.GeneratorMainUI;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class TestAction extends AnAction implements DBRunnable {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
//        new DbConfUI(e.getProject(),
//                true,
//                "",
//                this,
//                true).show();

        new GeneratorMainUI(e.getProject(),true).show();
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
