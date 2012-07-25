package com.framework.annotation;

import java.lang.annotation.*;

/**
 * 数据库表描述
 * User: 姜敏
 * Date: 2009-8-13
 * Time: 12:28:45
 * To change this template use File | Settings | File Templates.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TableAnnotation {
    String name() default "";  //数据库表名描述
    String sql() default "";   //查询得到bean的sqlID
    String key() default "";   //主键
    boolean log() default true;
}
