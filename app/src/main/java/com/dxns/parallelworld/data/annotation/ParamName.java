package com.dxns.parallelworld.data.annotation;

/**
 * @author kingty
 * @title ParamName
 * @description
 * @modifier
 * @date
 * @since 15/6/23 下午4:31
 */
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于Gson命名策略的注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ParamName {

    String value();
}

