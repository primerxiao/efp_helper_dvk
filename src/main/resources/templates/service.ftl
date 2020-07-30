package ${packageName};

<#list imports as import>
import ${import};
</#list>

/**
* Service Interface: ${comment!''}
* @author ${author}
* @date ${dateStr}
*/
public interface ${className} {
    /**
    * 新增数据
    * @param  ${baseClassName?uncap_first}VO
    * @return
    */
    int insert(${baseClassName}VO  ${baseClassName?uncap_first}VO) throws Exception;

    /**
    * 根据主键删除信息
    * @param ${baseClassName?uncap_first}VO
    * @return
    */
    int deleteByPk(${baseClassName}VO ${baseClassName?uncap_first}VO) throws Exception;

    /**
    * 根据主键更新信息
    * @param ${baseClassName?uncap_first}VO
    * @return
    */
    int updateByPk(${baseClassName}VO ${baseClassName?uncap_first}VO) throws Exception;

    /**
    * 根据主键查询详情
    * @param ${baseClassName?uncap_first}VO
    * @return
    */
    ${baseClassName}VO queryByPk(${baseClassName}VO ${baseClassName?uncap_first}VO) throws Exception;
}