package com.efp.dvk.plugins.test.action;

import com.efp.dvk.common.orm.entity.PluginDialogConf;
import com.efp.dvk.common.service.PluginOrmService;
import com.efp.dvk.plugins.db.model.DbRunEvent;
import com.efp.dvk.plugins.db.service.DBRunnable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class TestAction extends AnAction implements DBRunnable {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        //beansearch test

        PluginOrmService.instance().selectById(PluginDialogConf.builder().confKey("1").build());
        PluginOrmService.instance().insert(PluginDialogConf.builder().confKey("2").confValue("3").build());
        PluginDialogConf pluginDialogConf = PluginOrmService.instance().selectById(PluginDialogConf.builder().confKey("3").build());

//        SearchResult<Test> jack = CacheService.instance().beanSearcher.search(Test.class, MapUtils.builder().field(Test::getName, "jack").build());
//        List<Test> dataList = jack.getDataList();
//        System.out.println(dataList.size());

//        //nofify信息中带文件跳转路径
//        File file = new File("E:\\payadmin4_2.sql");
//
//        VirtualFile fileByIoFile = VfsUtil.findFileByIoFile(file, true);
//
//        EfpDvkNotifications.EFP_DVK_VIEW_GROUP
//                .createNotification(
//                        EfpDvkBundle.message(
//                                "notification.test.to.file",
//                                 fileByIoFile.getPath()
//                        ),
//                        NotificationType.INFORMATION)
//                .setListener((notification, event) -> ExtensionScriptsUtil.navigateToFile(e.getProject(), file.toPath())).notify(e.getProject());

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
