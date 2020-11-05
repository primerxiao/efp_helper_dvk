package com.efp.plugins.general.cron.action;

import com.efp.plugins.general.cron.ui.CronUi;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class CronAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        try {
            new CronUi(anActionEvent.getProject()).show();
        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog(anActionEvent.getProject(), e.getMessage(), "错误信息");
        }
    }
}
