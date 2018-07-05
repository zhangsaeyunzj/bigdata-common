package com.nuonuo.bigdata.annotation;

import com.nuonuo.bigdata.common.BaseRowKeyGenerator;
import com.nuonuo.bigdata.common.RowKeyGenerator;
import com.nuonuo.bigdata.common.RowKeyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RowKey {
    String field() default "";

    long length() default 0;

    RowKeyType method() default RowKeyType.UUID;

    Class<? extends RowKeyGenerator> clazzName() default BaseRowKeyGenerator.class;
}
