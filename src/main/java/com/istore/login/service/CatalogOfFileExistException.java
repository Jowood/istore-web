package com.istore.login.service;

public class CatalogOfFileExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4981160685134578353L;
	public CatalogOfFileExistException(){
		super("在该目录中文件已经存在了！");
	}
}
