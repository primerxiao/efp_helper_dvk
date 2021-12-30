package com.efp.plugins.project.gitmr.bean;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * @author primerxiao
 */
public class AppInfoTableModel extends AbstractTableModel {

    private List<AppInfo> appInfoList;

    private String[] columnNames = {"项目名", "项目id", "提交记录"};

    public AppInfoTableModel(List<AppInfo> appInfoList) {
        this.appInfoList = appInfoList;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return appInfoList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AppInfo appInfo = appInfoList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return appInfo.getProjectName();
            case 1:
                return appInfo.getProjectId();
            case 2:
                return appInfo.getCompareStatus();
            default:
                return null;
        }
    }

    public List<AppInfo> getAppInfoList() {
        return appInfoList;
    }

    public void setAppInfoList(List<AppInfo> appInfoList) {
        this.appInfoList = appInfoList;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }
}
