package com.istore.login.controller;
  

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.web.servlet.ModelAndView;

import com.framework.controller.MultiController;
import com.framework.system.SystemParameter;
import com.framework.util.BeanUtil;
import com.framework.util.WebUtil;
import com.istore.entity.LoginResult;
import com.istore.entity.User; 
import com.istore.login.service.UserExistException;
import com.istore.login.service.UserLoginService;
import com.istore.login.service.UserManagementService;

public class LoginController  extends MultiController {
	
	private static Logger logger = Logger.getLogger(LoginController.class.getName());
	
	private UserLoginService userLoginService;
	private UserManagementService userManagement; 
	
	public UserManagementService getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagementService userManagement) {
		this.userManagement = userManagement;
	}

	public UserLoginService getUserLoginService() {
		return userLoginService;
	}

	public void setUserLoginService(UserLoginService userLoginService) {
		this.userLoginService = userLoginService;
	}

	/**
     * 用户登录
     *
     * @param request  请求
     * @param response 响应
     * @return ModelAndView
     * @throws Exception 异常
     */
    public ModelAndView userLogin(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    	return forward(request, response); 
    }

    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    	HttpSession session = WebUtil.getSession();
    	session.removeAttribute(SystemParameter.LOGIN_USER_KEY);
    	return new ModelAndView("login/userLogin.jsp");
    }
    
	/**
     * 检查登录用户名和密码
     * @param request  请求
     * @param response 响应
     * @return ModelAndView
     * @throws Exception 异常
     */
    public ModelAndView checkUserLogin(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    	if (isLogin()) {
    		return new ModelAndView("login/main.jsp"); 
    	}
    	User user = (User) BeanUtil.createBean(request, User.class);
    	LoginResult loginResult = userLoginService.checkLogin(user);
    	if (loginResult.getStatus() < 0) {
    		return new ModelAndView("login/userLogin.jsp", BeanUtil.toMap(loginResult));
    	} else if (loginResult.getStatus() == 1) {
    		HttpSession session = WebUtil.getSession();
    		session.setAttribute(SystemParameter.LOGIN_USER_KEY, user); 
    		logger.info(user.getName()+"：登录系统");
    		return new ModelAndView("login/main.jsp", BeanUtil.toMap(loginResult));
    	}
    	return null; 
    }
    
    @SuppressWarnings("unchecked")
    public ModelAndView selectUser(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    	if (!isLogin()) {
    		return new ModelAndView("login/userLogin.jsp"); 
    	}
		Map<String,String> params = BeanUtil.createBean(request, HashMap.class);
    	List<User> list = null; 
    	Map<String,Object> result = new HashMap<String,Object>(); 
        list = userManagement.query(params);   
        result.put("list", list);
        result.putAll(params);
    	return new ModelAndView(getView(request), result);
    }

    public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception{ 
    	User user = (User) BeanUtil.createBean(request, User.class);
    	Map<String,String> result = new HashMap<String,String>(); 
    	boolean success = true;
    	try {
			userManagement.update(user);
		} catch (UserExistException e) {
			logger.error("不存在该用户");
			result.put("errorMsg", "不存在该用户");
			success = false;
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "用户文件无法读取！");
			success = false;
		} catch (IOException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "用户文件无法写入！");
			success = false;
		}
    	result.put("success", String.valueOf(success));
    	return new ModelAndView(getView(request), result);    	
    }    
    
    public ModelAndView deleteUser(HttpServletRequest request, HttpServletResponse response) throws Exception{ 
    	User user = (User) BeanUtil.createBean(request, User.class);
    	Map<String,String> result = new HashMap<String,String>(); 
    	boolean success = true;
    	try {
			userManagement.delete(user);
		} catch (UserExistException e) {
			logger.error("不存在该用户");
			result.put("errorMsg", "不存在该用户");
			success = false;
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "用户文件无法读取！");
			success = false;
		} catch (IOException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "用户文件无法写入！");
			success = false;
		}
    	result.put("success", String.valueOf(success));
    	return new ModelAndView(getView(request), result);    	
    }
    
    public ModelAndView insertUser(HttpServletRequest request, HttpServletResponse response) throws Exception{ 
    	
    	User user = (User) BeanUtil.createBean(request, User.class);
    	Map<String,String> result = new HashMap<String,String>(); 
    	boolean success = true;
    	try {
			userManagement.insert(user);
		} catch (UserExistException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "登录名已经存在");
			success = false;
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "用户文件无法读取！");
			success = false;
		} catch (IOException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "用户文件无法写入！");
			success = false;
		}
    	result.put("success", String.valueOf(success));
    	return new ModelAndView(getView(request), result);
    } 

}
