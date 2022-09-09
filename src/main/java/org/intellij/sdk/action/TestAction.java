package org.intellij.sdk.action;

import com.intellij.database.model.DasTable;
import com.intellij.database.model.RawConnectionConfig;
import com.intellij.database.psi.DbDataSourceImpl;
import com.intellij.database.run.actions.NavigateAction;
import com.intellij.database.util.DasUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.PsiElement;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TestAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {


    }

}
