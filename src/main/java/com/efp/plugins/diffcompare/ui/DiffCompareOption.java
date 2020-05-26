package com.efp.plugins.diffcompare.ui;

import com.android.aapt.Resources;
import com.efp.plugins.diffcompare.config.DiffCompareCacheConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.intellij.ide.impl.ProjectUtil;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.colorpicker.CommonButton;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;

public class DiffCompareOption extends DialogWrapper {
    private JPanel jpanel;
    private JTable diffCompareList;
    private JScrollPane jScrollPane;
    private JButton add;
    private JButton addCommentBtn;
    private JButton exportBtn;
    private JComboBox moduleComboBox;
    private JTextField fileTextField;
    private JComboBox diffTypeComboBox;
    private JButton qryBtn;


    private AnActionEvent event;

    public DiffCompareOption(boolean canBeParent, AnActionEvent event) {
        super(canBeParent);
        this.event = event;
        jpanel.setPreferredSize(new Dimension(1024, 720));
        init();
        setCache();
        loadCache();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return this.jpanel;
    }

    private DefaultTableModel defaultTableModel = null;

    private void createUIComponents() {
        defaultTableModel = new DefaultTableModel(new String[][]{}, new String[]{"差异类型", "模块", "文件全路径", "处理方案"});
        // TODO: place custom component creation code here
        // 设置表格
//        String[][] data = {};
//        String[] name = {"差异类型", "模块", "文件全路径", "处理方案"};
//        DefaultTableModel defaultTableModel = new DefaultTableModel(data, name);
        diffCompareList = new JBTable(defaultTableModel);
        diffCompareList.setEnabled(false);
        diffCompareList.getColumnModel().getSelectionModel().addListSelectionListener(e -> {
            int selectedRowCount = diffCompareList.getSelectedRowCount();
        });
        jScrollPane = new JBScrollPane(diffCompareList);

        //设置按钮
        add = new CommonButton("新增数据");
        add.addActionListener(e -> defaultTableModel.addRow(
                new String[]{String.valueOf(new Random().nextInt()),
                        String.valueOf(new Random().nextInt()),
                        String.valueOf(new Random().nextInt()),
                        String.valueOf(new Random().nextInt())}));

        addCommentBtn = new CommonButton("添加注释到代码");

        exportBtn = new CommonButton("导出");

        moduleComboBox = new ComboBox();

        diffTypeComboBox = new ComboBox(new String[]{"新增", "修改"});

        fileTextField = new JBTextField();

        qryBtn = new CommonButton("过滤搜索");

    }

    //保存缓存数据
    public void setCache() {
        DiffCompareCacheConfig diffCompareCacheConfig = new DiffCompareCacheConfig();
        diffCompareCacheConfig.setDiffCompareListIndex(diffCompareList.getSelectedRow());
        diffCompareCacheConfig.setModuleComboBoxValue((String) moduleComboBox.getSelectedItem());
        diffCompareCacheConfig.setFileTextFieldValue(fileTextField.getText());
        diffCompareCacheConfig.setDiffTypeComboBoxValue((String) diffTypeComboBox.getSelectedItem());
        PropertiesComponent.getInstance(Objects.requireNonNull(event.getProject())).setValue(diffCompareCacheConfig.getCacheKey(), new Gson().toJson(diffCompareCacheConfig));
    }
    //加载缓存数据
    public void loadCache(){
        DiffCompareCacheConfig diffCompareCacheConfig = new DiffCompareCacheConfig();
        String value = PropertiesComponent.getInstance(Objects.requireNonNull(event.getProject())).getValue(diffCompareCacheConfig.getCacheKey());
        diffCompareCacheConfig = new Gson().fromJson(value, DiffCompareCacheConfig.class);
        //设置值
        for (String[] basicDatum : diffCompareCacheConfig.getBasicData()) {
            defaultTableModel.addRow(basicDatum);

        }
    }
}
