package com.efp.plugins.diffcompare.action;

import com.efp.common.util.EditorUtils;
import com.efp.plugins.diffcompare.ui.DiffCompareOption;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class DiffCompareAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {


        DiffCompareOption diffCompareOption = new DiffCompareOption(true, e);
        diffCompareOption.show();

/*        if (true) {
            return;
        }
        //展示一个列表
        EditorUtils.closeAllEditor(e);
        //执行gitcomparewith branch动作
        AnAction gitCompareWithBranchAction = ActionManager.getInstance().getAction("Git.CompareWithBranch");
        gitCompareWithBranchAction.actionPerformed(e);*/

    }
}
