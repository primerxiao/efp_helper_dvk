package com.efp.common.constant;

public enum TemplateFileNameEnum {
    DO("do.ftl"),
    PO("po.ftl"),
    INPUT("input.ftl"),
    OUTPUT("output.ftl"),
    DAO("dao.ftl"),
    MAPPER("mapper.ftl"),
    FACADE("facade.ftl"),
    FACADEIMPL("facadeImpl.ftl"),
    REPOSITORY("repository.ftl"),
    REPOSITORYIMP("repositoryImpl.ftl"),;

    /**
     * 模板文件名称
     */
    private String fileName;

    TemplateFileNameEnum(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
