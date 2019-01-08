package com.ziyun.dormitory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 目标数据源注解，注解在方法上指定数据源的名称
 * @author: yk.tan
 * @date: 2017/12/5
 * @history:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TargetDataSource {
    /**
     * 此处接收的是数据源的名称
     *
     * @return
     */
    String value();
}
