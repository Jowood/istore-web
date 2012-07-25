package com.istore.ebook.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import com.istore.ebook.controller.ProjectExistException;
import com.istore.ebook.controller.ProjectNotExistException;
import com.istore.entity.Project;

public interface ProjectService {
	public List<Project> query(Map<String,String> params);

	public void insertProject(Project project)throws DocumentException, IOException,ProjectExistException;

	public void deleteProject(Project project)throws DocumentException, IOException,ProjectExistException;

	public void updateProject(Project project)throws DocumentException, IOException,ProjectExistException,ProjectNotExistException;
}
