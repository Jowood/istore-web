package com.framework.excore.template.export;

import java.io.IOException;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-4-23
 * @Time: 下午4:34:28
 * @Description :压缩文件
 */
public interface IZipFileTool {

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 下午4:52:01
	 * @param in
	 *            文件输入流
	 * @param fileName
	 *            压缩后的文件名
	 * @return OutputStream
	 * @throws IOException 
	 * @Description: 压缩文件
	 */
	public ByteArrayOutputStream zipFileByte(byte[] bt) throws IOException,Exception;
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 下午4:52:01
	 * @param in
	 *            文件输入流
	 * @param fileName
	 *            压缩后的文件名
	 * @return OutputStream
	 * @throws IOException 
	 * @Description: 压缩文件
	 */
	public ByteArrayOutputStream zipFileStream(InputStream in) throws IOException,Exception;

}
