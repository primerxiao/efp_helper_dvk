package com.irdstudio.efp.loan.service.impl;

public class ServiceImpl  {
    @Override
    public List<${generateJava.voClassName}> ${generateJava.currentMethodName}(${generateJava.voClassName} ${generateJava.voClassName?uncap_first}) {
        try {
            return (List<${generateJava.voClassName}>) beansCopy(${generateJava.daoClassName?uncap_first}.${generateJava.currentMethodName}(${generateJava.voClassName?uncap_first}), ${generateJava.voClassName}.class);
        } catch (Exception e) {
            logger.error("数据转换出现异常!", e);
        }
        return null;
    }

 }