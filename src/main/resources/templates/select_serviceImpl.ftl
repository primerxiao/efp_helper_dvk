package com.irdstudio.efp.loan.service.impl;

public class ServiceImpl  {
    @Override
    public List<${generateJava.voClassName}> ${generateJava.currentMethodName}(${generateJava.voClassName} ${generateJava.voClassName?uncap_first})  throws Exception {
            return (List<${generateJava.voClassName}>) beansCopy(${generateJava.daoClassName?uncap_first}.${generateJava.currentMethodName}(beanCopy(${generateJava.voClassName?uncap_first},${generateJava.domainClassName}.class)), ${generateJava.voClassName}.class);
    }

 }