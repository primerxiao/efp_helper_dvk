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
        int num = ${generateInfo.basicClassName?uncap_first}Repository.insertSingle(mapperFacade.map(input, ${generateInfo.basicClassName}DO.class));
        IsrvRspInfoOutput isrvRspInfoOutput = new IsrvRspInfoOutput();
        isrvRspInfoOutput.setRspCnt(num);
        return isrvRspInfoOutput;
    }

    @Override
    public IsrvRspInfoOutput deleteByPk(${generateInfo.basicClassName}Input input) {
        int num = ${generateInfo.basicClassName?uncap_first}Repository.deleteByPk(mapperFacade.map(input, ${generateInfo.basicClassName}DO.class));
        IsrvRspInfoOutput isrvRspInfoOutput = new IsrvRspInfoOutput();
        isrvRspInfoOutput.setRspCnt(num);
        return isrvRspInfoOutput;
    }


    @Override
    public ${generateInfo.basicClassName}Output queryByPk(${generateInfo.basicClassName}Input input) {
        return mapperFacade.map(
            ${generateInfo.basicClassName?uncap_first}Repository.queryByPk(mapperFacade.map(input, ${generateInfo.basicClassName}DO.class)),
            ${generateInfo.basicClassName}Output.class
        );
    }

    @Override
    public IsrvRspInfoOutput updateByPk(${generateInfo.basicClassName}Input input) {
        int num = ${generateInfo.basicClassName?uncap_first}Repository.updateByPk(mapperFacade.map(input, ${generateInfo.basicClassName}DO.class));
        IsrvRspInfoOutput isrvRspInfoOutput = new IsrvRspInfoOutput();
        isrvRspInfoOutput.setRspCnt(num);
        return isrvRspInfoOutput;
    }
}
