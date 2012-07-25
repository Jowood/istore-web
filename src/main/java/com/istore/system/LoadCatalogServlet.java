package com.istore.system;

import java.io.File;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
 
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.framework.exception.StartupException;
import com.istore.entity.Catalog;

public class LoadCatalogServlet extends HttpServlet { 
	private static final long serialVersionUID = 1L; 
	private static Logger logger = Logger.getLogger(LoadCatalogServlet.class.getName());
	
	public void init() throws ServletException {
		String configName = this.getServletContext().getRealPath(
				this.getInitParameter("configFile")); 
		try {
			File file = new File(configName);
			if (!file.exists())
				throw new StartupException("系统目录文件" + configName + "未找到");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(file);
			Element root = doc.getRootElement();
			int count = 0;
			for (Iterator<?> i = root.elementIterator(); i.hasNext();) {
				Element element = (Element) i.next();
				String name = element.element("Name").getText();
				String key = element.element("Key").getText(); 
				String project = element.element("Project").getText();
				Catalog catalog = new Catalog();
				catalog.setName(name);
				catalog.setKey(key);
				catalog.setParent(element.element("Parent").getText());
				catalog.setLevel(element.element("Parent").getText());
				catalog.setProjectKey(project); 
				if (StringUtils.isNotEmpty(catalog.getParent())) {
					Catalog parent = SystemCatalog.get(catalog.getParent());
					if (parent != null) {
						parent.addChildren(catalog);
					} 
				} 
				SystemCatalog.set(key, catalog);  	
				count++;
			} 
			logger.info("文件夹数量为"+count); 
			logger.info("系统目录初始化完成！"); 

		} catch (Exception e) {
			logger.error("exception class:" + e.getClass().getName()+ "\nexception message:" + e.getMessage());
			throw new ServletException(e);
		}		
	}
}
