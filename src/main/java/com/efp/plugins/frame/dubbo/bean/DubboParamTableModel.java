package com.efp.plugins.frame.dubbo.bean;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 肖均辉
 */
public class DubboParamTableModel extends AbstractTableModel {

    private List<DubboMethodParam> dubboMethodParams = new ArrayList();

    private String[] columnNames = {"参数类型", "参数值"};

    public DubboParamTableModel(List<DubboMethodParam> dubboMethodParams) {
        this.dubboMethodParams = dubboMethodParams;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return dubboMethodParams.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DubboMethodParam dubboMethodParam = dubboMethodParams.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return dubboMethodParam.getType();
            case 1:
                return dubboMethodParam.getValue();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            dubboMethodParams.get(rowIndex).setValue((String) aValue);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public List<DubboMethodParam> getDubboMethodParams() {
        return dubboMethodParams;
    }

    public void setDubboMethodParams(List<DubboMethodParam> dubboMethodParams) {
        this.dubboMethodParams = dubboMethodParams;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        super.addTableModelListener(e -> {

        });
    }
}
