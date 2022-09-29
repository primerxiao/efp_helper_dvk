package com.efp.dvk.plugins.generator.model;

import com.intellij.util.ui.ItemRemovable;
import lombok.Getter;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class GeneratorConfigTableModel  extends AbstractTableModel implements ItemRemovable {

    @Getter
    private final List<GeneratorConfig> generatorConfigList = new ArrayList<>();

    @Override
    public void removeRow(int idx) {
        generatorConfigList.remove(idx);
    }

    @Override
    public int getRowCount() {
        return generatorConfigList.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        GeneratorConfig generatorConfig = generatorConfigList.get(rowIndex);
        if (generatorConfig == null) {
            return null;
        }
        if (columnIndex == 0) {
            return generatorConfig.getName();
        }
        if (columnIndex == 1) {
            return generatorConfig.getType();
        }
        if (columnIndex == 2) {
            return generatorConfig.getFileName();
        }
        if (columnIndex == 3) {
            return generatorConfig.getFilePath();
        }
        if (columnIndex == 4) {
            return generatorConfig.getTemplateContent();
        }
        return null;
    }
}
