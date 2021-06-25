package com.fdb.a.<#list currentModule.name?split("-") as baseMoudleName>${baseMoudleName}<#if field_index==1></#if></#list>.infra.persistence.po;

import com.fdb.smcts.core.vo.BaseInfo;
<#list imports as import>
    import ${import};
</#list>

/**
* ${dasTable.comment!''}
* @date ${.now?string['yyyy-MM-dd']}
*/
public class ${className}  extends BaseInfo {
<#list dasColumns as classField>
    /**
    * ${classField.comment!''}
    */
    private ${classField.javaTypeClass.simpleName} ${classField.fieldName?uncap_first};
</#list>
<#list classFields as classField>

    public ${classField.javaTypeClass.simpleName} get${classField.fieldName}(){
    return ${classField.fieldName?uncap_first};
    }

    public void set${classField.fieldName}(${classField.javaTypeClass.simpleName} ${classField.fieldName?uncap_first}){
    this.${classField.fieldName?uncap_first}=${classField.fieldName?uncap_first};
    }
</#list>
}