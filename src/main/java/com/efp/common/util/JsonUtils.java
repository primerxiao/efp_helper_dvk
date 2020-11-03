package com.efp.common.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * json相关的操作
 */
public class JsonUtils {
    /**
     * 格式化json字符串
     * @param jsonStr 未格式化的json字符串
     * @return 格式化后的json字符串
     */
    public static String prettyformat(String jsonStr) {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(new JsonParser().parse(jsonStr).getAsJsonObject());
    }

}
