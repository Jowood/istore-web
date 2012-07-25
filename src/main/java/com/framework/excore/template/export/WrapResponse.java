package com.framework.excore.template.export;


import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import com.framework.util.DateUtil; 

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-4-23
 * @Time: 下午2:24:16
 * @Description :设置response的响应方式
 */
public class WrapResponse {

	public static WrapResponse getInstances() {
		return new WrapResponse();
	};

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-26
	 * @Time: 下午17:46
	 * @param bytes 字节
	 * @param response  response对象
	 * @param filePrefix 文件名前缀
	 * @param fileSuffix 文件 名后缀
	 * @throws ServiceException 
	 */
	public void setResponse(byte[] bytes, HttpServletResponse response,
			String filePrefix, String fileSuffix) throws Exception {

		//文件 名
		String downLoad = "";
		if (filePrefix != null) {
			try {
				downLoad = URLEncoder.encode(filePrefix,
				 ExportConstants.CODE_UTF8);
			} catch (UnsupportedEncodingException e) {
				throw new Exception(e.getMessage());
			}
		}
		//设置文件名
		 StringBuffer buf = new StringBuffer();
		 buf.append(ExportConstants.ATTACHMENT);
		 buf.append("\"");
		 buf.append(downLoad);
		 buf.append("_");
		 buf.append(DateUtil.getDate());
		 buf.append(fileSuffix);
		 buf.append("\"");
		 
		response.setContentType(ExportConstants.CONTEXT_TYPE);
		response.setCharacterEncoding(ExportConstants.CODE_UTF8);
		response.setHeader(ExportConstants.CONTEXT_DISPOSITION, buf.toString());
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			output.write(bytes);
			output.flush();
		} catch (Exception ex) {
			if (response.isCommitted()) {
				throw new Exception("exception class:"
						+ ex.getCause().getClass().getName()
						+ "\nexception message:" + ex.getCause().getMessage());
			}
			throw new Exception(ex.getMessage());
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
			try {
				response.flushBuffer();
			} catch (IOException e) {
				throw new Exception(e.getMessage());
			}
		}

	}

}
