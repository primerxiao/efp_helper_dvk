package com.irdstudio.efp.loan.service.impl;

public class ServiceImpl  {

    @Override
    public int insertOrUpdate(List<${generateJava.voClassName}> list) {
        List<${generateJava.domainClassName}> domainList = new ArrayList<${generateJava.domainClassName}>();
        int num = 0;
        try {
            domainList = (List<${generateJava.domainClassName}>) beansCopy(list, ${generateJava.domainClassName}.class);
            num = ${generateJava.daoClassName?uncap_first}.insertOrUpdate(domainList);
            String num_msg = "【${dasTable.comment}】插入或更新数据处理结果:" + num;
            logger.info(num_msg, "message{}");
        } catch (Exception e) {
            String msg2 = "【${dasTable.comment}】插入或更新数据出现异常:" + e;
            logger.error(msg2, "message{}");
            num = -1;
        }
        return num;
    }

 }