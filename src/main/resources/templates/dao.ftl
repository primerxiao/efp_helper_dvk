package ${packageName};

<#list imports as import>
import ${import};
</#list>
import com.fdb.a.smcpi.infra.persistence.po.CrdtApplInfoPO;

/**
 * DAO Interface: ${comment!''}
 * @author ${author}
 * @date ${dateStr}
*/
public interface ${className} {
    /**
    * 新增数据
    * @param  ${baseClassName?uncap_first}
    * @return
    */
    int insert(${baseClassName}  ${baseClassName?uncap_first});

    /**
    * 根据主键删除信息
    * @param ${baseClassName?uncap_first}
    * @return
    */
    int deleteByPk(${baseClassName} ${baseClassName?uncap_first});

    /**
    * 根据主键更新信息
    * @param ${baseClassName?uncap_first}
    * @return
    */
    int updateByPk(${baseClassName} ${baseClassName?uncap_first});

    /**
    * 根据主键查询详情
    * @param ${baseClassName?uncap_first}
    * @return
    */
    ${baseClassName} queryByPk(${baseClassName} ${baseClassName?uncap_first});
}