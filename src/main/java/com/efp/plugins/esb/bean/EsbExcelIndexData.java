package com.efp.plugins.esb.bean;

public class EsbExcelIndexData {
    //接口ID
    private String id;
    //交易代码
    private String dealCode;
    //交易名称
    private String dealName;
    //服务名称
    private String serviceName;
    //场景名称
    private String stageName;
    //服务消费者
    private String serviceConsumer;
    //服务提供者
    private String serviceProvider;
    //所属产品
    private String product;
    //修订人
    private String reviser;
    //修订日期
    private String revisionDate;
    //报文格式
    private String messageFormat;
    //备注
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDealCode() {
        return dealCode;
    }

    public void setDealCode(String dealCode) {
        this.dealCode = dealCode;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getServiceConsumer() {
        return serviceConsumer;
    }

    public void setServiceConsumer(String serviceConsumer) {
        this.serviceConsumer = serviceConsumer;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getReviser() {
        return reviser;
    }

    public void setReviser(String reviser) {
        this.reviser = reviser;
    }

    public String getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(String revisionDate) {
        this.revisionDate = revisionDate;
    }

    public String getMessageFormat() {
        return messageFormat;
    }

    public void setMessageFormat(String messageFormat) {
        this.messageFormat = messageFormat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "EsbExcelIndexData{" +
                "id='" + id + '\'' +
                ", dealCode='" + dealCode + '\'' +
                ", dealName='" + dealName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", stageName='" + stageName + '\'' +
                ", serviceConsumer='" + serviceConsumer + '\'' +
                ", serviceProvider='" + serviceProvider + '\'' +
                ", product='" + product + '\'' +
                ", reviser='" + reviser + '\'' +
                ", revisionDate='" + revisionDate + '\'' +
                ", messageFormat='" + messageFormat + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
