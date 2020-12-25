package com.efp.plugins.frame.dubbo.service;

/**
 * 发起dubbo调用需要的参数
 *
 * @author gcb
 */
public class DubboCallParam {
    public String applicationName;
    public String registryAddress;
    public String referenceInterface;
    public String referenceVersion;
    public boolean referenceGeneric;
    public String referenceGroup;
    public String invokeMethod;
    public String[] invokeMethodParamType;
    public Object[] invokeMethodParam;



    public DubboCallParam(String applicationName, String registryAddress, String referenceInterface, String referenceVersion, boolean referenceGeneric, String referenceGroup, String invokeMethod, String[] invokeMethodParamType, Object[] invokeMethodParam) {
        this.applicationName = applicationName;
        this.registryAddress = registryAddress;
        this.referenceInterface = referenceInterface;
        this.referenceVersion = referenceVersion;
        this.referenceGeneric = referenceGeneric;
        this.referenceGroup = referenceGroup;
        this.invokeMethod = invokeMethod;
        this.invokeMethodParamType = invokeMethodParamType;
        this.invokeMethodParam = invokeMethodParam;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public String getReferenceInterface() {
        return referenceInterface;
    }

    public void setReferenceInterface(String referenceInterface) {
        this.referenceInterface = referenceInterface;
    }

    public String getReferenceVersion() {
        return referenceVersion;
    }

    public void setReferenceVersion(String referenceVersion) {
        this.referenceVersion = referenceVersion;
    }

    public boolean isReferenceGeneric() {
        return referenceGeneric;
    }

    public void setReferenceGeneric(boolean referenceGeneric) {
        this.referenceGeneric = referenceGeneric;
    }

    public String getReferenceGroup() {
        return referenceGroup;
    }

    public void setReferenceGroup(String referenceGroup) {
        this.referenceGroup = referenceGroup;
    }

    public String getInvokeMethod() {
        return invokeMethod;
    }

    public void setInvokeMethod(String invokeMethod) {
        this.invokeMethod = invokeMethod;
    }

    public String[] getInvokeMethodParamType() {
        return invokeMethodParamType;
    }

    public void setInvokeMethodParamType(String[] invokeMethodParamType) {
        this.invokeMethodParamType = invokeMethodParamType;
    }

    public Object[] getInvokeMethodParam() {
        return invokeMethodParam;
    }

    public void setInvokeMethodParam(Object[] invokeMethodParam) {
        this.invokeMethodParam = invokeMethodParam;
    }
}
