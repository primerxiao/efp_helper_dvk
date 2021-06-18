package com.efp.common.constant;

public enum TemplateFileNameEnum {
    DO("domain.ftl"),
    INPUT("vo.ftl"),
    OUTPUT("vo.ftl"),
    DAO("dao.ftl"),
    MAPPER("mapper.ftl"),
    FACADE("service.ftl"),
    FACADEIMPL("service_impl.ftl"),
    REPOSITORY("controller.ftl"),
    APPLICATION("controller.ftl");
    private String fileName;
    TemplateFileNameEnum(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
