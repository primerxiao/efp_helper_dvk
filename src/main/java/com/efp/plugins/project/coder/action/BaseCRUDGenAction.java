package com.efp.plugins.project.coder.action;

import com.efp.common.constant.PluginContants;
import com.efp.common.util.DasUtils;
import com.efp.common.util.EditorUtils;
import com.efp.plugins.project.coder.bean.GenerateInfo;
import com.efp.plugins.project.coder.ui.GenerateOption;
import com.intellij.database.model.DasTable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.IPopupChooserBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class BaseCRUDGenAction extends AnAction {

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
        //弹出基础module选择
        IPopupChooserBuilder<String> stringIPopupChooserBuilder = JBPopupFactory.getInstance().createPopupChooserBuilder(PluginContants.CHOOSE_MODULE_NAMES).setItemChosenCallback(b -> {
            final GenerateInfo generateInfo = DasUtils.getGenerateInfo(e, (DasTable) psiElement);
            generateInfo.setBaseMoudleName(b);
            //打开界面
            GenerateOption generateOption = new GenerateOption(true, e, generateInfo);
            generateOption.show();
        });
        stringIPopupChooserBuilder.setAdText("选择存储代码的应用");
        stringIPopupChooserBuilder.createPopup().showCenteredInCurrentWindow(e.getProject());

    }


}
