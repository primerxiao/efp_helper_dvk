package com.efp.plugins.frame.coder.ui;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CompareOption extends DialogWrapper {
    private JPanel jpanel;
    private JCheckBox compareDomain;
    private JCheckBox compareVo;
    private JCheckBox compareMapper;

    protected CompareOption(boolean canBeParent) {
        super(canBeParent);
    }

    private void createUIComponents()  {
        // TODO: place custom component creation code here
        compareDomain = new JCheckBox();
        compareVo = new JCheckBox();
        compareMapper = new JCheckBox();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return this.jpanel;
    }

    @Override
    protected void doOKAction() {
        compareDomain();
    }

    private void compareDomain(){
        //获取所有字段
        //获取Domain的字段
    }
}
