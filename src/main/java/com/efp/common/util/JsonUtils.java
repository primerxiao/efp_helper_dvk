package com.efp.common.util;

import com.google.gson.Gson;
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

    /**
     * 将对象转成json串
     * @param src
     * @return
     */
    public static String toJsonString(Object src){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(src);
    }
}
