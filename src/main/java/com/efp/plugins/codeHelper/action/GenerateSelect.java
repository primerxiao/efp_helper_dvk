package com.efp.plugins.codeHelper.action;

import com.efp.common.constant.PluginContants;
import com.efp.common.util.DasUtils;
import com.efp.common.util.EditorUtils;
import com.efp.plugins.codeHelper.bean.GenerateInfo;
import com.efp.plugins.codeHelper.ui.GenerateOptionInsertOrUpdate;
import com.efp.plugins.codeHelper.ui.GenerateOptionSelect;
import com.intellij.database.model.DasColumn;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GenerateSelect extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        //DasUtils.refreshDas(e);
        EditorUtils.closeAllEditor(e);
        //获取数据库配置
        final PsiElement[] psiElementArr = e.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
        if (psiElementArr == null || psiElementArr.length < 1) {
            return;
        }
        if (!Arrays.stream(psiElementArr).anyMatch(p -> p instanceof DasColumn)) {
            return;
        }
        final GenerateInfo generateInfo = DasUtils.getGenerateInfo(e, PsiTreeUtil.getParentOfType(psiElementArr[0], DbTable.class));
        generateInfo.setSelectDasColumns(Arrays.stream(psiElementArr).map(p -> (DasColumn) p).collect(Collectors.toList()));
        final String currentMethodName = Messages.showInputDialog(e.getProject(), "请输出函数名称", PluginContants.GENERATOR_UI_TITLE, null);
        if (StringUtils.isEmpty(currentMethodName)) {
            return;
        }
        generateInfo.getGenerateJava().setCurrentMethodName(currentMethodName);
        final GenerateOptionSelect generateOptionSelect = new GenerateOptionSelect(true, e, generateInfo);
        generateOptionSelect.show();
    }

}
