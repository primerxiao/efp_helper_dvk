package ${generateInfo.packageName};

<#list generateInfo.imports as import>
import ${import};
</#list>
import org.sel4j.Logger;
import org.sel4j.LoggerFactory;


/**
* ${generateInfo.dasTable.comment!''}
* @date ${.now?string['yyyy-MM-dd']}
*/
public class ${generateInfo.className}  extends FrameworkServiceImpl implements ${generateInfo.basicClassName}Repository {

    private final static Logger logger = LoggerFactory.getLogger(${generateInfo.basicClassName}RepositoryImpl.class);

    @Autowired
    private ${generateInfo.basicClassName}Mapper ${generateInfo.basicClassName?uncap_first}Mapper;

    /**
    * 新增一条记录
    */
    @Override
    public int insertSingle(${generateInfo.basicClassName}DO in${generateInfo.basicClassName}DO) {
        logger.debug("当前新增数据为:" + in${generateInfo.basicClassName}DO.toString());
        int num = 0;
        try {
            num = ${generateInfo.basicClassName?uncap_first}Mapper.insertSingle(mapperFacade.map(in${generateInfo.basicClassName}DO, ${generateInfo.basicClassName}PO.class));
        } catch (Exception e) {
            logger.error("新增数据发生异常!", e);
            num = -1;
        }
        logger.debug("当前新增数据条数为:" + num);
        return num;
    }

    /**
    * 根据主键删除记录
    */
    @Override
    public int deleteByPk(${generateInfo.basicClassName}DO in${generateInfo.basicClassName}DO) {
        logger.debug("当前删除数据为:" + in${generateInfo.basicClassName}DO.toString());
        int num = 0;
        try {
            num = ${generateInfo.basicClassName?uncap_first}Mapper.deleteByPk(mapperFacade.map(in${generateInfo.basicClassName}DO, ${generateInfo.basicClassName}PO.class));
        } catch (Exception e) {
            logger.error("删除数据发生异常!", e);
            num = -1;
        }
        logger.debug("根据条件:" + in${generateInfo.basicClassName}DO + "删除数据条数为" + num);
        return num;
    }

    /**
    * 根据主键修改单条记录
    */
    @Override
    public int updateByPk(${generateInfo.basicClassName}DO in${generateInfo.basicClassName}DO) {
        logger.debug("当前修改数据为:" + in${generateInfo.basicClassName}DO.toString());
        int num = 0;
        try {
            num = ${generateInfo.basicClassName?uncap_first}Mapper.updateByPk(mapperFacade.map(in${generateInfo.basicClassName}DO, ${generateInfo.basicClassName}PO.class));
        } catch (Exception e) {
            logger.error("修改数据发生异常!", e);
            num = -1;
        }
        logger.debug("根据条件:" + in${generateInfo.basicClassName}DO + "修改的数据条数为" + num);
        return num;
    }

    /**
    * 根据主键查询单条记录
    */
    @Override
    public ${generateInfo.basicClassName}DO queryByPk(${generateInfo.basicClassName}DO in${generateInfo.basicClassName}DO) {
        logger.debug("当前查询参数信息为:" + in${generateInfo.basicClassName}DO);
        try {
            ${generateInfo.basicClassName}PO queryRsl${generateInfo.basicClassName}PO = ${generateInfo.basicClassName?uncap_first}Mapper.queryByPk(mapperFacade.map(in${generateInfo.basicClassName}DO, ${generateInfo.basicClassName}PO.class));
            if (Objects.nonNull(queryRsl${generateInfo.basicClassName}PO)) {
                logger.debug("当前查询结果为:" + queryRsl${generateInfo.basicClassName}PO.toString());
                return mapperFacade.map(queryRsl${generateInfo.basicClassName}PO, ${generateInfo.basicClassName}DO.class);
            }
            logger.debug("当前查询结果为空!");
            return null;
        } catch (Exception e) {
            logger.error("查询数据发生异常!", e);
        }
        return null;
    }

}