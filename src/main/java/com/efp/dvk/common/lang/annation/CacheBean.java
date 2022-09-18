package com.efp.dvk.common.lang.annation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Function;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheBean {

    /**
     * 缓存标识
     * @return String
     */
    String CacheName();

    Function<T,R> FUNCTION();


}
