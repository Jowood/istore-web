package com.framework.util;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

/**
 * web工具,提供request,response,session,servletcontext的获得
 * User: Administrator
 * Date: 2009-8-18
 * Time: 17:55:53
 * To change this template use File | Settings | File Templates.
 */
public class WebUtil {

    
    private static ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> responses = new ThreadLocal<HttpServletResponse>();
    private static ServletContext servletContext = null;


    /**
     * 将请求对象存储到本地线程中
     *
     * @param request 请求对象
     */
    public static void setRequest(HttpServletRequest request) {
        requests.remove();
        requests.set(request);
    }

    /**
     * 从本地线程中得到请求对象
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        return requests.get();
    }

    /**
     * 移除请求对象
     */
    public static void removeRequest(){
        requests.remove();
    }

    /**
     * 将响应对象存储到本地线程
     *
     * @param response 响应对象
     */
    public static void setResponse(HttpServletResponse response) {
        responses.remove();
        responses.set(response);
    }

    /**
     * 从本地线程中得到响应对象
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        return responses.get();
    }

    /**
     * 移除响应对象
     */
    public static void removeResponse(){
        responses.remove();
    }

    /**
     * 从本地线程中得到会话对象
     *
     * @return HttpServletResponse
     */
    public static HttpSession getSession() {
        return getRequest().getSession(true);
    }


    /**
     * 将全局对象存储到本地线程
     *
     * @param context 全局对象
     */
    public static void setServletContext(ServletContext context) {
        servletContext = context;
    }

    /**
     * 从本地线程中得到全局对象
     *
     * @return ServletContext
     */
    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static WebApplicationContext getWebApplicationContext(){
        return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    }

    /**
     * 移除全局对象
     */
    public static void removeServletContext(){
        servletContext=null;
    }
}
