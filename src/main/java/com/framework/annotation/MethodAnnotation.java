package com.framework.annotation;

import java.lang.annotation.*;

/**
 * 逻辑方法注解,实现权限控制和写日志功能, LogicController控制器的自动装配和运行参数
 * User: 姜敏
 * Date: 2009-8-13
 * Time: 11:42:57
 * To change this template use File | Settings | File Templates.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MethodAnnotation {
    public enum CheckType {NO_CHECK,CHECK_LOGIN,CHECK_LOGIN_GROUP}
    String name() default "";           //逻辑方法描述
    boolean log() default false;        //是否需要写日志 true:写 false:不写
    CheckType check() default CheckType.CHECK_LOGIN_GROUP;       //验证权限的类型 NO_CHECK: 不需要  CHECK_LOGIN: 验证登录 CHECK_LOGIN_GROUP: 验证登录和权限组     
    String group() default "";          //功能组,定义逻辑方法属于哪个功能组
    String notFilterSql() default "";   //无需数据过滤的sql名称,以逗号分隔
    String notFilterModName() default "";     //无需数据过滤的模块名称,以逗号分隔
    String url() default "";            //请求动作映射方法
    boolean ajax() default false;       //是否是ajax请求

}
