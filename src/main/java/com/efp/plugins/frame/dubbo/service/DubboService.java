package com.efp.plugins.frame.dubbo.service;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.efp.common.util.JsonUtils;
import com.efp.plugins.frame.dubbo.bean.DubboMethodParam;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiType;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author 肖均辉
 */
public class DubboService {

    public static DubboService instance = new DubboService();

    private final Supplier<ApplicationConfig> supplier = ApplicationConfig::new;

    private final String applicationName = "TestApi";

    /**
     * dubbo泛化调用
     *
     * @param dubboCallParam 参数
     * @return 调用响应结果
     */
    public Object remoteCall(DubboCallParam dubboCallParam) {
        ApplicationConfig application = supplier.get();
        application.setName(StringUtils.isEmpty(dubboCallParam.getApplicationName()) ? applicationName : dubboCallParam.getApplicationName());
        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress(dubboCallParam.getRegistryAddress());
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setApplication(application);
        reference.setRegistry(registry);
        // 弱类型接口名
        reference.setInterface(dubboCallParam.getReferenceInterface());
        // 声明为泛化接口
        reference.setVersion(dubboCallParam.getReferenceVersion());
        reference.setGeneric(dubboCallParam.isReferenceGeneric());
        reference.setSent(false);
        reference.setCluster("failover");
        reference.setGroup(dubboCallParam.getReferenceGroup());
        reference.setRetries(0);
        // 用com.alibaba.dubbo.rpc.service.GenericService可以替代所有接口引用
        GenericService genericService = reference.get();
        // 如果返回POJO将自动转成Map
        return genericService.$invoke(dubboCallParam.getInvokeMethod(), dubboCallParam.getInvokeMethodParamType(),
                dubboCallParam.getInvokeMethodParam());
    }

    private String[] getParamName(PsiMethod psiMethod) {
        ArrayList<String> types = new ArrayList<>();
        PsiParameterList parameterList = psiMethod.getParameterList();
        if (parameterList.isEmpty()) {
            return types.toArray(new String[0]);
        }
        PsiParameter[] parameters = parameterList.getParameters();
        if (parameters.length <= 0) {
            return types.toArray(new String[0]);
        }
        Arrays.stream(parameters).forEach(p -> types.add(p.getName()));
        return types.toArray(new String[types.size()]);
    }

    public List<DubboMethodParam> getDubboMethodParams(PsiMethod psiMethod) {
        ArrayList<DubboMethodParam> result = new ArrayList<>();
        PsiParameterList parameterList = psiMethod.getParameterList();
        if (!parameterList.isEmpty() && parameterList.getParameters().length > 0) {
            PsiParameter[] parameters = parameterList.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                result.add(DubboMethodParam
                        .DubboMethodParamBuilder
                        .aDubboMethodParam()
                        .withIndex(i + "")
                        .withType(parameters[i].getType().getCanonicalText())
                        .withValue("{}")
                        .build());
            }
        }
        return result;
    }

    public static DubboService getInstance() {
        return instance;
    }
}
