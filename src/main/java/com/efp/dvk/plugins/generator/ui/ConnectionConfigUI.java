package com.efp.dvk.plugins.generator.ui;

import com.efp.dvk.common.lang.annation.ConfField;
import com.efp.dvk.common.service.AbstractDialogService;
import com.efp.dvk.common.service.PluginOrmService;
import com.efp.dvk.plugins.db.model.DbType;
import com.efp.dvk.plugins.db.service.DbService;
import com.efp.dvk.plugins.generator.entity.DatabaseConfig;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Objects;

public class ConnectionConfigUI extends DialogWrapper implements AbstractDialogService {
    private JPanel jPanel;
    @ConfField
    private JTextField name;
    @ConfField
    private JComboBox<DbType> dbType;
    @ConfField
    private JTextField ip;
    @ConfField
    private JFormattedTextField port;
    @ConfField
    private JTextField username;
    @ConfField
    private JPasswordField password;
    @ConfField
    private JTextField schema;
    @ConfField
    private JComboBox<String> encoding;
    private JLabel testConnect;

    private final Project project;

    public ConnectionConfigUI(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        this.project = project;
        setTitle("New Connection Config");

        Arrays.stream(DbType.values()).forEach(d -> dbType.addItem(d));

        encoding.addItem("utf8");
        encoding.addItem("gb2312");
        encoding.addItem("gbk");

        testConnect.setText("test connect");
        testConnect.setForeground(JBColor.GREEN);
        testConnect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        testConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                synchronized (this) {
                    assert project != null;
                    DbService service = project.getService(DbService.class);
                    service.testConnection(getDabaseConfig());
                    super.mouseClicked(e);
                }
            }
        });
        loadConf();
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return jPanel;
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
        try {
            DatabaseConfig dabaseConfig = getDabaseConfig();
            project.getService(DbService.class).testConnection(dabaseConfig);
            if (PluginOrmService.instance().selectById(DatabaseConfig.builder().name(dabaseConfig.getName()).build()) != null) {
                throw new RuntimeException("name " + dabaseConfig.getName() + " exis");
            }
            return super.doValidate();
        } catch (Exception e) {
            return new ValidationInfo(e.getMessage());
        }
    }

    @Override
    protected void doOKAction() {
        saveConf();
        PluginOrmService.instance().insert(getDabaseConfig());
        super.doOKAction();
    }

    private DatabaseConfig getDabaseConfig() {
        return DatabaseConfig.builder()
                .dbTypeName(((DbType) Objects.requireNonNull(dbType.getSelectedItem())).name())
                .schema(schema.getText())
                .encoding(String.valueOf(encoding.getSelectedItem()))
                .host(ip.getText())
                .name(name.getText())
                .username(username.getText())
                .password(String.valueOf(password.getPassword()))
                .port(port.getText())
                .build();
    }
}
