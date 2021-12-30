package ${generateInfo.packageName};

import com.fdb.smcts.core.vo.BaseInfo;
<#list generateInfo.imports as import>
import ${import};
</#list>

/**
* ${generateInfo.dasTable.comment!''}
* @date ${.now?string['yyyy-MM-dd']}
*/
public class ${generateInfo.className}  extends BaseInfo {
<#list generateInfo.classFields as classField>
    /**
    * ${classField.comment!''}
    */
    private ${classField.javaTypeClass.simpleName} ${classField.fieldName?uncap_first};
</#list>
<#list generateInfo.classFields as classField>

    public ${classField.javaTypeClass.simpleName} get${classField.fieldName}(){
        return ${classField.fieldName?uncap_first};
    }

    public void set${classField.fieldName}(${classField.javaTypeClass.simpleName} ${classField.fieldName?uncap_first}){
        this.${classField.fieldName?uncap_first}=${classField.fieldName?uncap_first};
    }
</#list>
}