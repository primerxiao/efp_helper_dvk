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
        list.add(new ModuleCovertBean("basic.sequence.impl", "basic_sequence", ModuleType.IMPL));
        list.add(new ModuleCovertBean("cdp.pboc.common", "cdp_pboc", ModuleType.COMMON));
        list.add(new ModuleCovertBean("basic.sequence.middle", "basic_sequence", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("cdp.pboc.impl", "cdp_pboc", ModuleType.IMPL));
        list.add(new ModuleCovertBean("basic.sequence.service", "basic_sequence", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("cdp.pboc.middle", "cdp_pboc", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.batch.common", "efp_batch", ModuleType.COMMON));
        list.add(new ModuleCovertBean("cdp.pboc.service", "cdp_pboc", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.batch.service", "efp_batch", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.batch.middle", "efp_batch", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.console.common", "efp_console", ModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.console.front", "efp_console", ModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.console.middle", "efp_console", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.console.service", "efp_console", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.console.impl", "efp_console", ModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.ctr.common", "efp_ctr", ModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.batch.impl", "efp_batch", ModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.ctr.middle", "efp_ctr", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.ctr.impl", "efp_ctr", ModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.ctr.service", "efp_ctr", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.cus.front", "efp_cus", ModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.cus.impl", "efp_cus", ModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.cus.common", "efp_cus", ModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.cus.service", "efp_cus", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.cus.middle", "efp_cus", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.e4a.common", "efp_e4a", ModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.e4a.front", "efp_e4a", ModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.e4a.impl", "efp_e4a", ModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.e4a.middle", "efp_e4a", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.e4a.service", "efp_e4a", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.console.api", "efp_console", ModuleType.API));
        list.add(new ModuleCovertBean("efp.edoc.api", "efp_edoc", ModuleType.API));
        list.add(new ModuleCovertBean("efp.cus.api", "efp_cus", ModuleType.API));
        list.add(new ModuleCovertBean("efp.e4a.api", "efp_e4a", ModuleType.API));
        list.add(new ModuleCovertBean("efp.edoc.front", "efp_edoc", ModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.edoc.middle", "efp_edoc", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.edoc.service", "efp_edoc", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.esb.api", "efp_esb", ModuleType.API));
        list.add(new ModuleCovertBean("efp.esb.front", "efp_esb", ModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.edoc.common", "efp_edoc", ModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.edoc.impl", "efp_edoc", ModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.esb.middle", "efp_esb", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.esb.impl", "efp_esb", ModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.esb.service", "efp_esb", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.esb.common", "efp_esb", ModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.flow.front", "efp_flow", ModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.flow.middle", "efp_flow", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.flow.common", "efp_flow", ModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.flow.api", "efp_flow", ModuleType.API));
        list.add(new ModuleCovertBean("efp.limit.common", "efp_limit", ModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.limit.impl", "efp_limit", ModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.flow.service", "efp_flow", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.flow.impl", "efp_flow", ModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.limit.middle", "efp_limit", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.loan.impl", "efp_loan", ModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.limit.service", "efp_limit", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.loan.service", "efp_loan", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.loan.middle", "efp_loan", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.nls.front", "efp_nls", ModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.loan.common", "efp_loan", ModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.nls.middle", "efp_nls", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.nls.impl", "efp_nls", ModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.nls.api", "efp_nls", ModuleType.API));
        list.add(new ModuleCovertBean("efp.report.common", "efp_report", ModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.nls.common", "efp_nls", ModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.nls.service", "efp_nls", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.report.impl", "efp_report", ModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.riskm.common", "efp_riskm", ModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.riskm.front", "efp_riskm", ModuleType.FRONT));
        list.add(new ModuleCovertBean("efp.riskm.impl", "efp_riskm", ModuleType.IMPL));
        list.add(new ModuleCovertBean("efp.riskm.middle", "efp_riskm", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.riskm.service", "efp_riskm", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.rule.common", "efp_rule", ModuleType.COMMON));
        list.add(new ModuleCovertBean("efp.report.service", "efp_report", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.rule.middle", "efp_rule", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.report.middle", "efp_report", ModuleType.MIDDLE));
        list.add(new ModuleCovertBean("efp.riskm.api", "efp_risk", ModuleType.API));
        list.add(new ModuleCovertBean("efp.rule.service", "efp_rule", ModuleType.SERVICE));
        list.add(new ModuleCovertBean("efp.rule.impl", "efp_rule", ModuleType.IMPL));
    }

    /**
     * 根据条件获取项目module
     *
     * @return
     */
    public static Module getModule(Project project, DasNamespace dasNamespace, ModuleType moduleType) {
        //根据数据库名获取转换对象
        for (ModuleCovertBean moduleCovertBean : list) {
            if (moduleCovertBean.getNameSpaceName().equalsIgnoreCase(dasNamespace.getName())) {
                if (moduleCovertBean.moduleType.moduleTypeValue == moduleType.moduleTypeValue) {
                    return ModuleManager.getInstance(project).findModuleByName(moduleCovertBean.getModuleName());
                }
            }
        }
        return null;
    }

    static enum ModuleType {
        FRONT(1),
        API(2),
        MIDDLE(3),
        SERVICE(4),
        IMPL(5),
        COMMON(6);
        private int moduleTypeValue;

        ModuleType(int moduleTypeValue) {
            this.moduleTypeValue = moduleTypeValue;
        }

        public int getModuleTypeValue() {
            return moduleTypeValue;
        }
    }

    static class ModuleCovertBean {
        private String moduleName;
        private String nameSpaceName;
        private ModuleType moduleType;

        public ModuleCovertBean(String moduleName, String nameSpaceName, ModuleType moduleType) {
            this.moduleName = moduleName;
            this.nameSpaceName = nameSpaceName;
            this.moduleType = moduleType;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public String getNameSpaceName() {
            return nameSpaceName;
        }

        public void setNameSpaceName(String nameSpaceName) {
            this.nameSpaceName = nameSpaceName;
        }

        public ModuleType getModuleType() {
            return moduleType;
        }

        public void setModuleType(ModuleType moduleType) {
            this.moduleType = moduleType;
        }
    }
}
