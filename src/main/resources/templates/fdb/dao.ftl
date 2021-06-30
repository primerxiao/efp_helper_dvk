package ${generateInfo.packageName};
<#assign x = "${generateInfo.baseMoudleName}">
<#list generateInfo.imports as import>
import ${import};
</#list>
import com.fdb.a.${simpleBaseModuleNameMethod(x)}.infra.persistence.po.${generateInfo.basicClassName}PO;


/**
* ${generateInfo.dasTable.comment!''}
* @date ${.now?string['yyyy-MM-dd']}
*/
public interface ${generateInfo.className} {

    /**
    * 新增数据
    * @param  ${generateInfo.basicClassName?uncap_first}PO 需要新增的信息
    * @return int
    */
    int insertSingle(${generateInfo.basicClassName}PO ${generateInfo.basicClassName?uncap_first}PO);

    /**
    * 根据主键删除信息
    * @param ${generateInfo.basicClassName?uncap_first}PO 主键参数对象
    * @return int
    */
    int deleteByPk(${generateInfo.basicClassName}PO ${generateInfo.basicClassName?uncap_first}PO);

    /**
    * 根据主键查询信息
    * @param ${generateInfo.basicClassName?uncap_first}PO 查询条件
    * @return ${generateInfo.basicClassName}PO
    */
    ${generateInfo.basicClassName}PO queryByPk(${generateInfo.basicClassName}PO ${generateInfo.basicClassName?uncap_first}PO);

    /**
    * 根据主键更新信息
    * @param ${generateInfo.basicClassName?uncap_first}PO 需要更新的数据
    * @return int
    */
    int updateByPk(${generateInfo.basicClassName}PO ${generateInfo.basicClassName?uncap_first}PO);

}