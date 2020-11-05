package com.efp.plugins.general.jsonviewer.action;

import com.efp.plugins.general.jsonviewer.ui.JsonViewUi;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class JsonViewAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        new JsonViewUi(anActionEvent.getProject()).show();
    }
}
