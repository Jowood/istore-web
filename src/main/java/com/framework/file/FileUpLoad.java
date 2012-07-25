package com.framework.file;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件上传接口
 * @author 姜敏
 * @version 4.0
 */
public interface FileUpLoad {

  /**
   * 解析上传文件的请求对象
   *
   * @throws FileUploadException
   * @throws UnsupportedEncodingException
   */
  public void parseRequest() throws FileUploadException, UnsupportedEncodingException;


  /**
   * 解析上传文件的请求对象
   *
   * @param request
   * @throws FileUploadException
   * @throws UnsupportedEncodingException
   */
  public void parseRequest(HttpServletRequest request) throws FileUploadException, UnsupportedEncodingException;

  /**
   * 获得上传文件的集合
   *
   * @return List
   */
  public List<FileItem> getFileItems();

  /**
   * 获得指定索引的上传文件对象
   *
   * @param index
   * @return FileItem
   */
  public FileItem getFileItem(int index);

  /**
   * 获得上传文件的个数
   *
   * @return int
   */
  public int getFileItemsCount();

  /**
   * 获得文件上传请求中的上传参数
   *
   * @param name
   * @return String
   */
  public String getParameter(String name);

  /**
   * 获得文件上传请求中的上传参数数组
   *
   * @param name
   * @return String[]
   */
  public String[] getParameterValues(String name);

  /**
   * 获得文件上传请求中的输入框字段名的迭代器
   *
   * @return Iterator
   */
  public Iterator<String> getParameterNames();

  /**
   * 设置上传请求的请求字符集,用于请求参数的转码
   *
   * @param encoding
   */
  public void setURIEncoding(String encoding);

  /**
   * 设置文件上传占用的最大内存数
   *
   * @param maxMemorySize
   */
  public void setMaxMemorySize(int maxMemorySize);

  /**
   * 设置上传文件的最大尺寸
   *
   * @param maxRequestSize
   */
  public void setMaxRequestSize(int maxRequestSize);

  /**
   * 设置上传文件在服务器上的临时保存目录
   *
   * @param tempDirectory
   */
  public void setTempDirectory(String tempDirectory);

  /**
   * 设置上传文件在服务器上的最终保存目录
   *
   * @param destinationDirectory
   */
  public void setDestinationDirectory(String destinationDirectory);

  /**
   * 将上传文件写到服务器,指定文件名
   *
   * @param item
   * @param saveAsName
   * @throws Exception
   */
  public String write(FileItem item, String saveAsName) throws Exception;

  /**
   * 将上传文件写到服务器,按原文件名
   *
   * @param item
   * @throws Exception
   */
  public String write(FileItem item) throws Exception;


  /**
   * 将指定索引的上传文件写到服务器,指定文件名
   * @param index
   * @param saveAsName
   * @throws Exception
   */
  public String write(int index, String saveAsName) throws Exception;


  /**
   * 将指定索引的上传文件写到服务器, 按原文件名
   * @param index
   * @throws Exception
   */
  public String write(int index) throws Exception;

  /**
   * 获得文件扩展名
   *
   * @param item
   * @return String
   */
  public String getFileExt(FileItem item);

  /**
   * 获得指定索引的上传文件的后缀名
   * @param index
   * @return String
   */
  public String getFileExt(int index);


  /**
   * 获得指定索引的上传文件的相对名称(去除路径信息)
   * @param index
   * @return String
   */
  public String getFileRelative(int index);

  /**
   * 获得文件的相对名称(去除路径信息)
   *
   * @param item
   * @return String
   */
  public String getFileRelative(FileItem item);
  
  
}
