package com.efp.dvk.plugins.generator.ui;

import com.efp.dvk.common.lang.NotifyUtils;
import com.efp.dvk.common.service.PluginOrmService;
import com.efp.dvk.plugins.db.service.DbService;
import com.efp.dvk.plugins.generator.entity.DatabaseConfig;
import com.efp.dvk.plugins.generator.model.GeneratorConfigTableModel;
import com.intellij.compiler.options.ModuleOptionsTableModel;
import com.intellij.openapi.compiler.JavaCompilerBundle;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.TableUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GeneratorMainUI extends DialogWrapper {

    private JPanel jPanel;
    private JButton connectionConfigButton;
    private JButton generatorConfigButton;
    private JTree tree1;
    private JScrollPane jScrollPane;
    private JTextField filterTextField;
    private JButton filterButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField4;
    private JPanel rightSplitJpanel;

    private final Project project;

    private JBTable myTable;

    public GeneratorMainUI(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        this.project = project;
        setTitle("GeneratorUI");
        setSize(700, 500);

        DefaultMutableTreeNode root_node = new DefaultMutableTreeNode("Root Node");
        List<DatabaseConfig> databaseConfigs = PluginOrmService.instance().selectAll(DatabaseConfig.class);

        databaseConfigs.forEach(d -> {
            DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(d.getName());
            root_node.add(defaultMutableTreeNode);
        });
        DefaultTreeModel treeModel = new DefaultTreeModel(root_node);

        tree1.setModel(treeModel);
        tree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        tree1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    //双击
                    updateTables();
                    return;
                }
                super.mouseClicked(e);
            }
        });

        connectionConfigButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectionConfigUI connectionConfigUI = new ConnectionConfigUI(project, true);
                connectionConfigUI.show();
                int exitCode = connectionConfigUI.getExitCode();
                if (exitCode == OK_EXIT_CODE) {
                    //刷新
                }
            }
        });
        filterButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTables();
            }
        });
        filterButton.setText("过滤");
        connectionConfigButton.setText("连接设置");

        myTable = new JBTable(new GeneratorConfigTableModel());
        myTable.setShowGrid(false);
        myTable.setRowHeight(JBUIScale.scale(22));
        myTable.getEmptyText().setText(JavaCompilerBundle.message("settings.all.modules.will.be.compiled.with.project.bytecode.version"));


        JPanel tableComp = ToolbarDecorator.createDecorator(myTable)
                .disableUpAction()
                .disableDownAction()
                .setAddAction(b -> addModules())
                .setRemoveAction(b -> removeSelectedModules())
                .createPanel();

        rightSplitJpanel.add(tableComp);

        init();
    }

    private void removeSelectedModules() {
        TableUtil.removeSelectedItems(myTable);
    }

    private void addModules() {

    }

    private void updateTables() {
        TreePath selectionPath = tree1.getSelectionPath();
        if (selectionPath == null) {
            return;
        }
        DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
        if (selectionPath.getPath().length - 1 == 1) {
            lastPathComponent.removeAllChildren();
            String databaseConfigKey = (String) lastPathComponent.getUserObject();
            //选择了配置
            //获取表
            assert project != null;
            DatabaseConfig databaseConfig = null;//CacheService.instance().hashMapGet(CacheNameEnum.DatabaseConfig, databaseConfigKey);
            List<String> tableNames = project.getService(DbService.class).getTableNames(databaseConfig, filterTextField.getName());
            for (String tableName : tableNames) {
                DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(tableName);
                lastPathComponent.add(defaultMutableTreeNode);
                try {
                    tree1.fireTreeWillExpand(selectionPath);
                    tree1.fireTreeExpanded(selectionPath);
                } catch (ExpandVetoException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        if (selectionPath.getPath().length - 1 == 2) {
            //选择了表
            String selectTableName = (String) lastPathComponent.getUserObject();
            NotifyUtils.warn(project, selectTableName);

        }
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return jPanel;
    }
}
