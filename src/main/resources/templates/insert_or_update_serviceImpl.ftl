package com.irdstudio.efp.loan.service.impl;

public class ServiceImpl  {

    @Override
    public int insertOrUpdate(List<${generateJava.voClassName}> list) throw Exception {
        List<${generateJava.domainClassName}> domainList = new ArrayList<${generateJava.domainClassName}>();
        domainList = (List<${generateJava.domainClassName}>) beansCopy(list, ${generateJava.domainClassName}.class);
        return ${generateJava.daoClassName?uncap_first}.insertOrUpdate(domainList);
    }

 }