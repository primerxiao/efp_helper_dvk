package com.efp.dvk.common.lang.annation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelField {
    /**
     * 表头
     *
     * @return string
     */
    String header();

    int order() default 0;
}
