package com.efp.plugins.diffcompare.config;

public class DiffCompareCacheConfig {
    private String cacheKey = "DiffCompareCacheConfig";
    //"差异类型", "模块", "文件全路径", "处理方案"
    private String[][] basicData = {
            {"新增","efp.batch.middle","D:\\Psd\\BasicBranch-dev-PSD\\efp.batch.middle\\pom.xml",""},
            {"新增","efp.batch.middle","D:\\Psd\\BasicBranch-dev-PSD\\efp.batch.middle\\pom.xml",""}
    };
    private int diffCompareListIndex;
    private String moduleComboBoxValue;
    private String fileTextFieldValue;
    private String diffTypeComboBoxValue;

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String[][] getBasicData() {
        return basicData;
    }

    public void setBasicData(String[][] basicData) {
        this.basicData = basicData;
    }

    public int getDiffCompareListIndex() {
        return diffCompareListIndex;
    }

    public void setDiffCompareListIndex(int diffCompareListIndex) {
        this.diffCompareListIndex = diffCompareListIndex;
    }

    public String getModuleComboBoxValue() {
        return moduleComboBoxValue;
    }

    public void setModuleComboBoxValue(String moduleComboBoxValue) {
        this.moduleComboBoxValue = moduleComboBoxValue;
    }

    public String getFileTextFieldValue() {
        return fileTextFieldValue;
    }

    public void setFileTextFieldValue(String fileTextFieldValue) {
        this.fileTextFieldValue = fileTextFieldValue;
    }

    public String getDiffTypeComboBoxValue() {
        return diffTypeComboBoxValue;
    }

    public void setDiffTypeComboBoxValue(String diffTypeComboBoxValue) {
        this.diffTypeComboBoxValue = diffTypeComboBoxValue;
    }
}
