package com.framework.excore.template.export;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.framework.excore.template.export.supports.DefaultProssData;
import com.framework.excore.template.export.supports.PdfProcessData; 

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-4-27
 * @Time: 下午09:32
 * @Description :文件 处理工厂类
 */
public class ProcessDataFactory {

	private static Map<String, IProcessData> processDatas = Collections
			.synchronizedMap(new HashMap<String, IProcessData>());

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-27
	 * @Time: 下午09:32
	 * @Description :得到工厂实例
	 */
	public static ProcessDataFactory getProcessFactoryInstatnces() {
		return new ProcessDataFactory();
	}

	/**
	 * @throws Exception 
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-27
	 * @Time: 下午09:32
	 * @Description :数据处理
	 */
	public IProcessData getProcessDataInstatnces(String suffix) throws Exception {

		// 验证文件名是否为空
		if (suffix == null || suffix.trim().length() == 0) {
			throw new Exception("文件后缀名不能为空!");
		}

		// 如果已经实例化了,则直接返回
		if (processDatas.containsKey(suffix.toLowerCase())) {
			return processDatas.get(suffix);
		}

		// 文件处理类
		IProcessData process = null;
		// 如果处理的是txt格式的
		if (suffix.equalsIgnoreCase(ExportConstants.FILE_TXT)) {
			// 实例化txt处理类
			process = new DefaultProssData();
		}
		// 如果是xls处理
		else if (suffix.equalsIgnoreCase(ExportConstants.FILE_XLS)) {
			// 则实例化xls处理类
			process = new DefaultProssData();
		}
		// 如果是pdf格式处理
		else if (suffix.equalsIgnoreCase(ExportConstants.FILE_PDF)) {
			// 则实例化pdf处理类
			process = new PdfProcessData();
		} else {
			throw new Exception("不支持此文件格式的导出!");
		}

		// 缓存起来
		processDatas.put(suffix.toLowerCase(), process);

		return process;

	}

}
