package com.efp.plugins.project.gitmr.action;

import com.efp.plugins.project.gitmr.ui.GitMrUi;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author primerxiao
 */
public class GitMrAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        GitMrUi gitMrUi = new GitMrUi(e.getProject());
        gitMrUi.show();
    }
}
