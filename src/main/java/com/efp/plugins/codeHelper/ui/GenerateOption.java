package com.efp.plugins.codeHelper.ui;

import com.efp.common.constant.PluginContants;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GenerateOption  extends DialogWrapper {
    private JPanel jpanel;
    private JCheckBox serviceImpl;
    private JCheckBox mapper;
    private JCheckBox domain;
    private JCheckBox service;
    private JCheckBox controller;
    private JCheckBox dao;
    private JCheckBox vo;
    private JCheckBox isOverWrite;

    public GenerateOption(boolean canBeParent) {
        super(canBeParent);
        init();
        setTitle(PluginContants.GENERATOR_UI_TITLE);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        serviceImpl = new JCheckBox();
        mapper = new JCheckBox();
        domain = new JCheckBox();
        service = new JCheckBox();
        controller = new JCheckBox();
        dao = new JCheckBox();
        vo = new JCheckBox();
        isOverWrite = new JCheckBox();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return this.jpanel;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }
}
