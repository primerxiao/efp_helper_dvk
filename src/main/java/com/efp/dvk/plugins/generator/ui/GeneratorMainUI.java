package com.efp.dvk.plugins.generator.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GeneratorMainUI extends DialogWrapper {

    private JPanel jPanel;
    private JButton connectionConfigButton;
    private JButton generatorConfigButton;
    private JTabbedPane tabbedPane1;
    private JTree tree1;

    public GeneratorMainUI(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        setTitle("GeneratorUI");
        setSize(700, 500);
        connectionConfigButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConnectionConfigUI(project, true).show();
            }
        });
        connectionConfigButton.setText("连接设置");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return jPanel;
    }
}
