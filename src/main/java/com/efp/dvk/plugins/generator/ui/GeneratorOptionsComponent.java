package com.efp.dvk.plugins.generator.ui;

import com.efp.dvk.plugins.generator.model.GeneratorConfig;
import com.efp.dvk.plugins.generator.model.GeneratorConfigTableModel;
import com.intellij.compiler.options.ModuleTableCellRenderer;
import com.intellij.openapi.compiler.JavaCompilerBundle;
import com.intellij.openapi.project.Project;
import com.intellij.ui.InsertPathAction;
import com.intellij.ui.TableSpeedSearch;
import com.intellij.ui.TableUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.fields.ExpandableTextField;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;

public class GeneratorOptionsComponent extends JPanel {
    private final Project myProject;
    private final JBTable myTable;

    public GeneratorOptionsComponent(@NotNull Project myProject) {
        super(new GridBagLayout());
        this.myProject = myProject;
        myTable = new JBTable(new GeneratorConfigTableModel());
        myTable.setShowGrid(false);
        myTable.setRowHeight(JBUIScale.scale(22));
        myTable.getEmptyText().setText(JavaCompilerBundle.message("settings.all.modules.will.be.compiled.with.project.bytecode.version"));
        GeneratorConfigTableModel model = (GeneratorConfigTableModel) myTable.getModel();
        model.getGeneratorConfigList().add(GeneratorConfig.builder()
                .fileName("test")
                .filePath("test")
                .name("name")
                .type("type")
                .templateContent("coadfasda")
                .build()
        );

        model.fireTableDataChanged();
        new TableSpeedSearch(myTable);

        myTable.getColumnModel().getColumn(0).setHeaderValue("名称");
        myTable.getColumnModel().getColumn(1).setHeaderValue("类型");
        myTable.getColumnModel().getColumn(2).setHeaderValue("文件名");
        myTable.getColumnModel().getColumn(3).setHeaderValue("文件路径");
        myTable.getColumnModel().getColumn(4).setHeaderValue("文本模板");

        JPanel table = ToolbarDecorator.createDecorator(myTable)
                .disableUpAction()
                .disableDownAction()
                .setAddAction(b -> addModules())
                .setRemoveAction(b -> removeSelectedModules())
                .createPanel();
        table.setPreferredSize(new Dimension(myTable.getWidth(), 200));
        JLabel header = new JLabel("设置代码生成配置");
        add(header, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, JBUI.insets(5, 5, 0, 0), 0, 0));
        add(table, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, JBUI.insets(5, 5, 0, 0), 0, 0));

    }

    private void removeSelectedModules() {
        TableUtil.removeSelectedItems(myTable);
    }

    private void addModules() {

    }
}
