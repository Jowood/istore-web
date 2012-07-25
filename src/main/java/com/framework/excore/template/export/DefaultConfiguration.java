package com.framework.excore.template.export;

import java.io.IOException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.framework.excore.template.export.supports.DefaultTemplateLoader; 

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-4-23
 * @Time: 上午11:07:53
 * @Description :默认配置器
 */
public class DefaultConfiguration  {
	
	private static  DefaultConfiguration  defaultConfig = new DefaultConfiguration();

	/* description: 配置器 */
	private  Configuration config = null;

	/* description: 共享变量 */
	private Map<String, Object> shareMap = new HashMap<String, Object>();

	/* description: 模板加载器 */
	private ITemplateLoader tempLateLoader = null;

	/* description: 本地化 */
	private Locale locale = null;

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午11:27:15
	 * @param _tempLateLoader
	 * @param _locale
	 * @throws Exception 
	 * @Description: 构造函数
	 */
	private DefaultConfiguration(Locale _locale) {
		this();
		this.locale = _locale;
        
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午11:27:17
	 * @param _tempLateLoader
	 * @throws Exception 
	 * @throws IOException 
	 * @throws TemplateModelException 
	 * @Description: 构造函数
	 */
	private DefaultConfiguration() {
		DefaultTemplateLoader loader = new DefaultTemplateLoader();
		try {
			loader.initTemplateLoader();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage()) ;
		}
		this.tempLateLoader = loader;
		this.config = new Configuration();
		this.initShareMap();
		try {
			this.initConfiguration();
		} catch (TemplateModelException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage()) ;
		}

	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午11:19:09
	 * @throws TemplateModelException
	 * @Description: 初始化配置器
	 */
	public void initConfiguration() throws TemplateModelException {

		// 本地化资源
		if (locale != null) {
			this.config.setLocale(locale);
		}
		// 共享变量
		if (!shareMap.isEmpty()) {
			for (Map.Entry<String, Object> shareVar : shareMap.entrySet()) {
				this.config.setSharedVariable(shareVar.getKey(),
								shareVar.getValue());
			}
		}

		//设置编码为utf-8
		this.config.setEncoding(Locale.CHINA, ExportConstants.CODE_UTF8) ;
		// 初始化模板加载路经
		this.config.setTemplateLoader(tempLateLoader.getTemplateLoader());

	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午11:19:40
	 * @Description: 初始化共享变量
	 */
	public void initShareMap() {
		//demo:
//		this.shareMap.put("hw", "helloWorld") ;

	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午11:53:14
	 * @param _locale
	 * @return
	 * @throws Exception 
	 * @Description: 初始化  DefaultConfiguration 
	 */
	public static  DefaultConfiguration getInstancesConfiguration(Locale _locale) throws Exception {
		if( defaultConfig==null){
			defaultConfig = new DefaultConfiguration(_locale);
		}
		return defaultConfig;
	}
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午11:53:14
	 * @param _locale
	 * @return
	 * @throws Exception 
	 * @Description: 初始化  DefaultConfiguration 
	 */
	public static  DefaultConfiguration getInstancesConfiguration() throws Exception {
		if( defaultConfig==null){
			defaultConfig = new DefaultConfiguration();
		}
		return defaultConfig;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午11:54:57
	 * @return  config
	 * @Description: 返回config的值
	 */
	public  Configuration getConfig() {
		return config;
	}
}
