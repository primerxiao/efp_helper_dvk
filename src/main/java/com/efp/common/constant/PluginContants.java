package com.efp.common.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 插件常量类
 *
 * @author primerxiao
 */
public class PluginContants {

    public static final String GENERATOR_UI_TITLE = "FDB开发助手";

    public static class DubboConfig {
        public static final String DEFAULT_DUBBO_REGISTER = "zookeeper://10.139.4.171:2181";
        public static final String DEFAULT_DUBBO_GROUP = "dubbo";
        public static final String DEFAULT_DUBBO_VERSION = "1.0.0";
    }
    public static List<String> chooseModuleNames = Arrays.asList("a-smcbi", "a-smcbs", "a-smcfc", "a-smcia", "a-smcii", "a-smcpi", "a-smcqc", "a-smcrc", "a-smcrp", "a-smcti");


}