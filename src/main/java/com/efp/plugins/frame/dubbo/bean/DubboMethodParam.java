package com.efp.plugins.frame.dubbo.bean;

/**
 * @author 肖均辉
 */
public class DubboMethodParam {
    private String index;
    private String type;
    private String value;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public static final class DubboMethodParamBuilder {
        private String index;
        private String type;
        private String value;

        private DubboMethodParamBuilder() {
        }

        public static DubboMethodParamBuilder aDubboMethodParam() {
            return new DubboMethodParamBuilder();
        }

        public DubboMethodParamBuilder withIndex(String index) {
            this.index = index;
            return this;
        }

        public DubboMethodParamBuilder withType(String type) {
            this.type = type;
            return this;
        }

        public DubboMethodParamBuilder withValue(String value) {
            this.value = value;
            return this;
        }

        public DubboMethodParam build() {
            DubboMethodParam dubboMethodParam = new DubboMethodParam();
            dubboMethodParam.setIndex(index);
            dubboMethodParam.setType(type);
            dubboMethodParam.setValue(value);
            return dubboMethodParam;
        }
    }
}
