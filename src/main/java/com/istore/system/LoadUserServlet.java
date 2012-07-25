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
import com.istore.entity.User;

public class LoadUserServlet extends HttpServlet { 
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(LoadUserServlet.class.getName());
	
	public void init() throws ServletException {
		String configName = this.getServletContext().getRealPath(
				this.getInitParameter("configFile")); 
		try {
			File file = new File(configName);
			if (!file.exists())
				throw new StartupException("系统用户文件" + configName + "未找到");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(file);
			Element root = doc.getRootElement();

			for (Iterator<?> i = root.elementIterator(); i.hasNext();) {
				Element element = (Element) i.next();
				String loginName = element.element("LoginName").getText();
				String name = element.element("Name").getText();
				String password = element.element("Password").getText();
				User user = new User();
				user.setLoginName(loginName);
				user.setName(name);
				user.setPassword(password);
				SystemUser.set(loginName, user);
			} 
			logger.info("系统用户初始化完成！"); 

		} catch (Exception e) {
			logger.error("exception class:" + e.getClass().getName()+ "\nexception message:" + e.getMessage());
			throw new ServletException(e);
		}
	}
}
