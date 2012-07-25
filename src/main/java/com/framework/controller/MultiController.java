package com.framework.controller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.PropertiesMethodNameResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.TargetClassAware;

import com.framework.system.SystemParameter;
import com.framework.util.WebUtil;
import com.istore.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier; 
import java.util.*;
 

/**
 * 多功能控制器
 * User: Administrator
 * Date: 2010-1-11
 * Time: 23:13:09
 * To change this template use File | Settings | File Templates.
 */
public class MultiController extends MultiActionController implements InitializingBean {

    private String viewPath = "";                       //页面所在目录
    private String urlSuffix = "";                      //请求后缀
    private String[] forwardMappings;                   //请求到forward方法的映射,直接跳转
    private Properties methodMappings = null;           //请求到控制器方法的映射
//    private String multiHandler = "multiHandler";
    
    public boolean isLogin() { 
    	HttpSession session = WebUtil.getSession();
		User user = (User)session.getAttribute(SystemParameter.LOGIN_USER_KEY);  
    	return user != null;    	
    }
    
    private Properties autoMapping() {
        Properties properties = new Properties();
        Class<?> cls = this.getClass();
        while (!cls.equals(MultiController.class)) {
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                int mod = method.getModifiers();
                if (Modifier.isPublic(mod) || Modifier.isProtected(mod)) {
                    Class<?>[] parameter = method.getParameterTypes();
                    if (parameter != null && parameter.length >= 2) {
                        if (parameter[0].equals(HttpServletRequest.class) &&
                                parameter[1].equals(HttpServletResponse.class)) {
                            if (method.getReturnType() != null &&
                                    method.getReturnType().equals(ModelAndView.class)) {
                                properties.put("/" + method.getName() + urlSuffix, method.getName());
                            }
                        }
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return properties;
    }

    public void afterPropertiesSet() {

        methodMappings = autoMapping();
        if (forwardMappings != null && forwardMappings.length > 0) {
            for (String str : forwardMappings) {
                String key = str.trim();
                while (key.startsWith("/")) {
                    key = key.substring(1).trim();
                }
                key = "/" + key;
                methodMappings.put(key, "forward");       //请求动作到页面的映射
            }
        }

        if (methodMappings.isEmpty()) {
            if (this.logger.isInfoEnabled()) {
                this.logger.info("not url binding...... ");
            }
        } else {
            PropertiesMethodNameResolver propertiesMethodNameResolver = new PropertiesMethodNameResolver();
            propertiesMethodNameResolver.setMappings(methodMappings);
            this.setMethodNameResolver(propertiesMethodNameResolver);
        }

//        registerMultiHandler();

    }

//    protected void registerMultiHandler(){
////        System.out.println(getWebApplicationContext().containsBean(multiHandler));
//        if(getWebApplicationContext().containsBean(multiHandler)){
//            MultiControllerUrlHandlerMapping mapping = (MultiControllerUrlHandlerMapping)getWebApplicationContext().getBean(multiHandler);
//            mapping.initApplicationContext();
//        }
//
//    }

    public Properties getMethodMappings() {
        return methodMappings;
    }

    public void setMethodMappings(Properties methodMappings) {
        this.methodMappings = methodMappings;
    }

    public String[] getForwardMappings() {
        return forwardMappings;
    }

    public void setForwardMappings(String[] forwardMappings) {
        this.forwardMappings = forwardMappings;
    }

    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }

    /**
     * 动作直接跳传页面
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return ModelAndView
     */
    public final ModelAndView forward(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(getView(request));
    }  

    //获得代理类的目标类

    /**
     * 获得代理类的目录类
     *
     * @param service 代理服务
     * @return Class 代理类名
     */
    protected Class<?> getTargetClass(Object service) {
        Class<?> cls;
        if (AopUtils.isAopProxy(service)) {
            if (AopUtils.isCglibProxy(service)) {
                cls = service.getClass().getSuperclass();
            } else {
                cls = ((TargetClassAware) service).getTargetClass();
            }
        } else {
            cls = service.getClass();
        }
        return cls;
    }
 
    /**
     * 根据请求动作得到页面 如 sysUserView.do 页面为 sysUserView.jsp
     *
     * @param request 请求对象
     * @return String 页面名称
     */
    protected String getView(HttpServletRequest request) {
        String view = getMethod(request).concat(".jsp");
        if (viewPath != null && viewPath.length() > 0) {
            view = viewPath.concat("/").concat(view);
        }
        return view;
    }

    /**
     * 获得不带路径,不带参数的请求动作 如 /sysUserView.do
     *
     * @param request 请求对象
     * @return String 相对动作
     */
    protected String getURI(HttpServletRequest request) {
        String uri = request.getRequestURI();
        int index = uri.lastIndexOf("/");
        if (index != -1) {
            uri = uri.substring(index);
        }
        return uri;
    }

    /**
     * 获得请示动作对应的方法名
     *
     * @param request 请求对象
     * @return String 方法名
     */
    protected String getMethod(HttpServletRequest request) {
        String uri = getURI(request);
        int index = uri.indexOf(".");
        String method = (index != -1) ? uri.substring(0, index) : uri;
        while (method.startsWith("/")) {
            method = method.substring(1);
        }
        return method;
    }

    /**
     * 重定向一个请求动作
     *
     * @param url 动作
     * @return View
     */
    protected String getRedirect(String url) {
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + url;
    }

    /**
     * 转发请求,从一个请求动作中转发到另一个请求动作
     *
     * @param url 请求动作
     * @return view 视图对象
     */
    protected String getForward(String url) {
        return UrlBasedViewResolver.FORWARD_URL_PREFIX + url;
    }

    /**
     * 响应文本
     *
     * @param response 响应对象
     * @param text     输出文本
     * @throws java.io.IOException 异常
     */
    protected void responseText(HttpServletResponse response, String text) throws IOException {
        PrintWriter out = null; // 输出对象
        try {
            response.reset();
            response.setContentType("text/html; charset=UTF-8");
            out = response.getWriter();
            out.write(text);
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


    /**
     * 将字符串以xml形式输出到客户端
     *
     * @param response 响应对象
     * @param xml      输出的xml字符串
     * @throws IOException 抛出异常
     */
    protected void responseXML(HttpServletResponse response, String xml) throws IOException {
        PrintWriter out = null; // 输出对象
        try {
            response.reset();
            response.setContentType("text/xml; charset=UTF-8");
            out = response.getWriter();
            out.write(xml);
            out.flush();
        }
        finally {
            if (out != null) {
                out.close();
            }
        }
    } 

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        WebUtil.setRequest(request);
        WebUtil.setResponse(response);
        return super.handleRequestInternal(request, response);
    }
}
