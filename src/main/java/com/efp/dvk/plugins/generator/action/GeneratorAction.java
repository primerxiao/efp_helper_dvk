package com.efp.dvk.plugins.generator.action;

import com.efp.dvk.plugins.generator.ui.GeneratorMainUI;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class GeneratorAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        new GeneratorMainUI(e.getProject(), true).show();
    }
}
