package com.efp.dvk.plugins.db.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Data
public class DbConnectParam {

    private String driverClass;

    private String baseUrl;

    private String schema;

    private String userName;

    private String password;

    private String params;

    public String getConnectUrl() {
        return StringUtils.join(
                baseUrl,
                "/",
                schema,
                "?",
                StringUtils.isEmpty(params) ? "" : params
        );
    }
}
