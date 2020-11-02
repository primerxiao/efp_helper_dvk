package com.efp.common.constant;

public enum TemplateFileNameEnum {
    DOMAIN("domain.ftl"),
    VO("vo.ftl"),
    DAO("dao.ftl"),
    MAPPER("mapper.ftl"),
    SERVICE("service.ftl"),
    SERVICEIMPL("service_impl.ftl"),
    CONTROLLER("controller.ftl");
    private String fileName;
    TemplateFileNameEnum(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
