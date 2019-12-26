package com.efp.common.data;

/**
 * @author HIFeng
 */
public class EfpData {
    private String moduleName;
    private String xmlFileName;
    private int dubboType;

    public EfpData(String moduleName, String xmlFileName, int dubboType) {
        this.moduleName = moduleName;
        this.xmlFileName = xmlFileName;
        this.dubboType = dubboType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getXmlFileName() {
        return xmlFileName;
    }

    public void setXmlFileName(String xmlFileName) {
        this.xmlFileName = xmlFileName;
    }

    public int getDubboType() {
        return dubboType;
    }

    public void setDubboType(int dubboType) {
        this.dubboType = dubboType;
    }
}

