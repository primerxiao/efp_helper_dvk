package com.efp.service.project;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class ProjectOpenCloseListener implements ProjectManagerListener {

    @Override
    public void projectOpened(@NotNull Project project) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }
        ProjectCountingService service = ServiceManager.getService(ProjectCountingService.class);
        service.incrProjectCount();
        if (service.projectLimitExceeded()) {
            Messages.showMessageDialog(project,"打开的项目过多","提示信息",Messages.getInformationIcon());
        }
    }

    @Override
    public void projectClosed(@NotNull Project project) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }
        ProjectCountingService service = ServiceManager.getService(ProjectCountingService.class);
        service.decrProjectCount();
    }
}
