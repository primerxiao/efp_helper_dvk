package com.irdstudio.efp.loan.service.impl;

public class ServiceImpl  {

    @Override
    public ${generateJava.voClassName} ${generateJava.currentMethodName}(${generateJava.voClassName} ${generateJava.voClassName?uncap_first}) {
        ${generateJava.domainClassName} ${generateJava.domainClassName?uncap_first} = new ${generateJava.domainClassName}();
        try {
            ${generateJava.domainClassName?uncap_first} = (${generateJava.domainClassName}) beanCopy(${generateJava.voClassName?uncap_first}, ${generateJava.domainClassName}.class);
            return ${generateJava.daoClassName?uncap_first}.${generateJava.currentMethodName}(${generateJava.domainClassName?uncap_first});
        } catch (Exception e) {
            logger.error("【${dasTable.comment}】插入或更新数据出现异常:", e);
        }
        return null;
    }

 }