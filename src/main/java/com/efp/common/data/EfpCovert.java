package com.efp.common.data;

import com.intellij.database.model.DasNamespace;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HIFeng
 */
public class EfpCovert {

    public static List<ModuleCovertBean> list = new ArrayList<>();

    static {
        list.add(new ModuleCovertBean("basic.sequence.impl", "basic_sequence", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("cdp.pboc.common", "cdp_pboc", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("basic.sequence.middle", "basic_sequence", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("cdp.pboc.impl", "cdp_pboc", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("basic.sequence.service", "basic_sequence", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("cdp.pboc.middle", "cdp_pboc", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.batch.common", "efp_batch", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("cdp.pboc.service", "cdp_pboc", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.batch.service", "efp_batch", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.batch.middle", "efp_batch", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.console.common", "efp_console", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.console.front", "efp_console", EfpModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.console.middle", "efp_console", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.console.service", "efp_console", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.console.impl", "efp_console", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.ctr.common", "efp_ctr", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.batch.impl", "efp_batch", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.ctr.middle", "efp_ctr", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.ctr.impl", "efp_ctr", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.ctr.service", "efp_ctr", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.cus.front", "efp_cus", EfpModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.cus.impl", "efp_cus", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.cus.common", "efp_cus", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.cus.service", "efp_cus", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.cus.middle", "efp_cus", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.e4a.common", "efp_e4a", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.e4a.front", "efp_e4a", EfpModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.e4a.impl", "efp_e4a", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.e4a.middle", "efp_e4a", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.e4a.service", "efp_e4a", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.console.api", "efp_console", EfpModuleType.API));
        list.add(new ModuleCovertBean("efp.edoc.api", "efp_edoc", EfpModuleType.API));
        list.add(new ModuleCovertBean("efp.cus.api", "efp_cus", EfpModuleType.API));
        list.add(new ModuleCovertBean("efp.e4a.api", "efp_e4a", EfpModuleType.API));
        list.add(new ModuleCovertBean("efp.edoc.front", "efp_edoc", EfpModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.edoc.middle", "efp_edoc", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.edoc.service", "efp_edoc", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.esb.api", "efp_esb", EfpModuleType.API));
        list.add(new ModuleCovertBean("efp.esb.front", "efp_esb", EfpModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.edoc.common", "efp_edoc", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.edoc.impl", "efp_edoc", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.esb.middle", "efp_esb", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.esb.impl", "efp_esb", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.esb.service", "efp_esb", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.esb.common", "efp_esb", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.flow.front", "efp_flow", EfpModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.flow.middle", "efp_flow", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.flow.common", "efp_flow", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.flow.api", "efp_flow", EfpModuleType.API));
        list.add(new ModuleCovertBean("efp.limit.common", "efp_limit", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.limit.impl", "efp_limit", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.flow.service", "efp_flow", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.flow.impl", "efp_flow", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.limit.middle", "efp_limit", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.loan.impl", "efp_loan", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.limit.service", "efp_limit", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.loan.service", "efp_loan", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.loan.middle", "efp_loan", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.nls.front", "efp_nls", EfpModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.loan.common", "efp_loan", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.nls.middle", "efp_nls", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.nls.impl", "efp_nls", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.nls.api", "efp_nls", EfpModuleType.API));
        list.add(new ModuleCovertBean("efp.report.common", "efp_report", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.nls.common", "efp_nls", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.nls.service", "efp_nls", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.report.impl", "efp_report", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.riskm.common", "efp_riskm", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.riskm.front", "efp_riskm", EfpModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.riskm.impl", "efp_riskm", EfpModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.riskm.middle", "efp_riskm", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.riskm.service", "efp_riskm", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.rule.common", "efp_rule", EfpModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.report.service", "efp_report", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.rule.middle", "efp_rule", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.report.middle", "efp_report", EfpModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.riskm.api", "efp_risk", EfpModuleType.API));
        list.add(new ModuleCovertBean("efp.rule.service", "efp_rule", EfpModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.rule.impl", "efp_rule", EfpModuleType.IMPL));
    }

    /**
     * 根据条件获取项目module
     *
     * @return
     */
    public static Module getModule(Project project, DasNamespace dasNamespace, EfpModuleType moduleType) {
        //根据数据库名获取转换对象
        for (ModuleCovertBean moduleCovertBean : list) {
            if (moduleCovertBean.getNameSpaceName().equalsIgnoreCase(dasNamespace.getName())) {
                if (moduleCovertBean.getEfpModuleType().getModuleTypeValue() == moduleType.getModuleTypeValue()) {
                    return ModuleManager.getInstance(project).findModuleByName(moduleCovertBean.getModuleName());
                }
            }
        }
        return null;
    }

    /**
     * 或者模块名的切割数组
     * @param module
     * @return
     */
    public static String[] getModuleNameArr(Module module) {
        String name = module.getName();
        return name.split("\\.");
    }

}
