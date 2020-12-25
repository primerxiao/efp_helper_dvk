package com.efp.plugins.frame.dubbo.service;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class DubboServiceImpl {

    private final Supplier<ApplicationConfig> supplier=ApplicationConfig::new;

    private final String applicationName = "TestApi";

    /**
     * dubbo泛化调用
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


}
