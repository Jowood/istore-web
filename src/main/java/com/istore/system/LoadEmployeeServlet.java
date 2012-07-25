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
import com.istore.entity.Employee;

public class LoadEmployeeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	private static Logger logger = Logger.getLogger(LoadEmployeeServlet.class.getName());
	
	public void init() throws ServletException {
		String configName = this.getServletContext().getRealPath(
				this.getInitParameter("configFile")); 
		try {
			File file = new File(configName);
			if (!file.exists())
				throw new StartupException("系统雇员文件" + configName + "未找到");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(file);
			Element root = doc.getRootElement();

			for (Iterator<?> i = root.elementIterator(); i.hasNext();) {
				Element element = (Element) i.next();
				String key = element.element("key").getText(); 
				Employee emp = new Employee();
				emp.setKey(key);
				emp.setName(element.element("name").getText());
				emp.setOrg(element.element("org").getText());
				emp.setStart(element.element("start").getText());
				emp.setEnd(element.element("end").getText());
				emp.setVisa(element.element("visa").getText());
				emp.setAddress(element.element("address").getText());
				SystemEmployee.set(key, emp);  	
			} 
			logger.info("系统雇员初始化完成！"); 

		} catch (Exception e) {
			logger.error("exception class:" + e.getClass().getName()+ "\nexception message:" + e.getMessage());
			throw new ServletException(e);
		}		
	}

}
