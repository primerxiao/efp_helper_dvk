package com.efp.plugins.frame.dubbo.service;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * dobbo服务调用客户端
 * @author gcb
 */
public class DubboClient {

    public static DubboClient instance = new DubboClient();

    private Map<String, ReferenceConfig<GenericService>> referenceConfigMapping = new ConcurrentHashMap<String, ReferenceConfig<GenericService>>();
    private Object lock = new Object();


    /**
     * 创建dubbo连接
     *
     * @param serviceInterface
     * @param group
     * @return
     */
    public GenericService buildGenericService(String serviceInterface, String group, String version) {
        return this.buildGenericService(serviceInterface, 150000, group, version);
    }

    /**
     * 创建dubbo连接
     *
     * @param serviceInterface
     * @param timeout
     * @param group
     * @return
     */
    public GenericService buildGenericService(String serviceInterface, int timeout, String group, String version) {
        ReferenceConfig<GenericService> reference = referenceConfigMapping.get(serviceInterface);
        if (reference == null || reference.get() == null) {
            synchronized (lock) {
                reference = referenceConfigMapping.get(serviceInterface);
                if (reference == null || reference.get() == null) {
                    reference = new ReferenceConfig<GenericService>();
                    RegistryConfig registry = new RegistryConfig();
                    registry.setProtocol("zookeeper");
                    registry.setAddress("127.0.0.1:2181");
                    //registry.setFile(applicationName + ".properties");
                    //添加配置
                    reference.setApplication(new ApplicationConfig("TestApi"));
                    // 多个注册中心可以用setRegistries()
                    reference.setRegistry(registry);
                    reference.setInterface(serviceInterface);
                    reference.setGeneric(Boolean.TRUE);
                    reference.setSent(Boolean.FALSE);
                    reference.setCluster("failover");
                    if (StringUtils.isNoneEmpty(group)) {
                        reference.setGroup(group);
                    }
                    reference.setRetries(1);
                    reference.setTimeout(timeout);
                    reference.setVersion(StringUtils.isEmpty(version) ? "1.0.0" : version);
                    referenceConfigMapping.put(serviceInterface, reference);
                }
            }
        }
        return reference.get();
    }

}