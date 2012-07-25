package com.istore.ebook.controller;

public class ProjectNotExistException extends RuntimeException {
 
	private static final long serialVersionUID = 1L;
	public ProjectNotExistException(){
		super("项目不存在存在！");
	}

}
