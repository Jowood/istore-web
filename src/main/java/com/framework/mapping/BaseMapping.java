package com.framework.mapping;

import com.framework.annotation.FieldAnnotation;
import com.framework.annotation.TableAnnotation;
import com.framework.util.BeanUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 定义映射对象的公用方法, 动态参数
 * User: 姜敏
 * Date: 2009-8-16
 * Time: 15:05:41
 * To change this template use File | Settings | File Templates.
 */

public abstract class BaseMapping extends SerializeCloneable {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private List<String> params = new ArrayList<String>();


    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    /**
     * 将当前对象的所有字段(get,set)加入到参数列表
     */
    public void addAllField() {
        if (params == null) {
            params = new ArrayList<String>();
        }

        Class cls = this.getClass();
        while (!cls.equals(BaseMapping.class)) {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                String fieldname = field.getName();
                if (!params.contains(fieldname)) {
                    params.add(fieldname);
                }
            }
            cls = cls.getSuperclass();
        }
    }

    //添加参数到列表
    public void addParam(String param) {
        if (params == null) {
            params = new ArrayList<String>();
        }
        if (!params.contains(param)) {
            params.add(param);
        }
    }

    /**
     * 加入多个参数到列表
     *
     * @param params
     */
    public void addParams(String[] params) {
        for (String param : params) {
            addParam(param.trim());
        }
    }

    public void addParams(String params) {
        String[] arraystr = params.split(",");
        addParams(arraystr);
    }


    // 重置参数列表,并加入一个参数
    public void resetParam(String param) {
        if (params == null) {
            params = new ArrayList<String>();
        }
        params.clear();
        params.add(param);
    }

    // 重置参数列表,并加入一个参数数组
    public void resetParams(String[] params) {
        for (String param : params) {
            resetParam(param.trim());
        }
    }

    // 重置参数列表,并加入一个参数数组
    public void resetParams(String params) {
        String[] arraystr = params.split(",");
        resetParams(arraystr);
    }

    //删除参数列表中的指定参数
    public void removeParam(String param) {
        if (params != null) {
            params.remove(param);
        }
    }

    //删除一个参数数组
    public void removeParams(String[] params) {
        if (this.params != null) {
            for (String param : params) {
                this.params.remove(param.trim());
            }
        }
    }

    //删除多个参数,以逗号分割
    public void removeParams(String params) {

        String[] arraystr = params.split(",");
        removeParams(arraystr);

    }

    //删除列表中所有参数
    public void removeAllParam() {
        if (params != null) {
            params.clear();
        }
    }

    //调整double
    protected Double getAdjust(Double value) {
        return (value == null || value.isNaN()) ? null : value;
    }

    //调整float
    protected Float getAdjust(Float value) {
        return (value == null || value.isNaN()) ? null : value;
    }


    public String toString() {

        Class cls = this.getClass();
        String tableName = "";

        if (cls.isAnnotationPresent(TableAnnotation.class)) { //定义表注解
            TableAnnotation tableAnnotation = (TableAnnotation) cls.getAnnotation(TableAnnotation.class);
            if (!tableAnnotation.log()) {
                return null;
            }
            tableName = tableAnnotation.name() + "(" + cls.getSimpleName() + ")";
        } else {
            return null;
        }
        while (!cls.equals(BaseMapping.class)) {
            Field[] fields = cls.getDeclaredFields();
            String fieldname;
            for (Field field : fields) {
                fieldname = field.getName();
                if (params != null && params.size() > 0 && !params.contains(fieldname)) { //更新列表中没有该字段,就跳过
                    continue;
                }
                FieldAnnotation fieldAnnotation;
                if (field.isAnnotationPresent(FieldAnnotation.class)) {   //字段定义了注解
                    fieldAnnotation = field.getAnnotation(FieldAnnotation.class);
                    if (!fieldAnnotation.log()) { //不需要记录日志
                        continue;
                    }
                    fieldname = fieldAnnotation.name();  //得到注解的中文字段名
                    try {
                        field.setAccessible(true);
                        tableName += "\n" + fieldname + ":";
                        if (field.get(this) != null) {
                            tableName += toString(field.get(this));
                        } else {
                            tableName += "null";
                        }
                    } catch (IllegalAccessException e) {
                        logger.error(e.getMessage());
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return tableName;
    }


    private String toString(Object value) {
        if (value instanceof String) return (String) value;
        else if (value instanceof Boolean) return value.toString();
        else if (value instanceof Date) return formatter.format(value);
        else if (value instanceof BigDecimal) return ((BigDecimal) value).toPlainString();
        else if (value instanceof Double) return (new BigDecimal((Double) value)).toPlainString();
        else if (value instanceof Float) return (new BigDecimal((Float) value)).toPlainString();
        else if (value instanceof Long) return value.toString();
        else if (value instanceof Integer) return value.toString();
        else if (value instanceof Short) return value.toString();
        return null;
    }

    //得到对象的json字符串
    public String toJsonString() {
        return BeanUtil.toJsonString(this);
    }

}
