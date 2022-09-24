package com.efp.dvk.common.lang.annation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * @return Name of the database column. Leaving this empty will convert the java field name to snake_case
     */
    String name() default "";

}
