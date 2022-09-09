package com.efp.dvk.plugins.test.action;

import com.efp.dvk.plugins.db.model.DbConnectParam;
import com.efp.dvk.plugins.db.service.DBRunnable;
import com.efp.dvk.plugins.db.service.DbService;
import com.efp.dvk.plugins.db.ui.CommonDbConfUI;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class TestAction extends AnAction implements DBRunnable {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        new CommonDbConfUI(e.getProject(), true, this).show();
    }

    @Override
    public void run(Project project, DbConnectParam dbConnectParam) {
        DbService service = project.getService(DbService.class);
        Connection connection = service.getConnection(dbConnectParam);
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
