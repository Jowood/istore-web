package com.istore.login.service;

public class CatalogNotExistException extends RuntimeException { 
	private static final long serialVersionUID = 1L;
	public CatalogNotExistException(){
		super("目录不存在！");
	}

}
