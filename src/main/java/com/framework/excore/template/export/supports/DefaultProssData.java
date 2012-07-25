package com.framework.excore.template.export.supports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import com.framework.excore.template.export.AbstractProcessValidation;
import com.framework.excore.template.export.DefaultConfiguration;
import com.framework.excore.template.export.ExportConstants;
import com.framework.excore.template.export.IProcessData;

import freemarker.template.Template;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-4-23
 * @Time: 上午11:29:54
 * @Description :生成文件流（txt流 ,xls流）
 */ 
public class DefaultProssData extends AbstractProcessValidation implements IProcessData {
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午11:29:07
	 * @param ftlName
	 *            模板文件
	 * @param dataSource
	 *            数据源
	 * @return OutputStream
	 * @throws Exception
	 * @Description: 处理后的文件 流
	 */
	public ByteArrayOutputStream processFileStream(String ftlName,
			Map<String, Object> dataSource) throws Exception {
		// 缓冲区
		ByteArrayOutputStream stream = null;
		Writer out = null;
		try {
			// 检查数据的合法性
			this.checkParam(ftlName, dataSource);
			// 得到配置中心
			DefaultConfiguration config = DefaultConfiguration
					.getInstancesConfiguration();
			// 模板文件
			Template temp = config.getConfig().getTemplate(ftlName);
			temp.setEncoding(ExportConstants.CODE_UTF8);
			// 缓冲区
			stream = new ByteArrayOutputStream();
			// 将数据写入缓存区中
			out = new OutputStreamWriter(stream,ExportConstants.CODE_UTF8);
//			out = new OutputStreamWriter(stream);
			// 解析模板文件
			temp.process(dataSource, out);
			out.flush();

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			// 关闭流
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					throw new Exception(e.getMessage());
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					throw new Exception(e.getMessage());
				}
			}
		}
		// 返回
		return stream;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午11:29:07
	 * @param ftlName
	 *            模板文件
	 * @param dataSource
	 *            数据源
	 * @return byte[]
	 * @throws Exception
	 * @Description: 处理后的文件 流
	 */
	public byte[] processByteCache(String ftlName,
			Map<String, Object> dataSource) throws Exception {
		// 缓冲区
		ByteArrayOutputStream stream = null;
		// 解析文件
		stream = this.processFileStream(ftlName, dataSource);
		// 返回
		return stream.toByteArray();
	}


}
