package com.efp.dvk.plugins.test.action;

import com.efp.dvk.common.lang.EfpDvkBundle;
import com.efp.dvk.common.lang.EfpDvkNotifications;
import com.efp.dvk.plugins.db.model.DbRunEvent;
import com.efp.dvk.plugins.db.service.DBRunnable;
import com.efp.dvk.plugins.generator.ui.GeneratorMainUI;
import com.intellij.database.DatabaseBundle;
import com.intellij.database.DatabaseNotifications;
import com.intellij.database.extensions.ExtensionScriptsUtil;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class TestAction extends AnAction implements DBRunnable {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
//        new DbConfUI(e.getProject(),
//                true,
//                "",
//                this,
//                true).show();

        File file = new File("E:\\payadmin4_2.sql");

        VirtualFile fileByIoFile = VfsUtil.findFileByIoFile(file, true);

        EfpDvkNotifications.EFP_DVK_VIEW_GROUP
                .createNotification(
                        EfpDvkBundle.message(
                                "notification.test.to.file",
                                 fileByIoFile.getPath()
                        ),
                        NotificationType.INFORMATION)
                .setListener((notification, event) -> ExtensionScriptsUtil.navigateToFile(e.getProject(), file.toPath())).notify(e.getProject());

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
