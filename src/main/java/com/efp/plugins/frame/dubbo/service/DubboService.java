package com.efp.plugins.frame.dubbo.service;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSONObject;
import com.efp.plugins.frame.dubbo.bean.DubboMethodParam;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
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
        ProviderConfig providerConfig = new ProviderConfig();
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress(dubboCallParam.getRegistryAddress());
        registry.setTimeout(10000);
        registry.setWait(10000);
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(application);
        referenceConfig.setRegistry(registry);
        // 弱类型接口名
        referenceConfig.setInterface(dubboCallParam.getReferenceInterface());
        // 声明为泛化接口
        referenceConfig.setVersion(dubboCallParam.getReferenceVersion());
        referenceConfig.setGeneric(dubboCallParam.isReferenceGeneric());
        referenceConfig.setSent(false);
        referenceConfig.setCluster("failover");
        referenceConfig.setGroup(dubboCallParam.getReferenceGroup());
        referenceConfig.setRetries(0);
        if (StringUtils.isNotEmpty(dubboCallParam.getId())) {
            referenceConfig.setId(dubboCallParam.getId());
        }
        // 用com.alibaba.dubbo.rpc.service.GenericService可以替代所有接口引用
        GenericService genericService = referenceConfig.get();
        // 如果返回POJO将自动转成Map
        return genericService.$invoke(dubboCallParam.getInvokeMethod(), dubboCallParam.getInvokeMethodParamType(),
                dubboCallParam.getInvokeMethodParam());
    }

    public Object remoteCall2(DubboCallParam dubboCallParam) {
        GenericService genericService = DubboClient.instance.buildGenericService(dubboCallParam.getReferenceInterface(), 10000, dubboCallParam.getReferenceGroup(), dubboCallParam.getReferenceVersion());
        return genericService.$invoke(dubboCallParam.getInvokeMethod(), dubboCallParam.getInvokeMethodParamType(),
                dubboCallParam.getInvokeMethodParam());
    }


    public void remoteCallWithTelnet(DubboCallParam dubboCallParam) {
        TelnetClient telnetClient = null;
        PrintStream pStream = null;
        InputStream inputStream = null;
        try {
            String[] providerConfigArr = dubboCallParam.getRegistryAddress().split(":");
            telnetClient = new TelnetClient("vt200");
            telnetClient.setDefaultTimeout(5000);
            telnetClient.connect(providerConfigArr[0], Integer.parseInt(providerConfigArr[1]));
            inputStream = telnetClient.getInputStream();
            pStream = new PrintStream(telnetClient.getOutputStream());
            pStream.println("invoke " + dubboCallParam.getReferenceInterface() + "." + dubboCallParam.getInvokeMethod() + JSONObject.toJSONString(dubboCallParam.getInvokeMethodParam()));
            pStream.flush();
            StringBuffer stringBuffer = new StringBuffer();
            char ch;
            int code = -1;
            while ((code = inputStream.read()) != -1) {
                ch = (char) code;
                stringBuffer.append(ch);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pStream != null) {
                    pStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (telnetClient != null) {
                    telnetClient.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
