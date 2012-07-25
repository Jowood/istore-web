package com.framework.excore.template;



import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.framework.excore.template.export.ExportConstants;
import com.framework.excore.template.export.IProcessData;
import com.framework.excore.template.export.ProcessDataFactory;
import com.framework.excore.template.export.WrapResponse;
import com.framework.excore.template.export.supports.ProcessDataStaticProxy; 

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-4-25
 * @Time: 下午4:42:21
 * @Description :文件下载
 */ 
public class DownLoadServices {

	/**
	 * 
	 * @param ftlName
	 *            模板文件名
	 * @param dataSource
	 *            数据源
	 * @param response  HttpServletResponse对象
	 * @param filePrefix
	 *            文件前缀
	 * @param fileSuffix
	 *            文件后缀
	 * @param isZip
	 *            是否压缩
	 * @throws Exception
	 */
	public void downloadFile(String ftlName,final Map<String, Object> dataSource,
			HttpServletResponse response, String filePrefix, String fileSuffix,
			boolean isZip) throws Exception {

		// 得到数据处理类
		IProcessData prossData = ProcessDataFactory
				.getProcessFactoryInstatnces().getProcessDataInstatnces(
						fileSuffix);

		// 字节缓存
		byte[] bytes = null;
		

		// 如果需要压缩
		if (isZip) {
			
			//得到静态代理类
			IProcessData proxyProcess = ProcessDataStaticProxy.getInstances(filePrefix, fileSuffix, prossData) ;
			// 压缩文件 并生成流
			bytes = proxyProcess.processByteCache(ftlName, dataSource);
			// 指定要载格式为zip
			fileSuffix = ExportConstants.FILE_ZIP;
		}else{
			//处理后的字节流
			bytes = prossData.processByteCache(ftlName, dataSource);
		}
		// 设置response响应方式
		WrapResponse.getInstances().setResponse(bytes, response, filePrefix,
				fileSuffix);

	}

}
