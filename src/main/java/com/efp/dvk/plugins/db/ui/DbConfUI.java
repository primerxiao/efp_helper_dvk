package com.efp.dvk.plugins.db.ui;

import com.efp.dvk.common.annation.ConfField;
import com.efp.dvk.common.service.DialogAbstractService;
import com.efp.dvk.common.util.NotifyUtils;
import com.efp.dvk.plugins.db.model.DbRunEvent;
import com.efp.dvk.plugins.db.service.DBRunnable;
import com.efp.dvk.plugins.db.model.DbConnectParam;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class DbConfUI extends DialogWrapper implements DialogAbstractService {
    private JPanel jPanel;
    @ConfField
    private JComboBox<String> driverClassComboBox;
    @ConfField
    private JTextField baseUrlTextField;
    @ConfField
    private JTextField userNameTextField;
    @ConfField
    private JTextField schemaTextField;
    private JButton saveAsDefaultButton;
    private JButton loadFromModuleButton;
    @ConfField
    private JComboBox<Module> moduleComboBox;
    @ConfField
    private JPasswordField passwordField;
    @ConfField
    private JTextField paramsTextField;
    private JButton testConnectButton;

    private final Project project;

    private final DBRunnable dbRunnable;

    public DbConfUI(@Nullable Project project,
                    boolean canBeParent,
                    String title,
                    DBRunnable dbRunnable,
                    boolean schemaInformationFlag) {
        super(project, canBeParent);
        setTitle(StringUtils.isEmpty(title) ? "Database Config" : title);
        this.project = project;
        this.dbRunnable = dbRunnable;
        //driver
        driverClassComboBox.addItem("com.mysql.cj.jdbc.Driver");
        driverClassComboBox.addItem("com.mysql.jdbc.Driver");
        //url
        baseUrlTextField.setText("jdbc:mysql://localhost:3306");
        if (schemaInformationFlag) {
            schemaTextField.setEditable(false);
            schemaTextField.setText("information_schema");
        }
        //module
        assert project != null;
        Module[] modules = ModuleManager.getInstance(project).getModules();
        Arrays.stream(modules).forEach(m -> moduleComboBox.addItem(m));
        //save
        saveAsDefaultButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveConf();
                NotifyUtils.info(project, "save conf success");
            }
        });
        saveAsDefaultButton.setText("SaveConf");
        //load
        loadFromModuleButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadConf();
                NotifyUtils.info(project, "load conf success");
            }
        });
        loadFromModuleButton.setText("LoadConf");
        init();
    }

    @Override
    protected void doOKAction() {
        //check
        DbConnectParam dbConnectParam =
                DbConnectParam.builder().driverClass(String.valueOf(driverClassComboBox.getSelectedItem()))
                        .baseUrl(baseUrlTextField.getText().trim())
                        .schema(schemaTextField.getText().trim())
                        .userName(userNameTextField.getText().trim())
                        .password(String.valueOf(passwordField.getPassword()))
                        .params(paramsTextField.getText().trim()).build();
        super.doOKAction();
        dbRunnable.run(
                DbRunEvent.builder()
                        .dbConnectParam(dbConnectParam)
                        .project(project)
                        .build()
        );
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {

        if (StringUtils.isEmpty(String.valueOf(driverClassComboBox.getSelectedItem()))) {
            return new ValidationInfo("Choose driver class");
        }
        if (StringUtils.isEmpty(baseUrlTextField.getText())) {
            return new ValidationInfo("Base url is not valid");
        }
        if (StringUtils.isEmpty(schemaTextField.getText())) {
            return new ValidationInfo("Shcema is not valid");
        }
        if (StringUtils.isEmpty(userNameTextField.getText())) {
            return new ValidationInfo("Username is not valid");
        }
        if (StringUtils.isEmpty(String.valueOf(passwordField.getPassword()))) {
            return new ValidationInfo("Password is not valid");
        }
        return super.doValidate();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return jPanel;
    }
}
