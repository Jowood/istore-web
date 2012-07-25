package com.istore.login.service;

public class CatalogExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CatalogExistException(){
		super("目录已经存在！");
	}
}
