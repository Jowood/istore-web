package com.istore.ebook.controller;

public class ProjectExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ProjectExistException(){
		super("项目已经存在！");
	}

}
