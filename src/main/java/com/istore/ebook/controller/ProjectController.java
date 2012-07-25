package com.istore.ebook.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.springframework.web.servlet.ModelAndView;

import com.framework.controller.MultiController;
import com.framework.util.BeanUtil;
import com.istore.ebook.service.ProjectService; 
import com.istore.entity.Project; 

public class ProjectController extends MultiController {
	private ProjectService projectService;

 
    public ProjectService getProjectService() {
		return projectService;
	}


	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}


	@SuppressWarnings("unchecked")
	public ModelAndView manageProject(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    	if (!isLogin()) {
    		return new ModelAndView("login/userLogin.jsp"); 
    	}
		Map<String,String> params = BeanUtil.createBean(request, HashMap.class);
		List<Project> list = null; 
    	Map<String,Object> result = new HashMap<String,Object>(); 
        list = projectService.query(params);
        result.put("list", list);
        result.putAll(params);
    	return new ModelAndView(getView(request), result);
    }
	
	public ModelAndView createProject(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		Project project = (Project) BeanUtil.createBean(request, Project.class);
    	Map<String,String> result = new HashMap<String,String>(); 
    	boolean success = true;
    	try {
    		projectService.insertProject(project);
		} catch (ProjectExistException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "项目已经存在");
			success = false;
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "项目文件无法读取！");
			success = false;
		} catch (IOException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "项目文件无法写入！");
			success = false;
		}
    	result.put("success", String.valueOf(success));
    	return new ModelAndView(getView(request), result);
	}
	
	public ModelAndView deleteProject(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		Project project = (Project) BeanUtil.createBean(request, Project.class);
    	Map<String,String> result = new HashMap<String,String>(); 
    	boolean success = true;
    	try {
    		projectService.deleteProject(project);
		} catch (ProjectExistException e) {
			logger.error("项目不存在");
			result.put("errorMsg", "项目不存在");
			success = false;
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "项目文件无法读取！");
			success = false;
		} catch (IOException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "项目文件无法写入！");
			success = false;
		}
    	result.put("success", String.valueOf(success));
    	return new ModelAndView(getView(request), result);    	
	}
	
	
	
	public ModelAndView updateProject(HttpServletRequest request, HttpServletResponse response) throws Exception{ 
		Project project = (Project) BeanUtil.createBean(request, Project.class);
    	Map<String,String> result = new HashMap<String,String>(); 
    	boolean success = true;
    	try {
    		projectService.updateProject(project);
		} catch (ProjectExistException e) {
			logger.error("项目已经存在");
			result.put("errorMsg", "项目已经存在");
			success = false;
		} catch (ProjectNotExistException e) {
			logger.error("项目不存在");
			result.put("errorMsg", "项目不存在");
			success = false;
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "项目文件无法读取！");
			success = false;
		} catch (IOException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "项目文件无法写入！");
			success = false;
		}
    	result.put("success", String.valueOf(success));
    	return new ModelAndView(getView(request), result);    	
    }  
}
