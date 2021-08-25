package ${generateInfo.packageName};

<#assign x = "${generateInfo.baseMoudleName}">
import com.fdb.a.${simpleBaseModuleNameMethod(x)}.acl.repository.${generateInfo.basicClassName}Repository;
import com.fdb.a.${simpleBaseModuleNameMethod(x)}.domain.entity.${generateInfo.basicClassName}DO;
import com.fdb.a.${simpleBaseModuleNameMethod(x)}.infra.persistence.mapper.${generateInfo.basicClassName}Mapper;
import com.fdb.a.${simpleBaseModuleNameMethod(x)}.infra.persistence.po.${generateInfo.basicClassName}PO;
import com.fdb.smcts.core.base.FrameworkServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;


/**
* ${generateInfo.dasTable.comment!''}
* @date ${.now?string['yyyy-MM-dd']}
*/
@Service("${generateInfo.basicClassName?uncap_first}Repository")
public class ${generateInfo.className}  extends FrameworkServiceImpl implements ${generateInfo.basicClassName}Repository {

    private final static Logger logger = LoggerFactory.getLogger(${generateInfo.basicClassName}RepositoryImpl.class);

    @Autowired
    private ${generateInfo.basicClassName}Mapper ${generateInfo.basicClassName?uncap_first}Mapper;

    /**
    * 新增一条记录
    */
    @Override
    public int insertSingle(${generateInfo.basicClassName}DO in${generateInfo.basicClassName}DO) {
        return ${generateInfo.basicClassName?uncap_first}Mapper.insertSingle(mapperFacade.map(in${generateInfo.basicClassName}DO, ${generateInfo.basicClassName}PO.class));
    }

    /**
    * 根据主键删除记录
    */
    @Override
    public int deleteByPk(${generateInfo.basicClassName}DO in${generateInfo.basicClassName}DO) {
        return ${generateInfo.basicClassName?uncap_first}Mapper.deleteByPk(mapperFacade.map(in${generateInfo.basicClassName}DO, ${generateInfo.basicClassName}PO.class));
    }

    /**
    * 根据主键修改单条记录
    */
    @Override
    public int updateByPk(${generateInfo.basicClassName}DO in${generateInfo.basicClassName}DO) {
        return ${generateInfo.basicClassName?uncap_first}Mapper.updateByPk(mapperFacade.map(in${generateInfo.basicClassName}DO, ${generateInfo.basicClassName}PO.class));
    }

    /**
    * 根据主键查询单条记录
    */
    @Override
    public ${generateInfo.basicClassName}DO queryByPk(${generateInfo.basicClassName}DO in${generateInfo.basicClassName}DO) {
        return mapperFacade.map(${generateInfo.basicClassName?uncap_first}Mapper.queryByPk(mapperFacade.map(in${generateInfo.basicClassName}DO, ${generateInfo.basicClassName}PO.class)), ${generateInfo.basicClassName}DO.class);
    }

}