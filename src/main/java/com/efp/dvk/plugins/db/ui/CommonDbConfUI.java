package com.efp.dvk.plugins.db.ui;

import com.efp.dvk.plugins.db.service.DBRunnable;
import com.efp.dvk.plugins.db.model.DbConnectParam;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CommonDbConfUI extends DialogWrapper {
    private JPanel jPanel;
    private JComboBox<String> driverClassComboBox;
    private JTextField baseUrlTextField;
    private JTextField userNameTextField;
    private JTextField schemaTextField;
    private JButton saveAsDefaultButton;
    private JButton loadFromModuleButton;
    private JComboBox<Module> moduleComboBox;
    private JPasswordField passwordField;
    private JTextField paramsTextField;

    private final Project project;

    private final DBRunnable dbRunnable;

    public CommonDbConfUI(@Nullable Project project, boolean canBeParent, DBRunnable dbRunnable) {

        super(project, canBeParent);
        this.project = project;
        this.dbRunnable = dbRunnable;
        driverClassComboBox.addItem("com.mysql.cj.jdbc.Driver");
        driverClassComboBox.addItem("com.mysql.jdbc.Driver");

        init();
    }

    @Override
    protected void doOKAction() {
        //check
        if (StringUtils.isEmpty(String.valueOf(driverClassComboBox.getSelectedItem())) ||
                StringUtils.isEmpty(baseUrlTextField.getText()) ||
                StringUtils.isEmpty(schemaTextField.getText()) ||
                StringUtils.isEmpty(userNameTextField.getText()) ||
                StringUtils.isEmpty(String.valueOf(passwordField.getPassword()))) {
            Messages.showErrorDialog(project, "DbConnect config not valid", "Error");
            return;
        }
        DbConnectParam dbConnectParam = new DbConnectParam();
        dbConnectParam.setDriverClass(String.valueOf(driverClassComboBox.getSelectedItem()));
        dbConnectParam.setBaseUrl(baseUrlTextField.getText().trim());
        dbConnectParam.setSchema(schemaTextField.getText().trim());
        dbConnectParam.setUserName(userNameTextField.getText().trim());
        dbConnectParam.setPassword(String.valueOf(passwordField.getPassword()));
        dbConnectParam.setParams(paramsTextField.getText().trim());
        super.doOKAction();
        dbRunnable.run(project, dbConnectParam);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return jPanel;
    }
}
