package com.efp.dvk.plugins.generator.ui;

import com.efp.dvk.common.lang.NotifyUtils;
import com.efp.dvk.common.service.PluginOrmService;
import com.efp.dvk.plugins.db.service.DbService;
import com.efp.dvk.plugins.generator.entity.DatabaseConfig;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.compiler.JavaCompilerBundle;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;

public class GeneratorMainUI extends DialogWrapper {

    private JPanel jPanel;

    private JSplitPane jSplitPane;

    private final Project project;

    private final JTree jTree;

    private final GeneratorOptionsComponent generatorOptionsComponent;

    public GeneratorMainUI(@Nullable Project project, boolean canBeParent) {

        super(project, canBeParent);
        this.project = project;
        setTitle("GeneratorUI");
        setSize(720, 620);

        //初始化配置树
        DefaultMutableTreeNode root_node = new DefaultMutableTreeNode("Root Node");

        List<DatabaseConfig> databaseConfigs = PluginOrmService.instance().selectAll(DatabaseConfig.class);

        databaseConfigs.forEach(d -> {
            DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(d.getName());
            root_node.add(defaultMutableTreeNode);
        });
        DefaultTreeModel treeModel = new DefaultTreeModel(root_node);

        jTree = new Tree(treeModel);

        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        jTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    //双击
                    updateTables(null);
                    return;
                }
                super.mouseClicked(e);
            }
        });

        JPanel jTreePanel = ToolbarDecorator.createDecorator(jTree)
                .disableUpAction()
                .disableDownAction()
                .setAddAction(anActionButton -> {
                    ConnectionConfigUI connectionConfigUI = new ConnectionConfigUI(project, true);
                    connectionConfigUI.show();
                    int exitCode = connectionConfigUI.getExitCode();
                    if (exitCode == OK_EXIT_CODE) {
                        //刷新
                        root_node.removeAllChildren();
                        List<DatabaseConfig> databaseConfigs1 = PluginOrmService.instance().selectAll(DatabaseConfig.class);
                        for (DatabaseConfig databaseConfig : databaseConfigs1) {
                            DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(databaseConfig.getName());
                            root_node.add(defaultMutableTreeNode);
                        }
                    }
                })
                .setRemoveAction(anActionButton -> {
                    TreePath selectionPath = jTree.getSelectionPath();
                    assert selectionPath != null;
                    if (selectionPath.getPath().length == 2) {
                        //config
                        //
                    }

                })
                .addExtraAction(new FilterTableAction())
                .createPanel();


        jSplitPane.setLeftComponent(jTreePanel);

        assert project != null;
        generatorOptionsComponent = new GeneratorOptionsComponent(project);

        jSplitPane.setRightComponent(generatorOptionsComponent);

        init();
    }

    private void updateTables(@Nullable String filterTableName) {
        TreePath selectionPath = jTree.getSelectionPath();
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
            DatabaseConfig databaseConfig = PluginOrmService.instance().selectById(DatabaseConfig.builder().name(databaseConfigKey).build());
            List<String> tableNames = project.getService(DbService.class).getTableNames(databaseConfig, filterTableName);
            for (String tableName : tableNames) {
                DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(tableName);
                lastPathComponent.add(defaultMutableTreeNode);
                try {
                    jTree.fireTreeWillExpand(selectionPath);
                    jTree.fireTreeExpanded(selectionPath);
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

    /**
     * 过滤表的按钮动作
     */
    private class FilterTableAction extends AnActionButton {

        FilterTableAction() {
            super("Filter Table", AllIcons.Actions.Search);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {

            String filterTable = Messages.showInputDialog(project,
                    "Input table name which you want filter",
                    "Filter Table",
                    AllIcons.Ide.FeedbackRatingOn);

            updateTables(filterTable);

        }

    }

}
