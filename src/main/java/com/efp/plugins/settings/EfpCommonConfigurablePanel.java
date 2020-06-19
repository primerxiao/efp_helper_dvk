package com.efp.plugins.settings;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;

public class EfpCommonConfigurablePanel {
    private JPanel myPanel;
    private JTextField testTextField;
    public JComponent getPanel() {
        return myPanel;
    }

    public void reset() {
        testTextField.setText("");
    }

    public boolean isModified() {
        return StringUtils.isNotEmpty(testTextField.getText());
    }
}
