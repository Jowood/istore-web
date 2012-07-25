package com.framework.excore.template.export.supports;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.framework.excore.template.export.ExportConstants;
import com.framework.excore.template.export.ITemplateLoader;
import com.framework.excore.template.export.LoaderTemplateProperties;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-4-23
 * @Time: 上午10:46:11
 * @Description :模板加载
 */
public class DefaultTemplateLoader implements ITemplateLoader {

	/* description: 加载器路经 */
	private List<TemplateLoader> fileLoaders = new ArrayList<TemplateLoader>();

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午10:52:43
	 * @throws IOException
	 * @Description: 初始化加载路经
	 */
	public void initTemplateLoader() throws Exception {

		// 得到当前的classpath
		URL url = this.getClass().getClassLoader().getResource("");
		// 路经
		String path = url.getPath();
		//转义空格 等特殊字符
		path = URLDecoder.decode(path,ExportConstants.CODE_UTF8);
		
//		// 1.排产发交 模板路经
//		FileTemplateLoader pcfjTemp = new FileTemplateLoader(new File(path
//						+ "testFtl/"));
//
//		// 2.需求计算 模板路经
//		
//		
//		
//		//3.参考系      模板路经
//		
//		
//		//排产发交
//		fileLoaders.add(pcfjTemp);
//		//需求计算
//		
//		//参考系
		
		//得到所有的要加载模板路经
		Set<String> tempPath  = LoaderTemplateProperties.loadTemplate();
		//迭代加载
		for(Iterator<String> it = tempPath.iterator();it.hasNext();){
			//模板路经
			String _path = it.next();
			//去掉前面的'/'
			if(_path.startsWith("/")){
				_path = _path.substring(1) ;
			}
			//增加后面的'/'
			if(!_path.endsWith("/")){
				_path = _path+"/" ;
			}
			
			FileTemplateLoader templateFile = new FileTemplateLoader(new File(path
					+ _path));
			fileLoaders.add(templateFile);
			
		}
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ahena.util.export.ITemplateLoader#getTemplateLoader()
	 */
	public MultiTemplateLoader getTemplateLoader() {

		TemplateLoader[] temp = new TemplateLoader[fileLoaders.size()];
		for (int i = 0; i < fileLoaders.size(); i++) {
			temp[i] = fileLoaders.get(i);
		}

		/** 初始化模板加载器 **/
		MultiTemplateLoader multi = new MultiTemplateLoader(temp);

		return multi;
	}

}
