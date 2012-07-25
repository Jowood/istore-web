package com.istore.system;

public class FileExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public FileExistException() {
		super("文件不存在！");
	}
}
