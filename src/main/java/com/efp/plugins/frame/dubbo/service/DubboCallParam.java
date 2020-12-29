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
    public String id;

    public DubboCallParam(String applicationName, String registryAddress, String referenceInterface, String referenceVersion, boolean referenceGeneric, String referenceGroup, String invokeMethod, String[] invokeMethodParamType, Object[] invokeMethodParam, String id) {
        this.applicationName = applicationName;
        this.registryAddress = registryAddress;
        this.referenceInterface = referenceInterface;
        this.referenceVersion = referenceVersion;
        this.referenceGeneric = referenceGeneric;
        this.referenceGroup = referenceGroup;
        this.invokeMethod = invokeMethod;
        this.invokeMethodParamType = invokeMethodParamType;
        this.invokeMethodParam = invokeMethodParam;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static final class Builder {
        public String applicationName;
        public String registryAddress;
        public String referenceInterface;
        public String referenceVersion;
        public boolean referenceGeneric;
        public String referenceGroup;
        public String invokeMethod;
        public String[] invokeMethodParamType;
        public Object[] invokeMethodParam;
        public String id;

        private Builder() {
        }

        public static Builder aDubboCallParam() {
            return new Builder();
        }

        public Builder withApplicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public Builder withRegistryAddress(String registryAddress) {
            this.registryAddress = registryAddress;
            return this;
        }

        public Builder withReferenceInterface(String referenceInterface) {
            this.referenceInterface = referenceInterface;
            return this;
        }

        public Builder withReferenceVersion(String referenceVersion) {
            this.referenceVersion = referenceVersion;
            return this;
        }

        public Builder withReferenceGeneric(boolean referenceGeneric) {
            this.referenceGeneric = referenceGeneric;
            return this;
        }

        public Builder withReferenceGroup(String referenceGroup) {
            this.referenceGroup = referenceGroup;
            return this;
        }

        public Builder withInvokeMethod(String invokeMethod) {
            this.invokeMethod = invokeMethod;
            return this;
        }

        public Builder withInvokeMethodParamType(String[] invokeMethodParamType) {
            this.invokeMethodParamType = invokeMethodParamType;
            return this;
        }

        public Builder withInvokeMethodParam(Object[] invokeMethodParam) {
            this.invokeMethodParam = invokeMethodParam;
            return this;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public DubboCallParam build() {
            return new DubboCallParam(applicationName, registryAddress, referenceInterface, referenceVersion, referenceGeneric, referenceGroup, invokeMethod, invokeMethodParamType, invokeMethodParam, id);
        }
    }
}
