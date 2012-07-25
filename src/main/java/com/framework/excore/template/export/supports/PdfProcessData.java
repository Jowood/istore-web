package com.framework.excore.template.export.supports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import com.framework.excore.template.export.AbstractProcessValidation;
import com.framework.excore.template.export.ExportConstants;
import com.framework.excore.template.export.IProcessData;
import com.framework.excore.template.export.ProcessDataFactory; 

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-4-26
 * @Time: 上午11:29:54
 * @Description :生成pdf流（pdf）
 */
public class PdfProcessData extends AbstractProcessValidation implements
				IProcessData {

	/* description: fop工厂类 */
	private FopFactory fopFactory = FopFactory.newInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.athena.excore.template.export.IProcessData#processFileStream(java
	 * .lang.String, java.util.Map)
	 */
	public ByteArrayOutputStream processFileStream(String ftlName,
					Map<String, Object> dataSource) throws Exception {
		// 检查数据的合法性
		this.checkParam(ftlName, dataSource);

		// 输出流 ,要返回出去的
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 得到xml的流
		IProcessData process = ProcessDataFactory.getProcessFactoryInstatnces()
						.getProcessDataInstatnces(ExportConstants.FILE_XLS);
		// xml的缓存数组
		byte[] bt = process.processByteCache(ftlName, dataSource);

		/*** 将xml的缓存数组转换成fo流 ***/
		// 转换成输入流
		ByteArrayInputStream rederStream = new ByteArrayInputStream(bt);
		// 转换成fo流
		Source src = new StreamSource(rederStream);

		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

		try {
			// 得到当前的classpath
			URL url = Thread.currentThread().getContextClassLoader()
							.getResource("");
			// 路经
			String path = url.getPath();
			// 转义空格 等特殊字符
			path = URLDecoder.decode(path, ExportConstants.CODE_UTF8);
			fopFactory.setUserConfig(path + ExportConstants.FOP_CONFIG_PATH);

			
			
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent,
							out);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			Result res = new SAXResult(fop.getDefaultHandler());

			// 转换成pdf流
			transformer.transform(src, res);

		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		finally {

			// 关闭输输入流
			if (rederStream != null) {
				try {
					rederStream.close();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					throw new Exception(e.getMessage());
				}
			}
			// 关闭输输出流
			if (out != null) {
				try {
					out.close();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					throw new Exception(e.getMessage());
				}
			}

		}

		return out;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.athena.excore.template.export.IProcessData#processByteCache(java.
	 * lang.String, java.util.Map)
	 */
	public byte[] processByteCache(String ftlName,
					Map<String, Object> dataSource) throws Exception {

		// pdf字节缓存
		byte[] pdfBt = this.processFileStream(ftlName, dataSource)
						.toByteArray();

		return pdfBt;

	}

	// 将txt转换成pdf
	// public byte[] processByteCache(String ftlName,
	// Map<String, Object> dataSource) throws ServiceException {
	//
	// IProcessData process = ProcessDataFactory.getProcessFactoryInstatnces()
	// .getProcessDataInstatnces(ExportConstants.FILE_TXT);
	// byte[] bt = process.processByteCache(ftlName, dataSource);
	// //字节流
	// ByteArrayInputStream byteStream = new ByteArrayInputStream(bt);
	// //实例化一个reader流
	// InputStreamReader rederStream = new InputStreamReader(byteStream);
	// //实例化 BufferedReader
	// BufferedReader bufReader = new BufferedReader(rederStream);
	//
	// BaseFont bfChinese;
	// try {
	// //1.
	// bfChinese =
	// BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
	//
	// //2.利用系统字体解决中文造字问题
	// // bfChinese = BaseFont.createFont("C:/windows/fonts/simsun.ttc,1",
	// BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
	// }
	// catch (DocumentException e1) {
	// e1.printStackTrace();
	// throw new ServiceException(e1);
	// }
	// catch (IOException e1) {
	// throw new ServiceException(e1);
	// }
	//
	//
	// Font fontChinese = new Font(bfChinese,14, Font.NORMAL);
	// //字节流
	// byte[] pdfBt = null;
	// ByteArrayOutputStream bytes = null;
	// try {
	//
	// Document doc = new Document(PageSize.A4,36, 36, 36, 36);
	// bytes = new ByteArrayOutputStream() ;
	// PdfWriter writer = null;
	// try {
	// writer = PdfWriter.getInstance(doc,bytes) ;
	// }
	// catch (DocumentException e) {
	// throw new ServiceException(e);
	// }
	// doc.open();
	// // writer.open();
	// String str = bufReader.readLine();
	// while(str!=null){
	// try {
	// System.out.println(str) ;
	// Paragraph ph = new Paragraph(str,fontChinese);
	// doc.add(ph) ;
	// // doc.add(new Paragraph(new
	// String("试试中文!".getBytes("gbk"),"iso-8859-1"))) ;
	// //doc.add(new ListItem(new
	// String("试试中文!".getBytes("gbk"),"iso-8859-1")));
	// str = bufReader.readLine();
	// }
	// catch (DocumentException e) {
	// throw new ServiceException(e);
	// }
	// }
	// doc.close();
	// // writer.close();
	// // writer.getDirectContent().a
	// pdfBt = bytes.toByteArray();
	//
	// }
	// catch (IOException e) {
	// throw new ServiceException(e);
	// }
	// finally{
	//
	//
	// if(byteStream!=null){
	// try {
	// byteStream.close();
	// }
	// catch (IOException e) {
	// throw new ServiceException(e);
	// }
	// }
	// if(rederStream!=null){
	// try {
	// rederStream.close();
	// }
	// catch (IOException e) {
	// throw new ServiceException(e);
	// }
	// }
	// if(bufReader!=null){
	// try {
	// bufReader.close();
	// }
	// catch (IOException e) {
	// throw new ServiceException(e);
	// }
	// }
	//
	// if(bytes!=null){
	// try {
	// bytes.close();
	// }
	// catch (IOException e) {
	// throw new ServiceException(e);
	// }
	// }
	//
	//
	//
	//
	//
	// }
	//
	// return pdfBt;
	//
	// }

}
