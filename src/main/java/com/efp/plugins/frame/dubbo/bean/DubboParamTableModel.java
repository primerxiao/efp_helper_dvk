package com.efp.plugins.frame.dubbo.bean;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 肖均辉
 */
public class DubboParamTableModel extends AbstractTableModel {

    private List<DubboMethodParam> dubboMethodParams = new ArrayList();

    private String[] columnNames = {"序号", "参数类型", "参数值"};

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
                return dubboMethodParam.getIndex();
            case 1:
                return dubboMethodParam.getType();
            case 2:
                return dubboMethodParam.getValue();
            default:
                return null;
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
}
