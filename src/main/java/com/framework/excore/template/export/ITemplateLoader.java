package com.framework.excore.template.export;

import freemarker.cache.MultiTemplateLoader;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-4-23
 * @Time: 上午10:41:13
 * @Description : 模板加载
 */
public interface ITemplateLoader {
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午10:42:18
	 * @return MultiTemplateLoader
	 * @Description:  得到模板加哉器
	 */
	public MultiTemplateLoader  getTemplateLoader();

}
