package ${packageName};

import com.irdstudio.basic.framework.core.vo.BaseInfo;
<#list imports as import>
import ${import};
</#list>

/**
* ${comment!''}
* @author ${author}
* @date ${dateStr}
*/
public class ${className}  extends BaseInfo {
<#list classFields as classField>
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