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
    public int insert(${baseClassName}VO ${baseClassName?uncap_first}Vo) {
        logger.debug("当前新增数据为:"+ ${baseClassName?uncap_first}Vo.toString());
        int num = 0;
        try {
            ${baseClassName} ${baseClassName?uncap_first} = new ${baseClassName}();
            beanCopy(${baseClassName?uncap_first}Vo, ${baseClassName?uncap_first});
            num = ${baseClassName?uncap_first}Dao.insert(${baseClassName?uncap_first});
        } catch (Exception e) {
            logger.error("新增数据发生异常!", e);
            num = -1;
        }
        logger.debug("当前新增数据条数为:"+ num);
        return num;
    }

    /**
    * 根据主键删除信息
    * @param ${baseClassName?uncap_first}Vo
    * @return
    */
    @Override
    public int deleteByPk(${baseClassName}VO ${baseClassName?uncap_first}Vo) {
        logger.debug("当前删除数据条件为:"+ ${baseClassName?uncap_first}Vo);
        int num = 0;
        try {
            ${baseClassName} ${baseClassName?uncap_first} = new ${baseClassName}();
            beanCopy(${baseClassName?uncap_first}Vo, ${baseClassName?uncap_first});
            num = ${baseClassName?uncap_first}Dao.deleteByPk(${baseClassName?uncap_first});
        } catch (Exception e) {
            logger.error("删除数据发生异常!", e);
            num = -1;
        }
        logger.debug("根据条件:"+ ${baseClassName?uncap_first}Vo +"删除的数据条数为"+ num);
        return num;
    }

    /**
    * 根据主键更新信息
    * @param ${baseClassName?uncap_first}Vo
    * @return
    */
    @Override
    public int updateByPk(${baseClassName}VO ${baseClassName?uncap_first}Vo) {
        logger.debug("当前修改数据为:"+ ${baseClassName?uncap_first}Vo.toString());
        int num = 0;
        try {
            ${baseClassName} ${baseClassName?uncap_first} = new ${baseClassName}();
            beanCopy(${baseClassName?uncap_first}Vo, ${baseClassName?uncap_first});
            num = ${baseClassName?uncap_first}Dao.updateByPk(${baseClassName?uncap_first});
        } catch (Exception e) {
            logger.error("修改数据发生异常!", e);
            num = -1;
        }
        logger.debug("根据条件:"+ ${baseClassName?uncap_first}Vo +"修改的数据条数为"+ num);
        return num;
    }

    /**
    * 根据主键查询详情
    * @param in${baseClassName}Vo
    * @return
    */
    @Override
    public ${baseClassName}VO queryByPk(${baseClassName}VO in${baseClassName}Vo) {
        logger.debug("当前查询参数信息为:"+ in${baseClassName}Vo);
        try {
            ${baseClassName} query${baseClassName} = new ${baseClassName}();
            beanCopy(in${baseClassName}Vo, query${baseClassName});
            ${baseClassName} queryRsl${baseClassName} = ${baseClassName?uncap_first}Dao.queryByPk(query${baseClassName});
            if (Objects.nonNull(queryRsl${baseClassName})) {
                return beanCopy(queryRsl${baseClassName}, new ${baseClassName}VO());
            } else {
                logger.debug("当前查询结果为空!");
            }
        } catch (Exception e) {
            logger.error("查询数据发生异常!", e);
        }
        return null;
    }
}
