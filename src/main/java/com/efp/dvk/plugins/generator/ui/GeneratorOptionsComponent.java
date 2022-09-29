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
import java.awt.*;

public class GeneratorOptionsComponent extends JPanel {
    private final Project myProject;
    private final JBTable myTable;

    public GeneratorOptionsComponent(@NotNull Project myProject) {
        super(new GridBagLayout());
        this.myProject = myProject;
        GeneratorConfigTableModel generatorConfigTableModel = new GeneratorConfigTableModel();
        GeneratorConfig generatorConfig = new GeneratorConfig();
        generatorConfig.setFileName("test");
        generatorConfig.setName("test");
        generatorConfig.setType("test");
        generatorConfig.setFilePath("test");
        generatorConfig.setTemplateContent("test");
        generatorConfigTableModel.getGeneratorConfigList().add(generatorConfig);
        myTable = new JBTable(generatorConfigTableModel);
        myTable.setShowGrid(false);
        myTable.setRowHeight(JBUIScale.scale(22));
        myTable.getEmptyText().setText(JavaCompilerBundle.message("settings.all.modules.will.be.compiled.with.project.bytecode.version"));

//        if (column == 0) {
//            return "配置名";
//        }
//        if (column == 1) {
//            return "类型";
//        }
//        if (column == 2) {
//            return "文件名";
//        }
//        if (column == 3) {
//            return "文件路径";
//        }
//        if (column == 4) {
//            return "模板内容";
//        }

        TableColumn moduleColumn = myTable.getColumnModel().getColumn(0);
        moduleColumn.setHeaderValue("配置名");
        moduleColumn.setCellRenderer(new ModuleTableCellRenderer());

        TableColumn optionsColumn = myTable.getColumnModel().getColumn(1);
        String columnTitle = "类型";
        optionsColumn.setHeaderValue(columnTitle);
        int width = myTable.getFontMetrics(myTable.getFont()).stringWidth(columnTitle) + 10;
        optionsColumn.setPreferredWidth(width);
        optionsColumn.setMinWidth(width);
        ExpandableTextField editor = new ExpandableTextField();
        InsertPathAction.addTo(editor, null, false);
        optionsColumn.setCellEditor(new DefaultCellEditor(editor));
        new TableSpeedSearch(myTable);

        JPanel table = ToolbarDecorator.createDecorator(myTable)
                .disableUpAction()
                .disableDownAction()
                .setAddAction(b -> addModules())
                .setRemoveAction(b -> removeSelectedModules())
                .createPanel();
        table.setPreferredSize(new Dimension(myTable.getWidth(), 150));
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
