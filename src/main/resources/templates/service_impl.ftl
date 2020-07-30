package ${packageName};

<#list imports as import>
import ${import};
</#list>

/**
* Service Impl: ${comment!''}服务实现
* @author ${author}
* @date ${dateStr}
*/
@Service("${baseClassName?uncap_first}Service")
public class ${className} implements ${baseClassName}Service, FrameworkService {

    private static Logger logger = LoggerFactory.getLogger(${className}.class);

    @Autowired
    private ${baseClassName}Dao ${baseClassName?uncap_first}Dao;

    /**
    * 新增数据
    * @param ${baseClassName?uncap_first}Vo
    * @return
    */
    @Override
    public int insert(${baseClassName}VO ${baseClassName?uncap_first}Vo) throws Exception {
        ${baseClassName} ${baseClassName?uncap_first} = new ${baseClassName}();
        beanCopy(${baseClassName?uncap_first}Vo, ${baseClassName?uncap_first});
        return ${baseClassName?uncap_first}Dao.insert(${baseClassName?uncap_first});
    }

    /**
    * 根据主键删除信息
    * @param ${baseClassName?uncap_first}Vo
    * @return
    */
    @Override
    public int deleteByPk(${baseClassName}VO ${baseClassName?uncap_first}Vo) throws Exception {
        ${baseClassName} ${baseClassName?uncap_first} = new ${baseClassName}();
        beanCopy(${baseClassName?uncap_first}Vo, ${baseClassName?uncap_first});
        return ${baseClassName?uncap_first}Dao.deleteByPk(${baseClassName?uncap_first});
    }

    /**
    * 根据主键更新信息
    * @param ${baseClassName?uncap_first}Vo
    * @return
    */
    @Override
    public int updateByPk(${baseClassName}VO ${baseClassName?uncap_first}Vo) throws Exception {
        ${baseClassName} ${baseClassName?uncap_first} = new ${baseClassName}();
        beanCopy(${baseClassName?uncap_first}Vo, ${baseClassName?uncap_first});
        return ${baseClassName?uncap_first}Dao.updateByPk(${baseClassName?uncap_first});
    }

    /**
    * 根据主键查询详情
    * @param in${baseClassName}Vo
    * @return
    */
    @Override
    public ${baseClassName}VO queryByPk(${baseClassName}VO in${baseClassName}Vo) throws Exception {
        ${baseClassName} query${baseClassName} = new ${baseClassName}();
        beanCopy(in${baseClassName}Vo, query${baseClassName});
        ${baseClassName} queryRsl${baseClassName} = ${baseClassName?uncap_first}Dao.queryByPk(query${baseClassName});
        return beanCopy(queryRsl${baseClassName}, new ${baseClassName}VO());
    }
}
