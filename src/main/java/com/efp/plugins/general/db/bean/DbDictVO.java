package com.efp.plugins.general.db.bean;

/**
 * @author 86134
 */
public class DbDictVO {
    //@ExcelProperty("表英文名")
    private String tableNameEn;
    //@ExcelProperty("表中文名")
    private String tableNameCn;
    //@ExcelProperty("字段英文名")
    private String columnNameEn;
    //@ExcelProperty("字段中文名")
    private String columnNameCn;
    //@ExcelProperty("字段类型")
    private String columnType;
    //@ExcelProperty("字段长度")
    private String columnLength;
    //@ExcelProperty("主键标志")
    private String pkFlag;
    //@ExcelProperty("允许为空")
    private String allowNull;
    //@ExcelProperty("默认值")
    private String defaultValue;

    @Override
    public String toString() {
        return "DbDictVO{" +
                "tableNameEn='" + tableNameEn + '\'' +
                ", tableNameCn=" + tableNameCn +
                ", columnNameEn='" + columnNameEn + '\'' +
                ", columnNameCn='" + columnNameCn + '\'' +
                ", columnType='" + columnType + '\'' +
                ", columnLength='" + columnLength + '\'' +
                ", pkFlag='" + pkFlag + '\'' +
                ", allowNull='" + allowNull + '\'' +
                '}';
    }

    public String getTableNameEn() {
        return tableNameEn;
    }

    public void setTableNameEn(String tableNameEn) {
        this.tableNameEn = tableNameEn;
    }

    public String getTableNameCn() {
        return tableNameCn;
    }

    public void setTableNameCn(String tableNameCn) {
        this.tableNameCn = tableNameCn;
    }

    public String getColumnNameEn() {
        return columnNameEn;
    }

    public void setColumnNameEn(String columnNameEn) {
        this.columnNameEn = columnNameEn;
    }

    public String getColumnNameCn() {
        return columnNameCn;
    }

    public void setColumnNameCn(String columnNameCn) {
        this.columnNameCn = columnNameCn;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(String columnLength) {
        this.columnLength = columnLength;
    }

    public String getPkFlag() {
        return pkFlag;
    }

    public void setPkFlag(String pkFlag) {
        this.pkFlag = pkFlag;
    }

    public String getAllowNull() {
        return allowNull;
    }

    public void setAllowNull(String allowNull) {
        this.allowNull = allowNull;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
