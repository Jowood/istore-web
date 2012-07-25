package com.istore.ebook.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.framework.util.FileUtil;
import com.framework.util.StringUtil;
import com.framework.util.WebUtil;
import com.istore.ebook.controller.ProjectExistException;
import com.istore.ebook.controller.ProjectNotExistException;
import com.istore.entity.Catalog;
import com.istore.entity.Project;
import com.istore.system.FileExistException;
import com.istore.system.SystemCatalog;
import com.istore.system.SystemProject;

public class NoSQLProjectService implements ProjectService {
	private static Logger logger = Logger.getLogger(NoSQLProjectService.class.getName());
	private EBookService ebookService;
	
	public EBookService getEbookService() {
		return ebookService;
	}

	public void setEbookService(EBookService ebookService) {
		this.ebookService = ebookService;
	}

	@Override
	public List<Project> query(Map<String, String> params) { 
		logger.info("项目管理--查询项目列表");
		return SystemProject.getList();
	}
	
	@Override
	public void insertProject(Project project)throws DocumentException, IOException,ProjectExistException {
		logger.info("项目管理--添加新项目");
		if (SystemProject.getKey(project.getName()) == null) {
			Document doc = getProjectDocument(); 
            Element root = doc.getRootElement();
            Element userEL = root.addElement("Project");
            
            project.setKey(StringUtil.getPrimaryKey()); 
            userEL.addAttribute("key", project.getKey());
            userEL.addElement("Name").addText(project.getName());
            userEL.addElement("Key").addText(project.getKey()); 
            FileUtil.writeXML(doc, getProjectFile());
            SystemProject.set(project.getKey(), project); 
		} else {
			throw new ProjectExistException();
		}		
	}
	
	public File getProjectFile() {
		String userFile = WebUtil.getServletContext().getRealPath("/WEB-INF/config/project.xml");
		File file = new File(userFile);
        if (!file.exists()) {    //没有找到用户对应的配置文件　用户不存在
        	throw new FileExistException();
        }
        return file;
	}
	
	public Document getProjectDocument() throws DocumentException { 
        SAXReader reader = new SAXReader(); 
        return reader.read(getProjectFile());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteProject(Project project) throws DocumentException,
			IOException, ProjectExistException {
		logger.info("项目管理--删除项目");
		Project aProject = SystemProject.get(project.getKey());
		if (aProject != null) {
			// 删除文件 
			Map<String,String> params = new HashMap<String,String>();
			params.put("selectProjectKey", project.getKey());
			Map<String, ?> result = SystemCatalog.getList(params);
			List<Catalog> list = (List<Catalog>)result.get("list");
			for(Catalog catalog : list) {
				ebookService.deleteCatalog(catalog); 
			}
			
			Document doc = getProjectDocument();
	        List<?> els = doc.selectNodes("/Projects/Project[@key='"+ project.getKey() + "']" );
			Iterator<?> iter = els.iterator(); 
			Element root = doc.getRootElement();
			while (iter.hasNext()) {
				Element el = (Element) iter.next();
				root.remove(el);
			} 
            FileUtil.writeXML(doc, getProjectFile());
            SystemProject.delete(project);
			
		} else {
			throw new ProjectExistException();
		}
		
	}

	@Override
	public void updateProject(Project project) throws DocumentException,
			IOException, ProjectExistException,ProjectNotExistException {
		logger.info("项目管理--修改项目");
		if (SystemProject.get(project.getKey()) != null) {
			if(SystemProject.isExist(project)){
				throw new ProjectExistException();
			}
			Document doc = getProjectDocument(); 
	        List<?> list = doc.selectNodes("/Projects/Project[@key='"+ project.getKey() + "']" );
			Iterator<?> iter = list.iterator(); 
			while (iter.hasNext()) {
				Element el = (Element) iter.next();  
				if (!StringUtil.isEmpty(project.getName())) { 
					el.element("Name").setText(project.getName());					
				} 
			}
            FileUtil.writeXML(doc, getProjectFile());
            SystemProject.get(project.getKey()).setName(project.getName()); 
		} else {
			throw new ProjectNotExistException();
		}
		
	}
}
