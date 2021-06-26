package com.efp.plugins.project.coder.template.method;

import com.efp.plugins.project.coder.util.GenUtils;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

import java.util.List;

public class SimpleBaseModuleNameMethod implements TemplateMethodModel {
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        return GenUtils.getNameByBaseMoudleName((String) arguments.get(0));
    }
}
