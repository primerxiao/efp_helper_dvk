package com.efp.dvk.plugins.generator.model;

import com.intellij.util.ui.ItemRemovable;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class GeneratorConfigTableModel  extends AbstractTableModel implements ItemRemovable {

    private final List<GeneratorConfig> generatorConfigList = new ArrayList<>();

    @Override
    public void removeRow(int idx) {

    }

    @Override
    public int getRowCount() {
        return generatorConfigList.size();
    }

    @Override
    public int getColumnCount() {
        return GeneratorConfig.class.getDeclaredFields().length;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "配置名";
        }
        if (column == 1) {
            return "类型";
        }
        if (column == 2) {
            return "文件名";
        }
        if (column == 3) {
            return "文件路径";
        }
        if (column == 4) {
            return "模板内容";
        }
        return super.getColumnName(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
