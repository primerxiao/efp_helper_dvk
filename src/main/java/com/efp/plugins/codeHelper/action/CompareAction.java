package com.efp.plugins.codeHelper.action;

import com.efp.common.constant.PluginContants;
import com.efp.common.util.DasUtils;
import com.efp.common.util.EditorUtils;
import com.efp.plugins.codeHelper.bean.GenerateInfo;
import com.efp.plugins.codeHelper.ui.GenerateOptionInsertOrUpdate;
import com.intellij.database.model.DasTable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class CompareAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DasUtils.refreshDas(e);
        EditorUtils.closeAllEditor(e);
        //获取数据库配置
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        if (!(psiElement instanceof DasTable)) {
            Messages.showErrorDialog("请选择一个数据库表对象进行操作", PluginContants.GENERATOR_UI_TITLE);
            return;
        }
        final GenerateInfo generateInfo = DasUtils.getGenerateInfo(e, (DasTable) psiElement);
        final GenerateOptionInsertOrUpdate generateOptionInsertOrUpdate = new GenerateOptionInsertOrUpdate(true, e, generateInfo);
        generateOptionInsertOrUpdate.show();
    }

}
