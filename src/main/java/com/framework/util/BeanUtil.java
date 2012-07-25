package com.framework.util;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 表单封装的工具
 * User: Simple
 * Date: 2009-8-14
 * Time: 23:32:28
 * To change this template use File | Settings | File Templates.
 */
public class BeanUtil {

    private static final Log logger = LogFactory.getLog(BeanUtil.class);


    static {
        DateConverter dc = new DateConverter();
        String[] datePattern = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss"};
        dc.setPatterns(datePattern);
        ConvertUtils.register(dc, java.util.Date.class);
    }


    public static Map toMap(Object bean) {
        return bean != null ? new BeanMap(bean) : null;
    }



    /**
     * 属性值去空处理
     *
     * @param map
     * @return
     */
    public static Map trimMap(Map map) {
        for (Object o : map.entrySet()) {
            Map.Entry me = (Map.Entry) o;
            map.put(me.getKey(), me.getValue().toString().trim());
        }
        return map;
    }


    /**
     * 将请求参数封装成Bean对象
     *
     * @param request 请求
     * @param cls     类class
     * @return Object
     */
    public static <T> T createBean(HttpServletRequest request, Class<T> cls) {
        try {
            T obj = cls.newInstance();
            Map map = request.getParameterMap();
            BeanUtils.populate(obj, map);
            return obj;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 将以json方式提交的字符串封装成Bean对象
     *
     * @param json json对象
     * @param cls  bean的class
     * @return Object
     */
    public static <T> T createBean(String json, Class<T> cls) {
        if (json == null) return null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.getDeserializationConfig().disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.getDeserializationConfig().setDateFormat(new DateFormat());
        try {
            return mapper.readValue(json, cls);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 将以json方式提交的字符串封装成Bean对象
     *
     * @param json     json对象
     * @param javaType bean的class
     * @return Object
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T createBean(String json, JavaType javaType) {
        if (json == null) return null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.getDeserializationConfig().disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.getDeserializationConfig().setDateFormat(new DateFormat());
        try {
            return (T) mapper.readValue(json, javaType);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 将json 数组封装成 arrays
     *
     * @param json json数组
     * @param cls  类名
     * @return T[]
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T createArray(String json, Class cls) {
        if (json == null) return null;
        return (T) createBean(json, TypeFactory.arrayType(cls));
    }


    /**
     * 将以json方式提交的字符串封装成Bean集合
     *
     * @param jsonArray json数组
     * @param cls       bean的class
     * @return List
     */
    public static <T> List<T> createBeanList(String jsonArray, Class<T> cls) {
        if (jsonArray == null) return null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.getDeserializationConfig().disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.getDeserializationConfig().setDateFormat(new DateFormat());
        try {
            return mapper.readValue(jsonArray, TypeFactory.collectionType(List.class, cls));
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }


    /**
     * 对象转换json字符串
     *
     * @param obj 对象
     * @return String
     */
    public static String toJsonString(Object obj) {
        if (obj == null) return null;
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

        mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.getSerializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        try {
            StringWriter sb = new StringWriter();
            mapper.writeValue(sb, obj);
            return sb.toString();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static String listToJsonString(List list) {
        StringBuffer buf = new StringBuffer("[");
        if (list != null) {
            String flag = "";
            for (Object obj : list) {
                if (!(obj instanceof Map)) break;
                Map map = (Map) obj;
                Iterator it = map.entrySet().iterator();
                String mFlag = "";
                buf.append(flag).append("{");
                while (it.hasNext()) {
                    Map.Entry m = (Map.Entry) it.next();
                    buf.append(mFlag).append(m.getKey().toString()).append(":");
                    if (m.getValue() == null) {
                        buf.append("''");
                    } else if (m.getValue() instanceof Date) {
/*                    SimpleDateFormat simple = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    buf.append("new Date('").append(simple.format((Date)m.getValue())).append("')");*/
                        buf.append("'").append(DateUtil.timeToStr((Date) m.getValue())).append("'");
                    } else {
                        buf.append("'").append(m.getValue().toString()).append("'");
                    }
                    mFlag = ",";
                }
                buf.append("}");
                flag = ",";
            }
        }
        buf.append("]");
        return buf.toString();
    }

    /**
     * 将Map封装成json序列对象
     *
     * @param mapping 映射对象
     * @return JSONObject
     */
    public static JSONObject createJSONObject(Map mapping) {
        if (mapping == null) return null;
        JSONObject jsonObject = new JSONObject();
        for (Object key : mapping.keySet()) {
            Object value = mapping.get(key);
            if (value == null) {
                jsonObject.put((String) key, "");
            }
            if (value instanceof Object[]) {
                Object[] obj = (Object[]) mapping.get((String) key);
                String[] array_value = (String[]) convertArray(obj, String.class);
                jsonObject.put((String) key, array_value == null ? "" : array_value);
            } else {
                String string_value = mapping.get((String) key).toString();
                jsonObject.put((String) key, string_value == null ? "" : string_value);
            }
        }
        return jsonObject;
    }

    /**
     * 将list<BasicMapping>集合对象封装成json序列对象数组
     *
     * @param list 映射对象集合
     * @return JSONArray
     */
    public static JSONArray createJSONArray(List<Map> list) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        if (list != null) {
            jsonArray = new JSONArray();
            for (Map mapping : list) {
                jsonObject = createJSONObject(mapping);
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }


    public static Object[] convertArray(Object[] objects, Class<?> returnClass) {
        if (objects == null) return null;
        Object[] returnArray = null;
        if (returnClass.isAssignableFrom(String.class)) returnArray = new String[objects.length];
        if (returnClass.isAssignableFrom(Boolean.class)) returnArray = new Boolean[objects.length];
        if (returnClass.isAssignableFrom(Date.class)) returnArray = new Date[objects.length];
        if (returnClass.isAssignableFrom(BigDecimal.class)) returnArray = new BigDecimal[objects.length];
        if (returnClass.isAssignableFrom(Double.class)) returnArray = new Double[objects.length];
        if (returnClass.isAssignableFrom(Float.class)) returnArray = new Float[objects.length];
        if (returnClass.isAssignableFrom(Long.class)) returnArray = new Long[objects.length];
        if (returnClass.isAssignableFrom(Integer.class)) returnArray = new Integer[objects.length];
        if (returnClass.isAssignableFrom(Short.class)) returnArray = new Short[objects.length];
        if (returnArray != null) {
            for (int i = 0; i < objects.length; i++) {
                returnArray[i] = convertObject(objects[i], returnClass);
            }
            return returnArray;
        } else {
            return null;
        }
    }

    /**
     * 将值按照指定的类型转型
     *
     * @param value       值
     * @param returnClass 返回的class类型
     * @return 返回实例
     */

    protected static Object convertObject(Object value, Class returnClass) {
        if (value == null) return null;
        try {
            if (returnClass.isAssignableFrom(String.class)) {
                if (value instanceof String) return value;
                if (value instanceof Boolean) return value.toString();
                if (value instanceof Date) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setLenient(false);
                    return sdf.format(value);
                }
                if (value instanceof BigDecimal) return ((BigDecimal) value).toPlainString();
                if (value instanceof Double) return (new BigDecimal((Double) value)).toPlainString();
                if (value instanceof Float) return (new BigDecimal((Float) value)).toPlainString();
                if (value instanceof Long) return value.toString();
                if (value instanceof Integer) return value.toString();
                if (value instanceof Short) return value.toString();
            } else if (returnClass.isAssignableFrom(Boolean.class)) {
                if (value instanceof Boolean) return value;
                if (value instanceof String) {
                    return value.equals("true");
                }
            } else if (returnClass.isAssignableFrom(Date.class)) {
                if (value instanceof Date) return value;
                if (value instanceof String) {
                    SimpleDateFormat formatter;
                    String stringValue = (String) value;
                    if (stringValue.length() == 10) formatter = new SimpleDateFormat("yyyy-MM-dd");
                    else if (stringValue.length() == 16) formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    else if (stringValue.length() == 19) formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    else return null;
                    formatter.setLenient(false);
                    return formatter.parse(stringValue);
                }
            } else if (returnClass.isAssignableFrom(BigDecimal.class)) {
                if (value instanceof BigDecimal) return value;
                if (value instanceof Double) return new BigDecimal((Double) value);
                if (value instanceof Float) return new BigDecimal((Float) value);
                if (value instanceof Long) return new BigDecimal((Long) value);
                if (value instanceof Integer) return new BigDecimal((Integer) value);
                if (value instanceof Short) return new BigDecimal((Short) value);
                if (value instanceof String) return new BigDecimal((String) value);
            } else if (returnClass.isAssignableFrom(Double.class)) {
                if (value instanceof Number) return ((Number) value).doubleValue();
                if (value instanceof String) return (new BigDecimal((String) value)).doubleValue();
            } else if (returnClass.isAssignableFrom(Float.class)) {
                if (value instanceof Number) return ((Number) value).floatValue();
                if (value instanceof String) return (new BigDecimal((String) value)).floatValue();

            } else if (returnClass.isAssignableFrom(Long.class)) {
                if (value instanceof Number) return ((Number) value).longValue();
                if (value instanceof String) return (new BigDecimal((String) value)).longValue();
            } else if (returnClass.isAssignableFrom(Integer.class)) {
                if (value instanceof Number) return ((Number) value).intValue();
                if (value instanceof String) return (new BigDecimal((String) value)).intValue();
            } else if (returnClass.isAssignableFrom(Short.class)) {
                if (value instanceof Number) return ((Number) value).shortValue();
                if (value instanceof String) return (new BigDecimal((String) value)).shortValue();
            } else {
                return null;
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
//      return null;
        }
        return null;
    }

    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, IOException {

        //Class cls = LinkedHashMap.class;
        Map map = new HashMap();
        map.put("a", " 1 ");
        map.put("b", " 2 ");
        map.put("c", " 3 ");

//        JSONObject jsonObject = BeanUtil.createJSONObject(map);
//        System.out.println(jsonObject.toString());
//
//        List list = new ArrayList();
//        list.add(map);
//        JSONArray jsonArray = BeanUtil.createJSONArray(list);
//        System.out.println(jsonArray.toString());
        //BeanUtil.trimMap(map);
    }

}


class DateConverter extends DateTimeConverter {

    DateConverter() {
    }

    DateConverter(Object o) {
        super(o);
    }

    @Override
    protected Object convertToType(Class aClass, Object o) {
        try {
            return super.convertToType(aClass, o);
        } catch (Exception e) {
            return null;
        }
    }

    protected Class getDefaultType() {
        return Date.class;
    }
}

