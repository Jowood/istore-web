package com.framework.excore.template.export;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
 

/**
 * 
 * @author 王冲
 * @date 2012-04-25
 * @time 16:52
 * @description 加载资源文件
 * 
 */
public class LoaderTemplateProperties {
	/**
	 * @author 王冲
	 * @date 2012-04-25
	 * @time 16:53
	 * @return Set<String>
	 * @throws IOException
	 * @descrption 加载模板路经
	 */
	public static Set<String> loadTemplate() throws IOException,Exception {
		Set<String> temps = new HashSet<String>();
		//当前类的classloader
		ClassLoader loader = LoaderTemplateProperties.class.getClassLoader();
		//得到classpath下的资源文件
		ResourceBundle properties = new PropertyResourceBundle(
				loader.getResourceAsStream("template.properties"));
		//得到所有的模板路经
		for (Iterator<String> it = properties.keySet().iterator(); it.hasNext();) {
			//路经Key
			String pathKey = it.next().trim();
			//路经
			String path = properties.getString(pathKey).trim();
			if(path.length()==0){
				throw new Exception(pathKey+" 的模板路经不能为空!") ;
			}
			temps.add(path.trim());
//			temps.add(properties.getString(it.next().trim()).trim());
		}
		return temps;
	}
}
