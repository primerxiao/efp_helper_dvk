package com.efp.plugins.settings.bean;

import com.efp.common.constant.RegCenterTypeEnum;

public class RegCenter {

    private String ip;

    private String port;

    private RegCenterTypeEnum regCenterType;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public RegCenterTypeEnum getRegCenterType() {
        return regCenterType;
    }

    public void setRegCenterType(RegCenterTypeEnum regCenterType) {
        this.regCenterType = regCenterType;
    }

    public RegCenter(String ip, String port, RegCenterTypeEnum regCenterType) {
        this.ip = ip;
        this.port = port;
        this.regCenterType = regCenterType;
    }

    public RegCenter() {
        super();
    }

    @Override
    public String toString() {
        return regCenterType.getValue() + " " + ip + " " + port;
    }

    @Override
    public boolean equals(Object obj) {
        RegCenter regCenter = (RegCenter) obj;
        return ip.equals(regCenter.getIp()) &&
                port.equals(regCenter.getPort()) &&
                regCenterType.getValue().equals(regCenter.getRegCenterType().getValue());
    }
}
