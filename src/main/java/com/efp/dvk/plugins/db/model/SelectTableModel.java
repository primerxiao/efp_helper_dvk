package com.efp.dvk.plugins.db.model;

import com.efp.dvk.plugins.db.entity.Tables;
import lombok.Getter;
import lombok.Setter;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SelectTableModel extends AbstractTableModel {

    private List<Tables> data = new ArrayList<>();

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "数据库";
        }
        if (column == 1) {
            return "表名";
        }
        if (column == 2) {
            return "表注释";
        }
        return super.getColumnName(column);
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return data.get(rowIndex).getTableSchema();
        }
        if (columnIndex == 1) {
            return data.get(rowIndex).getTableName();
        }
        if (columnIndex == 2) {
            return data.get(rowIndex).getTableComment();
        }
        return null;
    }

}

