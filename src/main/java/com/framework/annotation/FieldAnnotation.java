package com.framework.annotation;

import java.lang.annotation.*;

/**
 * 数据库字段描述 (用于定义日志内容)
 * User: 姜敏
 * Date: 2009-8-13
 * Time: 12:31:18
 * To change this template use File | Settings | File Templates.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FieldAnnotation {
    //public enum Tyle {CHAR,VARCHAR,DATE,DATETIME}
    String name();      //字段名
    boolean log() default true;
    //Tyle type() default Tyle.VARCHAR;      //数据库字段类型
}
