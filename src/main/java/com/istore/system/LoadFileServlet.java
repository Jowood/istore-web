package com.istore.system;

import java.io.File;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.framework.exception.StartupException;
import com.istore.entity.Catalog;
import com.istore.entity.StoreFile;

public class LoadFileServlet extends HttpServlet { 
	private static final long serialVersionUID = 1L; 
	private static Logger logger = Logger.getLogger(LoadFileServlet.class.getName());
	
	public void init() throws ServletException {
		String configName = this.getServletContext().getRealPath(
				this.getInitParameter("configFile")); 
		try {
			File file = new File(configName);
			if (!file.exists())
				throw new StartupException("系统存储文件" + configName + "未找到");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(file);
			Element root = doc.getRootElement();
			int count = 0;
			for (Iterator<?> i = root.elementIterator(); i.hasNext();) {
				Element element = (Element) i.next();
				String name = element.element("Name").getText();
				String catalogKey = element.element("CatalogKey").getText();
				String key = element.element("Key").getText();
				String size = "";
				if (element.element("Size") != null) {
					size = element.element("Size").getText();
				}
				Catalog catalog = SystemCatalog.get(catalogKey); 
				if(catalog == null) continue;
				StoreFile storeFile = new StoreFile();
				storeFile.setCatalogKey(catalogKey);
				storeFile.setKey(key);
				storeFile.setName(name);
				storeFile.setSize(size);
				catalog.getList().add(storeFile); 
				count++;
			} 
			logger.info("文件数量为"+count); 
			logger.info("系统存储文件初始化完成！"); 

		} catch (Exception e) {
			logger.error("exception class:" + e.getClass().getName()+ "\nexception message:" + e.getMessage());
			throw new ServletException(e);
		}		
	}
}
