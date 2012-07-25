package com.framework.excore.template.export;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-4-24
 * @Time: 上午11:04:37
 * @Description :常量
 */
public final class ExportConstants {

	/* description: 文本格式 */
	public static final String FILE_TXT = ".txt";
	/* description: excel格式 */
	public static final String FILE_XLS = ".xls";
	/* description: pdf格式 */
	public static final String FILE_PDF = ".pdf";
	/* description: zip压缩包 */
	public static final String FILE_ZIP = ".zip";

	/* description: 响应类型 */
//	public static final String CONTEXT_TYPE = "application/x-download";
	public static final String CONTEXT_TYPE = "application/octet-stream;charset=utf-8";
	/* description: CONTEXT_DISPOSITION */
	public static final String CONTEXT_DISPOSITION = "Content-disposition";
	/* description: 下载文件名 */
	public static final String ATTACHMENT = "attachment;filename=";
	/* description: 编码 */
	public static final String CODE_UTF8 = "utf-8";
	public static final String CODE_GBK = "GBK";
	
	/* description:  FOP配置文件 */
	public static final String FOP_CONFIG_PATH = "com/athena/excore/template/resources/fop.xml" ;
	

}
