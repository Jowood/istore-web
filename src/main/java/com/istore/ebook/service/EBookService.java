package com.istore.ebook.service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import com.istore.entity.Catalog;
import com.istore.entity.StoreFile;
import com.istore.login.service.CatalogExistException;
import com.istore.login.service.CatalogNotExistException;

public interface EBookService {
	int PER_PG = 16;
	int RECORDS = 10;
	public List<Catalog> queryCatalog();
	public List<Catalog> queryCatalog(Map<String,String> params);
	public Map<String,String> queryCatalogCount();
	public List<StoreFile> queryEBook(String catalog);
	public void insertCatalog(Catalog catalog)throws DocumentException, IOException,CatalogExistException;
	public void updateCatalog(Catalog catalog)throws DocumentException, IOException,CatalogExistException,CatalogNotExistException;
	public void deleteCatalog(Catalog catalog)throws DocumentException, IOException,CatalogExistException;
	
	public List<Object> queryCatalogOfFile(Map<String, String> params);
	public void insertFile(StoreFile file, File saveFile)throws DocumentException, IOException,CatalogExistException, NoSuchAlgorithmException,Exception;
	public void delFile(StoreFile file)throws DocumentException, IOException,CatalogExistException, Exception;
	public Map<String,String> queryFileCount(Map<String, String> map);
	public void synchroCloud();
	public List<Catalog> queryAllCatalog(Map<String, String> map);
}
