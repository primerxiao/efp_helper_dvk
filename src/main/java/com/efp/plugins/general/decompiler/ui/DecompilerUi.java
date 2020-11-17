package com.efp.plugins.general.decompiler.ui;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.util.ExecUtil;
import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.RevealFileAction;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class DecompilerUi extends DialogWrapper {
    private JPanel jPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JButton button1;

    public DecompilerUi(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        setTitle("反编译工具");
        setOKButtonText("反编译");
        setCancelButtonText("取消");
        button1.setIcon(AllIcons.Actions.ProjectDirectory);
        button1.addActionListener(e -> {
            final FileChooserDescriptor descriptor = new FileChooserDescriptor(false, false, true, true, true, false) {
                @Override
                public boolean isFileSelectable(VirtualFile file) {
                    if (!file.isInLocalFileSystem()) {
                        return false;
                    }
                    if (!Objects.requireNonNull(file.getExtension()).contains("jar")) {
                        return false;
                    }
                    return super.isFileSelectable(file);
                }
            };
            FileChooser.chooseFile(descriptor, null, null,file->{
                textField1.setText(file.getPath());
                textField2.setText(file.getParent().getPath() + "/" + file.getName().replace(".jar", ""));
            });
        });
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return jPanel;
    }

    @Override
    protected void doOKAction() {
        //反编译操作
        String homePath = PathManager.getHomePath();
        if (!new File(homePath + "/plugins/java-decompiler/lib/java-decompiler.jar").exists()) {
            Messages.showErrorDialog("找不到decompiler插件，请先安装该插件", "错误信息");
            return;
        }

        ArrayList<String> cmds = new ArrayList<>();
        String cmd = "java -cp \"%s\" org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler -dgs=true %s %s";
        cmds.add(String.format(cmd, "java-decompiler.jar", textField1.getText().trim(), textField2.getText().trim()));
        GeneralCommandLine generalCommandLine = new GeneralCommandLine(cmds);
        generalCommandLine.setCharset(StandardCharsets.UTF_8);
        generalCommandLine.setWorkDirectory(homePath + "\\plugins\\java-decompiler\\lib\\");

        try {
            Process process = generalCommandLine.createProcess();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String s = ExecUtil.execAndReadLine(generalCommandLine);
        System.out.println(s);


        ProcessHandler processHandler = null;
        try {
            processHandler = new OSProcessHandler(generalCommandLine);
            processHandler.startNotify();

            File dir = new File(textField2.getText());
            if (!dir.exists()) {
                return;
            }
            if (!dir.isDirectory()) {
                return;
            }
            RevealFileAction.openDirectory(dir);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "错误信息");
        }

    }

}
