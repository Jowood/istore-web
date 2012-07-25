package com.framework.system;

import java.util.HashMap;
import java.util.Map;

import com.snda.storage.service.CSService;

/**
 * 系统参数类，
 * 保存系统初始化参数（system.xml），和静态常量
 *
 * @author 姜敏
 * @version 4.0
 */
public class SystemParameter {
    /**
     * 用户所处模块名称
     */
    public static final String MODEL_NAME = "mod_name";
    /**
     * 用户所处模块url
     */
    public static final String MODEL_URL = "mod_url";
    /**
     * 用户在会话中放置的key
     */
    public static final String LOGIN_USER_KEY="loginUser";

    /**
     * 登录用户的权限功能组在会话中放置的key
     */
    public static final String RIGHT_GROUP_KEY="rightGroup";

    /**
     * 消息key值
     */
    public static final String MESSAGE_KEY = "message";

    /**
     * 消息页
     */
    public static final String MESSAGE_PAGE = "message.jsp";

    /**
     * 错误页
     */
    public static final String ERROR_PAGE = "error.jsp";
    /**
     * 分页参数
     */
	public static final String PAGE_NUM = "page";


    protected static Map<String, String> parameter = new HashMap<String, String>();
    protected static CSService csService = null;
    public static void set(String key, String value) {
        parameter.put(key, value);
    }

    public static String get(String key) {
        return parameter.get(key);
    }
    
    public static void setGrandCloud(CSService service) {
    	csService = service;
    }
    
    public static CSService getGrandCloud() {
    	return csService;
    }
    
}
