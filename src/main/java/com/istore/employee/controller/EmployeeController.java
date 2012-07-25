package com.istore.employee.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.framework.controller.MultiController;
import com.framework.util.BeanUtil;
import com.istore.employee.service.EmployeeService;
import com.istore.entity.Employee;

public class EmployeeController extends MultiController {
	private EmployeeService employeeService;

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    	if (!isLogin()) {
    		return new ModelAndView("login/userLogin.jsp"); 
    	} 
    	Map<String,String> params = BeanUtil.createBean(request, HashMap.class);
    	Map<String,Object> result = new HashMap<String,Object>(); 
    	List<Employee> list = employeeService.search(params);
        result.put("list", list);
        result.put("pageEnd", list.size());
        result.putAll(params);
        return new ModelAndView(getView(request), result);  	
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView batch(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    	if (!isLogin()) {
    		return new ModelAndView("login/userLogin.jsp"); 
    	}  
    	
		Map<String,String> map = BeanUtil.createBean(request, HashMap.class); 
    	if (map.containsKey("insert") && StringUtils.isNotEmpty(map.get("insert"))) {
    		List<Employee> insert = BeanUtil.createBeanList(map.get("insert"), Employee.class);
    		employeeService.batchInsert(insert);
    	}
    	if (map.containsKey("update") && StringUtils.isNotEmpty(map.get("update"))) {
    		List<Employee> update = BeanUtil.createBeanList(map.get("update"), Employee.class);
    		employeeService.batchUpdate(update);    		
    	}
    	return new ModelAndView(this.getRedirect("search.emp"));
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    	if (!isLogin()) {
    		return new ModelAndView("login/userLogin.jsp"); 
    	}  
    	Map<String,String> params = BeanUtil.createBean(request, HashMap.class);
    	employeeService.delete(params);
		return new ModelAndView(this.getRedirect("search.emp"));
	}
}
