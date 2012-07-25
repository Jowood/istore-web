package com.framework.excore.template.export.supports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.framework.excore.template.export.ExportConstants;
import com.framework.excore.template.export.IZipFileTool;
import com.framework.util.DateUtil;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-4-23
 * @Time: 下午4:50:28
 * @Description :压缩文件
 */
public class DefaultZipFilTool implements IZipFileTool {

	/* description: 字节输出流 */
	private ByteArrayOutputStream out = null;
	/* description: zip流 */
	private ZipOutputStream zip = null;
	// 压缩后的文件名
	private String fileName = null;

	public DefaultZipFilTool(String _fileName, String ext)
			throws UnsupportedEncodingException {
		// 缓存流
		this.out = new ByteArrayOutputStream();
		// zip流
		this.zip = new ZipOutputStream(this.out);
		// 设置编码，防止出现乱码
		this.zip.setEncoding(ExportConstants.CODE_GBK);
		this.zip.setFallbackToUTF8(false);
		// 设置压缩的文件名称
		this.fileName = _fileName + "_" + DateUtil.getDate() + ext;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.athena.util.export.IZipFileTool#zipFileStream(java.io.InputStream,
	 * java.lang.String)
	 */
	public ByteArrayOutputStream zipFileByte(byte[] bt) throws IOException,Exception {
		// 将其转换成输入流
		InputStream in = new ByteArrayInputStream(bt);
		// 压缩文件
		this.zipFileStream(in);
		return this.out;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.athena.util.export.IZipFileTool#zipFileStream(java.io.InputStream,
	 * java.lang.String)
	 */
	public ByteArrayOutputStream zipFileStream(InputStream in)
			throws IOException,Exception {
		try {
			// 压缩实体
			ZipEntry entryFile = new ZipEntry(fileName);
			//
			zip.putNextEntry(entryFile);
			// 压缩文件
			int length = 0;
			byte[] bt = new byte[1024];
			while ((length = in.read(bt)) != -1) {
				zip.write(bt, 0, length);
			}
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		} finally {

			// 关闭流
			try {
				if (zip != null) {
					zip.closeEntry();
				}
			} catch (IOException e1) {
				throw new Exception(e1.getMessage());
			}
			try {
				if (zip != null) {
					zip.close();
				}
			} catch (IOException e1) {
				throw new Exception(e1.getMessage());
			}

			// 关闭流
			if (in != null) {
				try {
					in.close();
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
		return out;
	}
}
