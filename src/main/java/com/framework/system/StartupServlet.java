package com.framework.system;

import java.io.File;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
 
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.framework.exception.StartupException;
import com.framework.util.StringUtil;
import com.snda.storage.security.ProviderCredentials;
import com.snda.storage.security.SNDACredentials;
import com.snda.storage.service.impl.rest.httpclient.RestCSService;

/**
 * 系统启动servlet, 解析 WEB-INF/config/system.xml 系统参数文件， 初始化SystemParameter对象
 * 
 * @author 姜敏
 * @version 4.0
 */
public class StartupServlet extends javax.servlet.http.HttpServlet {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(getClass());

	public StartupServlet() {
		super();
	}

	public void init() throws ServletException {
		String configName = this.getServletContext().getRealPath(
				this.getInitParameter("configFile"));
		String systemName = "";
		try {
			File file = new File(configName);
			if (!file.exists())
				throw new StartupException("系统参数文件" + configName + "未找到");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(file);
			Element root = doc.getRootElement();

			for (Iterator<?> i = root.elementIterator(); i.hasNext();) {
				Element element = (Element) i.next();
				String key = element.element("key").getText();
				String value = element.element("value").getText();
				SystemParameter.set(key, value);
			}
			if (!StringUtil.isEmpty(SystemParameter.get("accessKey"))
					&&
					!StringUtil.isEmpty(SystemParameter.get("secretAccessKey"))){ 
				ProviderCredentials credentials = new  SNDACredentials( SystemParameter.get("accessKey"),SystemParameter.get("secretAccessKey") );
				SystemParameter.setGrandCloud(new RestCSService(credentials));				
			}

			ServletContext application=getServletContext();
 
			systemName = SystemParameter.get("systemName");
			logger.info(application.getRealPath("/")); 
			logger.info("      " + systemName + "初始化完成！"); 

		} catch (Exception e) {
			logger.error("exception class:" + e.getClass().getName()+ "\nexception message:" + e.getMessage());
			throw new ServletException(e);
		}
	} 
}