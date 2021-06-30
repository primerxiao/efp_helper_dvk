package ${generateInfo.packageName};

import com.fdb.smcts.core.vo.IsrvRspInfoOutput;
import ${generateInfo.packageName}.dto.${generateInfo.basicClassName}Input;
import ${generateInfo.packageName}.dto.${generateInfo.basicClassName}Output;

/**
* ${generateInfo.dasTable.comment!''}接口服务
* @date ${.now?string['yyyy-MM-dd']}
*/
public interface ${generateInfo.basicClassName}Service {

    /**
    * 新增${generateInfo.dasTable.comment!''}
    *
    * @param input 需要新增的信息
    * @return IsrvRspInfoOutput
    */
    IsrvRspInfoOutput insertSingle(${generateInfo.basicClassName}Input input);

    /**
    * 根据主键删除${generateInfo.dasTable.comment!''}
    *
    * @param input 主键参数
    * @return
    */
    IsrvRspInfoOutput deleteByPk(${generateInfo.basicClassName}Input input);

    /**
    * 根据主键查询信息
    *
    * @param input 主键参数
    * @return ${generateInfo.basicClassName}Output
    */
    ${generateInfo.basicClassName}Output queryByPk(${generateInfo.basicClassName}Input input);


    /**
    * 根据主键更新${generateInfo.dasTable.comment!''}
    *
    * @param input 主键参数
    * @return
    */
    IsrvRspInfoOutput updateByPk(${generateInfo.basicClassName}Input input);
}