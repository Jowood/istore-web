package com.istore.ebook.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.framework.system.SystemParameter;
import com.framework.util.FileUtil;
import com.framework.util.HmacGenerator;
import com.framework.util.MD5Util;
import com.framework.util.StringUtil;
import com.framework.util.WebUtil;
import com.istore.entity.Catalog;
import com.istore.entity.Project;
import com.istore.entity.StoreFile;
import com.istore.login.service.CatalogExistException;
import com.istore.login.service.CatalogNotExistException;
import com.istore.login.service.CatalogOfFileExistException;
import com.istore.system.FileExistException;
import com.istore.system.SystemCatalog;
import com.istore.system.SystemProject;
import com.snda.storage.service.CSService;
import com.snda.storage.service.model.CSObject;
import com.snda.storage.service.model.StorageObject;

public class NoSQLEBookService implements EBookService {
	private static Logger logger = Logger.getLogger(NoSQLEBookService.class.getName());
	
	public List<Catalog> queryCatalog() { 
		return SystemCatalog.getList();
	}

	public List<StoreFile> queryEBook(String catalogKey) { 
		logger.info("文档管理--查询目录");
		return SystemCatalog.get(catalogKey).getList();
	}

	public Map<String, String> queryCatalogCount() {
		logger.info("文档管理--查询目录页数");
		Map<String,String> result = new HashMap<String,String>(); 
		int count = SystemCatalog.count();
		result.put("total", String.valueOf(count/PER_PG + (count >= PER_PG ? count%PER_PG : 1)));
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Catalog> queryCatalog(Map<String, String> params) {
		logger.info("文档管理--后台查询目录");
		Map<String, ?> result = SystemCatalog.getList(params);
		int page = 1;
		if (params.containsKey(SystemParameter.PAGE_NUM)) {
			page = Integer.parseInt(params.get(SystemParameter.PAGE_NUM));
		}
		int count = Integer.parseInt(result.get("count").toString());
		List<Catalog> list = new ArrayList<Catalog>(); 
		List<Catalog> records= (List<Catalog>)result.get("list");
		if (count == 0) return list;
		for(int index = (page - 1) *  RECORDS; 
				index < records.size() && index < page * RECORDS; index++) {
			list.add(records.get(index));
		}
		params.put("page", String.valueOf(page));
		params.put("pages", String.valueOf(count/RECORDS + (count%RECORDS > 0 ? 1 : 0)));
		params.put("count", String.valueOf(count));
		params.put("pageNum", String.valueOf(RECORDS));
		return list;
	}

	public void insertCatalog(Catalog catalog) throws DocumentException, IOException,
			CatalogExistException { 
		logger.info("目录管理--插入");
		catalog.setKey(StringUtil.getPrimaryKey());
        if(StringUtils.isEmpty(catalog.getParent())) {
        	catalog.setLevel(catalog.getKey());
        	catalog.setParent("");
        } else {
        	Catalog parent = SystemCatalog.get(catalog.getParent());
        	catalog.setLevel(parent.getLevel() + "," + catalog.getKey());
        }
		if (!SystemCatalog.isExist(catalog)) {
			Document doc = getCatalogDocument(); 
            Element root = doc.getRootElement();
            Element userEL = root.addElement("Catalog");
            
            userEL.addAttribute("key", catalog.getKey());
            userEL.addAttribute("name", catalog.getName());
            userEL.addAttribute("project", catalog.getProjectKey());
            userEL.addAttribute("parent", catalog.getParent());
            userEL.addAttribute("level", catalog.getLevel());
            userEL.addElement("Name").addText(catalog.getName());
            userEL.addElement("Key").addText(catalog.getKey()); 
            userEL.addElement("Project").addText(catalog.getProjectKey()); 
            userEL.addElement("Parent").addText(catalog.getParent()); 
            userEL.addElement("level").addText(catalog.getLevel()); 
            FileUtil.writeXML(doc, getCatalogFile());
            if (StringUtils.isNotEmpty(catalog.getParent())) {
            	SystemCatalog.get(catalog.getParent()).addChildren(catalog);
            }
            SystemCatalog.set(catalog.getKey(), catalog); 
		} else {
			throw new CatalogExistException();
		}
	}  

	public void updateCatalog(Catalog catalog) throws DocumentException, IOException,
			CatalogExistException,CatalogNotExistException {
		logger.info("目录管理--修改");
		if (SystemCatalog.get(catalog.getKey()) != null) {
			if (SystemCatalog.isExist(catalog)){
				throw new CatalogExistException();
			}
			Document doc = getCatalogDocument(); 
	        List<?> list = doc.selectNodes("/Catalogs/Catalog[@key='"+ catalog.getKey() + "']" );
			Iterator<?> iter = list.iterator(); 
			while (iter.hasNext()) {
				Element el = (Element) iter.next();  
				if (!StringUtil.isEmpty(catalog.getName())) {
					Attribute att = el.attribute("name");
					att.setValue(catalog.getName());
					el.element("Name").setText(catalog.getName());					
				} 
			}
            FileUtil.writeXML(doc, getCatalogFile());
            SystemCatalog.get(catalog.getKey()).setName(catalog.getName()); 
		} else {
			throw new CatalogNotExistException();
		}
		
	}

	public void deleteCatalog(Catalog catalog) throws DocumentException, IOException,
			CatalogExistException {
		
		logger.info("目录管理--删除");
		Catalog aCatalog = SystemCatalog.get(catalog.getKey());
		if (aCatalog != null) {
			// 删除文件
			boolean success = true;
			List<Catalog> childrens = aCatalog.getChildren();
			for (int index = 0; index < childrens.size(); index++) {
				this.deleteCatalog(childrens.get(index));
			}
			List<StoreFile> files= aCatalog.getList();
			for(int index = 0; index < files.size(); index++) {
				StoreFile file = files.get(index);
				try { 
					delFile(file);
				} catch (Exception e) {
					logger.error("删除" + aCatalog.getName() + "目录的" + file.getName() + "文件失败" );
					success = false;
				}
			}
			
			if (!success) {
				throw new IOException();
			}

			
	        Document doc = getCatalogDocument();
	        List<?> list = doc.selectNodes("/Catalogs/Catalog[@key='"+ catalog.getKey() + "']" );
			Iterator<?> iter = list.iterator(); 
			Element root = doc.getRootElement();
			while (iter.hasNext()) {
				Element el = (Element) iter.next();
				root.remove(el);
			} 
            FileUtil.writeXML(doc, getCatalogFile());
            if (StringUtils.isNotEmpty(aCatalog.getParent())) {
            	SystemCatalog.get(aCatalog.getParent()).deleteChildren(aCatalog);
            }
            SystemCatalog.delete(catalog);
		} else {
			throw new CatalogExistException();
		}
	}
	
	
	public File getCatalogFile() {
		String userFile = WebUtil.getServletContext().getRealPath("/WEB-INF/config/catalog.xml");
		File file = new File(userFile);
        if (!file.exists()) {    //没有找到用户对应的配置文件　用户不存在
        	throw new FileExistException();
        }
        return file;
	}
	
	public Document getCatalogDocument() throws DocumentException { 
        SAXReader reader = new SAXReader(); 
        return reader.read(getCatalogFile());
	}

	public File getStoreFile() {
		String userFile = WebUtil.getServletContext().getRealPath("/WEB-INF/config/file.xml");
		File file = new File(userFile);
        if (!file.exists()) {    //没有找到用户对应的配置文件　用户不存在
        	throw new FileExistException();
        }
        return file;
	}
	
	public Document getStoreFileDocument() throws DocumentException { 
        SAXReader reader = new SAXReader(); 
        return reader.read(getStoreFile());
	}	
	
	@SuppressWarnings("unchecked")
	public List<Object> queryCatalogOfFile(Map<String, String> params) {
		logger.info("文档管理--后台目录文件查询");
		
		Map<String, ?> result = SystemCatalog.getCatalogOfFiles(params);
		int page = 1;
		if (params.containsKey(SystemParameter.PAGE_NUM)) {
			page = Integer.parseInt(params.get(SystemParameter.PAGE_NUM));
		}
		int count = Integer.parseInt(result.get("count").toString());
		List<Object> list = new ArrayList<Object>(); 
		List<Object> records= (List<Object>)result.get("list");
		Collections.reverse(records);
		if (count == 0) return list;
		for(int index = (page - 1) *  RECORDS; 
				index < records.size() && index < page * RECORDS; index++) {
			list.add(records.get(index));
		}
		params.put("page", String.valueOf(page));
		params.put("pages", String.valueOf(count/RECORDS + (count%RECORDS > 0 ? 1 : 0)));
		params.put("count", String.valueOf(count));
		params.put("pageNum", String.valueOf(RECORDS));
		return list;
	}

	public void insertFile(StoreFile file, File saveFile)throws DocumentException, IOException,CatalogExistException, NoSuchAlgorithmException,Exception { 
		logger.info("文件管理--插入");
		Catalog catalog = SystemCatalog.get(file.getCatalogKey());
		if (catalog == null) {
			throw new CatalogExistException();
		}
		if (catalog.isExistFile(file)) {
			throw new CatalogOfFileExistException();
		} 
		// 上传云存储
		logger.info("文件管理--开始上传文件到云存储");
		CSService service = SystemParameter.getGrandCloud();  
		StorageObject object = new CSObject(saveFile);
		service.putObject(SystemParameter.get("bucket"), object); 
		//System.out.println("文件大小: " + FileUtil.formetFileSize(saveFile.length()));
		file.setSize(FileUtil.formetFileSize(saveFile.length()));
		
  		//String savePath = WebUtil.getServletContext().getRealPath("/files");
		//putFile(file.getKey(), savePath + "/" + file.getKey() + file.getName().substring(file.getName().lastIndexOf(".")));
		logger.info("文件管理--结束上传文件到云存储");
		// 写入XML文件
		Document doc = getStoreFileDocument(); 
        Element root = doc.getRootElement();
        Element userEL = root.addElement("File"); 
        userEL.addAttribute("key", file.getKey());
        userEL.addAttribute("name", file.getName());
        userEL.addAttribute("catalogKey", file.getCatalogKey());
        userEL.addElement("Name").addText(file.getName());
        userEL.addElement("Key").addText(file.getKey()); 
        userEL.addElement("Size").addText(file.getSize()); 
        userEL.addElement("CatalogKey").addText(file.getCatalogKey());
        FileUtil.writeXML(doc, getStoreFile());
        logger.info("文件管理--修改XML文件");
        // 写入内存
        catalog.getList().add(file);
        logger.info("文件管理--写入内存"); 
		
		 
		
	}

	public void delFile(StoreFile file)throws DocumentException, IOException, CatalogExistException, Exception { 
		logger.info("文件管理--删除");
		Catalog catalog = SystemCatalog.get(file.getCatalogKey());
		if (catalog != null) {
			// 删除上传云存储
			/*logger.info("文件管理--开始删除云存储的文件");
			deleteCloudFile(file.getKey());
			logger.info("文件管理--删除云存储的文件结束");*/
	        Document doc = getStoreFileDocument();
	        List<?> list = doc.selectNodes("/Files/File[@key='"+ file.getKey() + "']" );
			Iterator<?> iter = list.iterator(); 
			Element root = doc.getRootElement();
			while (iter.hasNext()) {
				Element el = (Element) iter.next();
				root.remove(el);
			} 
            FileUtil.writeXML(doc, getStoreFile());
            logger.info("文件管理--修改XML文件");
            catalog.delete(file);
            logger.info("文件管理--写入内存");
		} else {
			throw new CatalogExistException();
		}		
	}	
	
	public void putFile(String putFileName, String localfile) throws Exception {
		String urlResouce = "/" + SystemParameter.get("bucket") + "/" + putFileName; // create URL
		File localFile = new File(localfile); 
		File localFileMD5 = new File(localfile);
		long putFileLength = localFile.length(); // get putfile's length
		String putFileMd5 = MD5Util.md5file(localFileMD5); // get putfile's md5
		putFileMd5 = putFileMd5.trim();
		String datestring = DateUtil.formatDate(new Date()); // create date
		String CanonicalizedSNDAHeaders = "x-snda-meta-md5checksum:"
		+ putFileMd5 + "\n"; // store your metas
		String StringToSign = "PUT" + "\n" // create StringToSign
		+ putFileMd5 + "\n" // Content-Md5 Filed
		+ "\n" // no Content-Type Filed
		+ datestring + "\n" // Date Filed
		+ CanonicalizedSNDAHeaders
		+ urlResouce;
		String Authorization = "SNDA" + " " + SystemParameter.get("accessKey") + ":"
		+ HmacGenerator.generate(SystemParameter.get("secretAccessKey"), StringToSign);
		String hostUrl = SystemParameter.get("baseURL") + urlResouce;
		HttpURLConnection urlConnection =
		(HttpURLConnection) (new URL(hostUrl)).openConnection();
		urlConnection.setDoInput(true);
		urlConnection.setDoOutput(true);
		urlConnection.setRequestMethod("PUT");
		urlConnection.setRequestProperty("Content-Length", String.valueOf(putFileLength));
		urlConnection.setRequestProperty("Content-MD5", putFileMd5);
		urlConnection.setRequestProperty("Authorization", Authorization);
		urlConnection.setRequestProperty("Date", datestring);
		urlConnection.setRequestProperty("x-snda-meta-md5checksum", putFileMd5);
		OutputStream urlOutputStream = urlConnection.getOutputStream();
		FileInputStream fileInputStream = new FileInputStream(localFile); 
		com.google.common.io.ByteStreams.copy(fileInputStream, urlOutputStream);
		fileInputStream.close();
		urlOutputStream.close(); 
		if (urlConnection.getResponseCode() != 204){
			logger.error("上传云存储失败！！");
			throw new IOException("上传云存储失败！！");
		}
	}
	
    public void deleteCloudFile(String putFileName) throws Exception {
	    String urlResouce = "/" + SystemParameter.get("bucket") + "/" + putFileName; // create URL
	    String datestring = DateUtil.formatDate(new Date()); // create Date
	    String CanonicalizedSNDAHeaders = "";
	    String StringToSign = "DELETE" + "\n" // create StringToSign
	    + "\n" // no Content-Md5 Filed
	    + "\n" // no Content-Type Filed
	    + datestring + "\n" // Date Filed
	    + CanonicalizedSNDAHeaders + urlResouce;
	    String Authorization = "SNDA" + " " + SystemParameter.get("accessKey") + ":"
	    + HmacGenerator.generate(SystemParameter.get("secretAccessKey"), StringToSign);
	    String hostUrl = SystemParameter.get("baseURL") + urlResouce;
	    HttpURLConnection urlConnection =
	    (HttpURLConnection) (new URL(hostUrl)).openConnection();
	    urlConnection.setRequestMethod("DELETE");
	    urlConnection.setRequestProperty("Authorization", Authorization);
	    urlConnection.setRequestProperty("Date", datestring); 
		if (urlConnection.getResponseCode() != 204){ 
			logger.error("删除云存储失败！！");
			throw new IOException("删除云存储失败！！");
		}
    }

	@Override
	public Map<String, String> queryFileCount(Map<String, String> map) {
		logger.info("文档管理--查询目录页数");
		Map<String,String> result = new HashMap<String,String>(); 
		Catalog catalog = SystemCatalog.get(map.get("catalogKey"));
		int count = catalog.getList().size();
		result.put("total", String.valueOf(count/PER_PG + (count >= PER_PG ? count%PER_PG : 1)));
		return result;
	}

	@Override
	public void synchroCloud() {
		logger.info("文档管理--同步云存储数据");
        String savePath = WebUtil.getServletContext().getRealPath("/files");
        //建目录
        File f1 = new File(savePath);
        if (!f1.exists()) {
            f1.mkdirs();
        }
        File[] files = f1.listFiles();
        for(int index = 0; index < files.length; index++) {
        	files[index].delete();
        }
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Catalog> queryAllCatalog(Map<String, String> map) { 
		List<Project> projects = SystemProject.getList();
		List<Catalog> result = new ArrayList<Catalog>();
		for(Project project : projects) {
			Catalog temp = new Catalog();
			temp.setName(project.getName()); 
			result.add(temp);
			map.put(SystemCatalog.PROJECT_KEY, project.getKey());
			List<Catalog> list = (List<Catalog>)SystemCatalog.getList(map).get("list");
			result.addAll(list);
		}
		return result;
	}  
	
}
