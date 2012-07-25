package com.framework.excore.template.export;

import java.util.Map; 
/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-5-9
 * @Time: 上午10:47:43
 * @version 1.0
 * @Description :验证数据的合法性
 */
public abstract class AbstractProcessValidation {

	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午11:29:07
	 * @param ftlName
	 *            模板文件
	 * @param dataSource
	 *            数据源
	 * @throws ServiceException
	 * @throws Exception
	 * @Description: 检查参数的 合法性
	 */
	protected void checkParam(String ftlName, Map<String, Object> dataSource)
					throws Exception {
		if (ftlName == null || ftlName.trim().length() == 0) {
			throw new Exception("模板文件名不能为空!");
		}
		if (dataSource == null || dataSource.isEmpty()) {
			throw new Exception("数据源不能为空!");
		}
	}
}
