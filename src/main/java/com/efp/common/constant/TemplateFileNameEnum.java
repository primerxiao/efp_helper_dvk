package com.efp.common.constant;

import com.efp.plugins.codeHelper.bean.ServiceImpl;

public enum TemplateFileNameEnum {
    DOMAIN("domain.ftl"), VO("vo.ftl"), DAO("dao.ftl"), MAPPER("mapper.ftl"), SERVICE("service.ftl"), SERVICEIMPL("service_impl");
    private String fileName;
    TemplateFileNameEnum(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
