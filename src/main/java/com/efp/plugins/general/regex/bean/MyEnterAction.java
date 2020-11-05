package com.efp.plugins.general.regex.bean;

import com.efp.plugins.general.regex.ui.AnyRulePopupPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class MyEnterAction extends DumbAwareAction {

	private final boolean myEnterAsOK;
	private AnyRulePopupPanel anyRulePopupPanel;

	public MyEnterAction(boolean enterAsOK, AnyRulePopupPanel anyRulePopupPanel) {
		this.myEnterAsOK = enterAsOK;
		this.anyRulePopupPanel = anyRulePopupPanel;
	}

	@Override
	public void update(@NotNull AnActionEvent e) {
		e.getPresentation().setEnabled(e.getData(CommonDataKeys.EDITOR) == null);
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		anyRulePopupPanel.insertRuleToDocument();
	}
}
