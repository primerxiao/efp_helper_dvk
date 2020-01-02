package ${generateJava.controllerPathName};

import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.irdstudio.basic.framework.core.constant.DataRuleType;
import com.irdstudio.basic.framework.core.constant.ResponseData;
import com.irdstudio.basic.framework.core.util.StringUtil;
import com.irdstudio.basic.framework.core.util.TimeUtil;
import com.irdstudio.basic.framework.core.util.TraceUtil;
import com.irdstudio.basic.framework.web.controller.AbstractController;
import com.irdstudio.basic.sequence.service.facade.PatternedLimitableSeqService;
import com.irdstudio.efp.e4a.service.facade.SRoleDataRuleService;

import ${generateJava.servicePackageName}.${generateJava.serviceClassName}
import ${generateJava.voPackageName}.${generateJava.voClassName};


/**
* Controller：${dasTable.comment}
*
* @author
* @date 2019-12-19
*/
@RestController
@RequestMapping("/api")
public class ${generateJava.baseClassName}Controller extends AbstractController {

    @Autowired
    @Qualifier("${generateJava.baseClassName?uncap_first}Service")
    private ${generateJava.baseClassName}Service ${generateJava.baseClassName?uncap_first}Service;

    @Autowired
    @Qualifier("sRoleDataRuleService")
    private SRoleDataRuleService sRoleDataRuleService;

    @Autowired
    private PatternedLimitableSeqService patternedLimitableSeqService;

    /**
    * 新增数据
    *
    * @param in${generateJava.baseClassName}Vo
    * @return
    */
    @PostMapping(value = "/signature/stam/upload/record")
    public @ResponseBody ResponseData<Integer> insert${generateJava.baseClassName}(@RequestBody ${generateJava.baseClassName}VO in${generateJava.baseClassName}Vo) {
        String traceId = patternedLimitableSeqService.getSequence();
        ThreadContext.put(TraceUtil.TRACEID, traceId);
        TraceUtil.setTraceId(traceId);

        int outputVo = ${generateJava.baseClassName?uncap_first}Service.insert(in${generateJava.baseClassName}Vo);
        return getResponseData(outputVo);
    }

    /**
    * 根据主键删除信息
    *
    * @param in${generateJava.baseClassName}Vo
    * @return
    */
    @DeleteMapping(value = "/signature/stam/upload/record")
    public @ResponseBody ResponseData<Integer> deleteByPk(@RequestBody ${generateJava.baseClassName}VO in${generateJava.baseClassName}Vo) {
        String traceId = patternedLimitableSeqService.getSequence();
        ThreadContext.put(TraceUtil.TRACEID, traceId);
        TraceUtil.setTraceId(traceId);
        int outputVo = ${generateJava.baseClassName?uncap_first}Service.deleteByPk(in${generateJava.baseClassName}Vo);
        return getResponseData(outputVo);
    }

    /**
    * 根据主键更新信息
    *
    * @param in${generateJava.baseClassName}Vo
    * @return
    */
    @PutMapping(value = "/signature/stam/upload/record")
    public @ResponseBody ResponseData<Integer> updateByPk(@RequestBody ${generateJava.baseClassName}VO in${generateJava.baseClassName}Vo) {
        String traceId = patternedLimitableSeqService.getSequence();
        ThreadContext.put(TraceUtil.TRACEID, traceId);
        TraceUtil.setTraceId(traceId);
        int outputVo = ${generateJava.baseClassName?uncap_first}Service.updateByPk(in${generateJava.baseClassName}Vo);
        return getResponseData(outputVo);
    }

    /**
    * 根据主键查询详情
    *
    */
    @GetMapping(value = "/signature/stam/upload/record")
    public @ResponseBody ResponseData<${generateJava.baseClassName}VO> queryByPk() {
        String traceId = patternedLimitableSeqService.getSequence();
        ThreadContext.put(TraceUtil.TRACEID, traceId);
        TraceUtil.setTraceId(traceId);
        ${generateJava.baseClassName}VO inVo = new ${generateJava.baseClassName}VO();
        ${generateJava.baseClassName}VO outputVo = ${generateJava.baseClassName?uncap_first}Service.queryByPk(inVo);
        return getResponseData(outputVo);
    }
}