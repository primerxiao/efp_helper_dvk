package com.efp.plugins.general.decompiler.action;

import com.efp.plugins.general.decompiler.ui.DecompilerUi;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class DecompilerAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        new DecompilerUi(anActionEvent.getProject(), true).show();
    }
}
