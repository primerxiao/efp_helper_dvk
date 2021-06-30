package ${generateInfo.packageName};

<#assign x = "${generateInfo.baseMoudleName}">
import com.fdb.a.${simpleBaseModuleNameMethod(x)}.domain.entity.${generateInfo.basicClassName}DO;

/**
* ${generateInfo.dasTable.comment!''}
* @date ${.now?string['yyyy-MM-dd']}
*/
public interface ${generateInfo.className} {
    /**
    * 新增数据
    * @param  ${generateInfo.basicClassName?uncap_first}DO
    * @return
    */
    int insertSingle(${generateInfo.basicClassName}DO ${generateInfo.basicClassName?uncap_first}DO);

    /**
    * 根据主键删除信息
    * @param ${generateInfo.basicClassName?uncap_first}DO
    * @return
    */
    int deleteByPk(${generateInfo.basicClassName}DO ${generateInfo.basicClassName?uncap_first}DO);

    /**
    * 根据主键更新信息
    * @param ${generateInfo.basicClassName?uncap_first}DO
    * @return
    */
    int updateByPk(${generateInfo.basicClassName}DO ${generateInfo.basicClassName?uncap_first}DO);

    /**
    * 根据主键查询详情
    * @param ${generateInfo.basicClassName?uncap_first}DO
    * @return
    */
    ${generateInfo.basicClassName}DO queryByPk(${generateInfo.basicClassName}DO ${generateInfo.basicClassName?uncap_first}DO);
}