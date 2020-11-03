package com.efp.plugins.jsonviewer.ui;

import com.efp.common.util.JsonUtils;
import com.efp.common.util.NotifyUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class JsonViewUi extends DialogWrapper {

    private JPanel jPanel;
    private JScrollPane jscrollPane;
    private JTextArea textArea;

    public JsonViewUi(@Nullable Project project) {
        super(project, true);
        jPanel.setPreferredSize(new Dimension(400, 400));
        setTitle("Json格式化");
        setOKActionEnabled(true);
        setOKButtonText("格式化");
        setCancelButtonText("关闭");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return jPanel;
    }

    @Override
    protected void doOKAction() {
        String srcAreaText = textArea.getText();
        if (StringUtils.isEmpty(srcAreaText)) {
            NotifyUtils.notifyError("数据不能为空");
            return;
        }
        //格式化字符串
        textArea.setText("");
        textArea.append(JsonUtils.prettyformat(srcAreaText));
    }
}
