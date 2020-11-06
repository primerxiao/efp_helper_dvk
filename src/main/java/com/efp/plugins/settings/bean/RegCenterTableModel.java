package com.efp.plugins.settings.bean;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RegCenterTableModel extends AbstractTableModel {

    private List<RegCenter> regCenters = new ArrayList();

    private String[] columnNames = {"ip地址", "端口", "注册中心类型"};

    public RegCenterTableModel(List<RegCenter> regCenters) {
        this.regCenters = regCenters;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return regCenters.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RegCenter regCenter = regCenters.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return regCenter.getIp();
            case 1:
                return regCenter.getPort();
            case 2:
                return regCenter.getRegCenterType().getValue();
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public List<RegCenter> getRegCenters() {
        return regCenters;
    }

    public void setRegCenters(List<RegCenter> regCenters) {
        this.regCenters = regCenters;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

}
