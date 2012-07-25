package com.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-1-23
 * Time: 22:18:36
 * To change this template use File | Settings | File Templates.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PageAnnotation {
    int pageSize() default 50;
    String recordSql() default "";
    String countSql() default "";
    String fixedOrder() default "";
    String fieldOrder() default "";
}
