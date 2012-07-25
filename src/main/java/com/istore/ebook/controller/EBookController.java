package com.istore.ebook.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.web.servlet.ModelAndView;

import com.framework.controller.MultiController;
import com.framework.excore.template.DownLoadServices;
import com.framework.excore.template.export.ExportConstants;
import com.framework.file.FileDownLoad;
import com.framework.file.FileDownLoadImpl;
import com.framework.system.SystemParameter;
import com.framework.util.BeanUtil;
import com.framework.util.HmacGenerator;
import com.framework.util.StringUtil;
import com.istore.ebook.service.EBookService;
import com.istore.entity.Catalog;
import com.istore.entity.Project;
import com.istore.entity.StoreFile;
import com.istore.login.service.CatalogExistException;
import com.istore.login.service.CatalogNotExistException;
import com.istore.login.service.CatalogOfFileExistException;
import com.istore.system.SystemCatalog;
import com.istore.system.SystemProject;

public class EBookController extends MultiController {
	private EBookService ebookService;
	
	private DownLoadServices  downloadServices;	
	
	public DownLoadServices getDownloadServices() {
		return downloadServices;
	}

	public void setDownloadServices(DownLoadServices downloadServices) {
		this.downloadServices = downloadServices;
	}

	public EBookService getEbookService() {
		return ebookService;
	}

	public void setEbookService(EBookService ebookService) {
		this.ebookService = ebookService;
	}
	
	/**
     * 查询目录信息
     *
     * @param request  请求
     * @param response 响应
     * @return ModelAndView
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
	public ModelAndView queryCatalog(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    	if (!isLogin()) {
    		return new ModelAndView("login/userLogin.jsp"); 
    	}    	
    	Map<String,String> map = BeanUtil.createBean(request, HashMap.class); 
    	if (map.containsKey("act") && map.get("act").equals("init")) { 
    		map.put("count", String.valueOf(SystemProject.getList().size()));
    		return new ModelAndView("ebook/queryCatalogCount.jsp", map);
    	} else if (map.containsKey("act") && map.get("act").equals("list")) {
    		Map<String,Object> result = new HashMap<String,Object>(); 
    		map.put("page", map.get("pg"));
    		List<Catalog> list = ebookService.queryAllCatalog(map);
    		result.put("list", list);
            result.putAll(map);
    		return new ModelAndView("ebook/queryCatalog.jsp", result);
    	}
    	return null; 
    }
    
    @SuppressWarnings("unchecked")
    public ModelAndView queryFileData(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Map<String,String> map = BeanUtil.createBean(request, HashMap.class); 
    	if (map.containsKey("act") && map.get("act").equals("init")) { 
    		return new ModelAndView("ebook/queryFileCount.jsp",ebookService.queryFileCount(map));
    	} else if (map.containsKey("act") && map.get("act").equals("list")) {
    		Map<String,Object> result = new HashMap<String,Object>(); 
    		map.put("page", map.get("pg"));
    		List<Object> list = ebookService.queryCatalogOfFile(map);
    		result.put("list", list);
            result.putAll(map);
    		return new ModelAndView("ebook/queryFileData.jsp", result);
    	} 
    	return null; 
    }
    
/*	public ModelAndView queryFile(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    	if (!isLogin()) {
    		return new ModelAndView("login/userLogin.jsp"); 
    	}    	  
    	return  new ModelAndView(getView(request), BeanUtil.createBean(request, HashMap.class)); 
    }  */  

    @SuppressWarnings("unchecked")
	public ModelAndView queryFile(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    	if (!isLogin()) {
    		return new ModelAndView("login/userLogin.jsp"); 
    	}
		Map<String,String> params = BeanUtil.createBean(request, HashMap.class);
		Map<String,Object> result = new HashMap<String,Object>(); 
		if (params.containsKey("catalogKey")&&!StringUtil.isEmpty(params.get("catalogKey"))){
			Catalog catalog = SystemCatalog.get(params.get("catalogKey"));
			if(!StringUtil.isEmpty(catalog)) {
				Project project = SystemProject.get(catalog.getProjectKey());
				if (!StringUtil.isEmpty(project)) {
					result.put("projectName", project.getName());
				}
			}
		}
		List<Object> list = null; 
    	
        list = ebookService.queryCatalogOfFile(params);
/*        HttpSession session = WebUtil.getSession();
        session.setAttribute("catalogKey", params.get("catalogKey"));*/
        result.put("list", list);
        result.putAll(params);
    	return new ModelAndView(getView(request), result);    
    }
     
    @SuppressWarnings("unchecked")
	public ModelAndView exportCatalog(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    	if (!isLogin()) {
    		return new ModelAndView("login/userLogin.jsp"); 
    	}
		Map<String,String> params = BeanUtil.createBean(request, HashMap.class);
		if (!params.containsKey("selectProjectKey") || StringUtil.isEmpty(params.get("selectProjectKey"))) {
			return new ModelAndView(this.getRedirect("manageProject.ebook"));
		}
		List<Catalog> list = null; 
    	if (params.containsKey("parent") && StringUtils.isNotEmpty(params.get("parent"))) {
    		//list = ebookServic`
    	} else {
    		list = ebookService.queryCatalog(params);
    	} 
    	/*List result = new ArrayList();
    	for (Catalog clog : list) {
    		Map item = new HashMap();
    		item.put("name", clog.getName());
    		
    		result.add(item);
    	}*/
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("list", exportData(list));
    	downloadServices.downloadFile("catalog_file.ftl", data, response, "文档导出", ExportConstants.FILE_XLS, false) ;
    	return null;
    }
    
    public List<Map<String, ?>> exportData(List<Catalog> list) {
    	List<Map<String, ?>> result = new ArrayList<Map<String, ?>>(); 
    	for (Catalog item : list) {   
    			Map<String, Object> map = new HashMap<String, Object>();
    			map.put("name", item.getName());
    			map.put("file", item.getList()); 
    			map.put("catalog", exportData(item.getChildren()));
    			result.add(map); 
    	}
    	return result;
    }
    
    @SuppressWarnings("unchecked")
	public ModelAndView manageCatalog(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    	if (!isLogin()) {
    		return new ModelAndView("login/userLogin.jsp"); 
    	}
    	
		Map<String,String> params = BeanUtil.createBean(request, HashMap.class);
		if (!params.containsKey("selectProjectKey") || StringUtil.isEmpty(params.get("selectProjectKey"))) {
			return new ModelAndView(this.getRedirect("manageProject.ebook"));
		}
		List<?> list = null; 
    	Map<String,Object> result = new HashMap<String,Object>(); 
    	if (params.containsKey("parent") && StringUtils.isNotEmpty(params.get("parent"))) {
    		//list = ebookServic`
    	} else {
    		list = ebookService.queryCatalog(params);
    	} 
        result.put("list", list);
        result.putAll(params);
    	return new ModelAndView(getView(request), result);
    }    
    
    public ModelAndView createCatalog(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    	Catalog catalog = (Catalog) BeanUtil.createBean(request, Catalog.class);
    	Map<String,String> result = new HashMap<String,String>(); 
    	boolean success = true;
    	try {
    		ebookService.insertCatalog(catalog);
		} catch (CatalogExistException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "目录已经存在");
			success = false;
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "目录文件无法读取！");
			success = false;
		} catch (IOException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "目录文件无法写入！");
			success = false;
		}
    	result.put("success", String.valueOf(success));
    	return new ModelAndView(getView(request), result);
    }
    
    public ModelAndView updateCatalog(HttpServletRequest request, HttpServletResponse response) throws Exception{ 
    	Catalog catalog = (Catalog) BeanUtil.createBean(request, Catalog.class);
    	Map<String,String> result = new HashMap<String,String>(); 
    	boolean success = true;
    	try {
    		ebookService.updateCatalog(catalog);
		} catch (CatalogExistException e) {
			logger.error("目录已经存在");
			result.put("errorMsg", "目录已经存在");
			success = false;
		} catch (CatalogNotExistException e) {
			logger.error("目录不存在");
			result.put("errorMsg", "目录不存在");
			success = false;
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "目录文件无法读取！");
			success = false;
		} catch (IOException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "目录文件无法写入！");
			success = false;
		}
    	result.put("success", String.valueOf(success));
    	return new ModelAndView(getView(request), result);    	
    }    
    
    public ModelAndView deleteCatalog(HttpServletRequest request, HttpServletResponse response) throws Exception{ 
    	Catalog catalog = (Catalog) BeanUtil.createBean(request, Catalog.class);
    	Map<String,String> result = new HashMap<String,String>(); 
    	boolean success = true;
    	try {
    		ebookService.deleteCatalog(catalog);
		} catch (CatalogExistException e) {
			logger.error("目录不存在");
			result.put("errorMsg", "目录不存在");
			success = false;
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "目录文件无法读取！");
			success = false;
		} catch (IOException e) {
			logger.error(e.getMessage());
			result.put("errorMsg", "目录文件无法写入！");
			success = false;
		}
    	result.put("success", String.valueOf(success));
    	return new ModelAndView(getView(request), result);    	
    }
    
    @SuppressWarnings("unchecked")
	public ModelAndView viewCatalog(HttpServletRequest request, HttpServletResponse response) throws Exception{ 
    	if (!isLogin()) {
    		return new ModelAndView("login/userLogin.jsp"); 
    	}
		Map<String,String> params = BeanUtil.createBean(request, HashMap.class);
		List<Object> list = null; 
    	Map<String,Object> result = new HashMap<String,Object>(); 
        list = ebookService.queryCatalogOfFile(params);
/*      HttpSession session = WebUtil.getSession();
        session.setAttribute("catalogKey", params.get("catalogKey"));*/
        result.put("list", list);
        result.putAll(params);
    	return new ModelAndView(getView(request), result);    	
    }
    
    @SuppressWarnings("unchecked")
    public ModelAndView uploadHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String savePath = getServletContext().getRealPath("/files");
        //建目录
        File f1 = new File(savePath);
        if (!f1.exists()) {
            f1.mkdirs();
        } 
        
        DiskFileItemFactory fac = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(fac);
        upload.setHeaderEncoding("utf-8"); 
        Iterator<FileItem> it = upload.parseRequest(request).iterator();
        String name = ""; 
        String key = "";
        while (it.hasNext()) {
            FileItem item = it.next();
            if (!item.isFormField()) {
                name = item.getName();
                if (name == null || name.trim().equals("")) {
                    continue;
                }
                //数据操作
                logger.info("开始文件上传到本地");
                key = StringUtil.getPrimaryKey();

                File saveFile = new File(savePath + "/" + key);
                item.write(saveFile);
                logger.info("文件上传写入本地成功");

        
            }
        }
        response.getWriter().print(key);
        return null;
    }
    @SuppressWarnings("unchecked")
    public ModelAndView uploadFiles(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
		Map<String,String> params = BeanUtil.createBean(request, HashMap.class);
    	String savePath = getServletContext().getRealPath("/files");
    	String[] keys = params.get("keys").split(";");
    	String[] names = params.get("names").split(";");
    	String catalogKey = params.get("catalogKey");
    	Map<String,Object> result = new HashMap<String,Object>(); 
    	for (int index = 0; index < keys.length && index < names.length; index++) {
        	logger.info("开始文件上传到云存储");
        	File saveFile = new File(savePath + "/" + keys[index]);
        	if (saveFile.length() <= 0) { 
            	result.put("errorMsg", "文件大小为0KB，无法上传！");
            	break;        		
        	}
            StoreFile file = new StoreFile();
            file.setKey(keys[index]);
            file.setName(names[index]);
            file.setCatalogKey(catalogKey);
            try {
            	ebookService.insertFile(file, saveFile);
            } catch(CatalogExistException e) {
            	logger.error(e.getMessage());
            	result.put("errorMsg", "无效的目录名");
            	break;
            } catch(CatalogOfFileExistException e) {
            	logger.error(e.getMessage());
            	result.put("errorMsg", "在该目录中" + names[index]+ "已经存在了");
            	break;
            } catch(IOException e) {
            	logger.error(e.getMessage());
            	result.put("errorMsg", "上传文件失败！");
            	break;
            }
            saveFile.delete();
    	}
    	result.put("success", !result.containsKey("errorMsg")); 
    	
    	return new ModelAndView(getView(request), result);  
    }
    
    
    
    public ModelAndView downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if (!isLogin()) {
    		return new ModelAndView("login/userLogin.jsp"); 
    	}
    	StoreFile file = BeanUtil.createBean(request, StoreFile.class);
	    String urlResouce = "/" + SystemParameter.get("bucket") + "/" + file.getKey(); // create URL
	    
	    //File localFile = new File(localGetFileName);
	    
	    String datestring = DateUtil.formatDate(new Date()); // create Date
	    String CanonicalizedSNDAHeaders = "";
	    String StringToSign = "GET" + "\n" // create StringToSign
	    + "\n" // no Content-Md5 Filed
	    + "\n" // no Content-Type Filed
	    + datestring + "\n" // Date Filed
	    + CanonicalizedSNDAHeaders + urlResouce;
		String Authorization = "SNDA" + " " + SystemParameter.get("accessKey") + ":"
		+ HmacGenerator.generate(SystemParameter.get("secretAccessKey"), StringToSign);
		String hostUrl = SystemParameter.get("baseURL") + urlResouce;
		
	    HttpURLConnection urlConnection =
	    (HttpURLConnection) (new URL(hostUrl)).openConnection();
	    urlConnection.setDoInput(true);
	    urlConnection.setDoOutput(true);
	    urlConnection.setRequestMethod("GET");
	    urlConnection.setRequestProperty("Authorization", Authorization);
	    urlConnection.setRequestProperty("Date", datestring);
	    InputStream urlInputStream = null;  
	    logger.info("开始下载云存储文件"); 
	   
	    
	    try { 
	        String savePath = getServletContext().getRealPath("/download");
	        //建目录
	        File f1 = new File(savePath);
	        if (!f1.exists()) {
	            f1.mkdirs();
	        }	    	
            FileDownLoad down = new FileDownLoadImpl(response);
            urlInputStream = urlConnection.getInputStream();   
              
            down.write(urlInputStream, file.getName());
    	    urlInputStream.close();
             
            logger.info("结束下载云存储文件"); 
        }
        catch (Exception ex) {
            //if (response.isCommitted()) {
                logger.error(ex.getMessage());
            //}
            throw new IOException(ex.getMessage());
        }
        finally { 
            if (urlInputStream != null) {
                try{
                	urlInputStream.close();
                }
                catch (Exception e) {
                    logger.error(e.getMessage());
                }            	
            }           
            response.flushBuffer();
    		if (urlConnection.getResponseCode() != 200){
    			logger.error("下载云存储文件失败！！"); 
    		}
        } 
	    return null;
    }
    
    @SuppressWarnings("unchecked")
    public ModelAndView delLocalFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
		Map<String,String> params = BeanUtil.createBean(request, HashMap.class);
    	String savePath = getServletContext().getRealPath("/files");
    	File saveFile = new File(savePath + "/" + params.get("key") + params.get("sf_type"));
    	//ebookService.insertFile(file, saveFile);
        saveFile.delete();
        response.getWriter().print("1");
        return null;
    }
    
    public ModelAndView delFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	StoreFile file = BeanUtil.createBean(request, StoreFile.class);
        ebookService.delFile(file);
        response.getWriter().print("1");
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public ModelAndView viewChildren(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if (!isLogin()) {
    		return new ModelAndView("login/userLogin.jsp"); 
    	}
		
		Map<String,String> params = BeanUtil.createBean(request, HashMap.class);
		List<Object> list = null; 
    	Map<String,Object> result = new HashMap<String,Object>(); 
        list = ebookService.queryCatalogOfFile(params);
/*      HttpSession session = WebUtil.getSession();
        session.setAttribute("catalogKey", params.get("catalogKey"));*/
        result.put("list", list);
        result.putAll(params);
    	return new ModelAndView(getView(request), result);    	    	
    }
    
    @SuppressWarnings("unchecked")
    public ModelAndView viewFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if (!isLogin()) {
    		return new ModelAndView("login/userLogin.jsp"); 
    	}
		
		Map<String,String> params = BeanUtil.createBean(request, HashMap.class);
		Map<String,Object> result = new HashMap<String,Object>(); 
		if (params.containsKey("catalogKey")&&!StringUtil.isEmpty(params.get("catalogKey"))){
			Catalog catalog = SystemCatalog.get(params.get("catalogKey"));
			if(!StringUtil.isEmpty(catalog)) {
				Project project = SystemProject.get(catalog.getProjectKey());
				if (!StringUtil.isEmpty(project)) {
					result.put("projectName", project.getName());
				}
			}
		}
		List<Object> list = null; 
    	
        list = ebookService.queryCatalogOfFile(params);
/*        HttpSession session = WebUtil.getSession();
        session.setAttribute("catalogKey", params.get("catalogKey"));*/
        result.put("list", list);
        result.putAll(params);
    	return new ModelAndView(getView(request), result);        	
    }
}
