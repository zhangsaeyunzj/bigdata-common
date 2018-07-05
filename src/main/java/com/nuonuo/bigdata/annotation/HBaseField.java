package com.nuonuo.bigdata.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HBaseField {
    String name() default "";

    String cf() default "";

    String format() default "yyyy-MM-dd";

    boolean ignore() default false;
}
