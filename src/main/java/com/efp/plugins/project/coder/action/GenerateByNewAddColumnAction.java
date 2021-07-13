package com.efp.plugins.project.coder.action;

import com.efp.common.constant.PluginContants;
import com.efp.common.notifier.NotificationHelper;
import com.efp.common.util.DasUtils;
import com.efp.common.util.EditorUtils;
import com.efp.plugins.project.coder.bean.GenerateInfo;
import com.efp.plugins.project.coder.ui.GenerateByNewAddColumnUi;
import com.intellij.database.model.DasColumn;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.popup.IPopupChooserBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GenerateByNewAddColumnAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        EditorUtils.closeAllEditor(e);
        //获取数据库配置
        final PsiElement[] psiElementArr = e.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
        if (psiElementArr == null || psiElementArr.length < 1) {
            NotificationHelper.getInstance().notifyError("需要选择相应的字段数据", e.getProject());
            return;
        }
        if (!Arrays.stream(psiElementArr).anyMatch(p -> p instanceof DasColumn)) {
            NotificationHelper.getInstance().notifyError("需要选择相应的字段数据", e.getProject());
            return;
        }
        final GenerateInfo generateInfo = DasUtils.getGenerateInfo(e, PsiTreeUtil.getParentOfType(psiElementArr[0], DbTable.class));
        generateInfo.setSelectDasColumns(Arrays.stream(psiElementArr).map(p -> (DasColumn) p).collect(Collectors.toList()));
        //弹出基础module选择
        IPopupChooserBuilder<String> stringIPopupChooserBuilder = JBPopupFactory.getInstance().createPopupChooserBuilder(PluginContants.CHOOSE_MODULE_NAMES).setItemChosenCallback(b -> {
            generateInfo.setBaseMoudleName(b);
            //打开界面
            final GenerateByNewAddColumnUi generateOptionSelect = new GenerateByNewAddColumnUi(e.getProject(), generateInfo);
            generateOptionSelect.show();
        });
        stringIPopupChooserBuilder.setAdText("选择存储代码的应用");
        stringIPopupChooserBuilder.createPopup().showCenteredInCurrentWindow(e.getProject());
    }

}
