package ${generateInfo.packageName};
import com.fdb.a.${simpleBaseModuleNameMethod(generateInfo.baseMoudleName)}.acl.repository.${generateInfo.basicClassName}Repository;
import com.fdb.a.${simpleBaseModuleNameMethod(generateInfo.baseMoudleName)}.domain.entity.${generateInfo.basicClassName}DO;
import com.fdb.a.${simpleBaseModuleNameMethod(generateInfo.baseMoudleName)}.facade.${generateInfo.basicClassName}Service;
import com.fdb.smcts.core.vo.IsrvRspInfoOutput;
import com.fdb.a.${simpleBaseModuleNameMethod(generateInfo.baseMoudleName)}.facade.dto.${generateInfo.basicClassName}Input;
import com.fdb.a.${simpleBaseModuleNameMethod(generateInfo.baseMoudleName)}.facade.dto.${generateInfo.basicClassName}Output;
import com.fdb.smcts.core.base.FrameworkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service("${generateInfo.basicClassName?uncap_first}Service")
public class ${generateInfo.basicClassName}ServiceImpl extends FrameworkServiceImpl implements ${generateInfo.basicClassName}Service {

    private static Logger logger = LoggerFactory.getLogger(${generateInfo.basicClassName}ServiceImpl.class);

    @Autowired
    private ${generateInfo.basicClassName}Repository ${generateInfo.basicClassName?uncap_first}Repository;

    @Override
    public IsrvRspInfoOutput insertSingle(${generateInfo.basicClassName}Input input) {
        logger.info("新增授信关联客户信息，参数{}", input);
        int num = 0;
        try {
            num = ${generateInfo.basicClassName?uncap_first}Repository.insertSingle(mapperFacade.map(input, ${generateInfo.basicClassName}DO.class));
        } catch (Exception e) {
            logger.error("新增授信关联客户信息失败!", e);
            num = -1;
        }
        IsrvRspInfoOutput isrvRspInfoOutput = new IsrvRspInfoOutput();
        isrvRspInfoOutput.setRspCnt(num);
        return isrvRspInfoOutput;
    }

    @Override
    public IsrvRspInfoOutput deleteByPk(${generateInfo.basicClassName}Input input) {
        logger.info("删除授信关联客户信息，参数{}", input);
        int num = 0;
        try {
            num = ${generateInfo.basicClassName?uncap_first}Repository.deleteByPk(mapperFacade.map(input, ${generateInfo.basicClassName}DO.class));
        } catch (Exception e) {
            logger.error("删除授信关联客户信息失败!", e);
            num = -1;
        }
        IsrvRspInfoOutput isrvRspInfoOutput = new IsrvRspInfoOutput();
        isrvRspInfoOutput.setRspCnt(num);
        return isrvRspInfoOutput;
    }


    @Override
    public ${generateInfo.basicClassName}Output queryByPk(${generateInfo.basicClassName}Input input) {
        logger.info("跟据主键查询授信关联客户信息，参数{}", input);
        try {
            return mapperFacade.map(
                ${generateInfo.basicClassName?uncap_first}Repository.queryByPk(mapperFacade.map(input, ${generateInfo.basicClassName}DO.class)),
                ${generateInfo.basicClassName}Output.class
            );
        } catch (Exception e) {
            logger.error("跟据主键查询授信关联客户信息失败!", e);
            return null;
        }
    }

    @Override
    public IsrvRspInfoOutput updateByPk(${generateInfo.basicClassName}Input input) {
        logger.info("更新授信关联客户信息，参数{}", input);
        int num = 0;
        try {
            num = ${generateInfo.basicClassName?uncap_first}Repository.updateByPk(mapperFacade.map(input, ${generateInfo.basicClassName}DO.class));
        } catch (Exception e) {
            logger.error("更新授信关联客户信息失败!", e);
            num = -1;
        }
        IsrvRspInfoOutput isrvRspInfoOutput = new IsrvRspInfoOutput();
        isrvRspInfoOutput.setRspCnt(num);
        return isrvRspInfoOutput;
    }
}
