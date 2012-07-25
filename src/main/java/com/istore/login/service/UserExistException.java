package com.istore.login.service;

public class UserExistException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserExistException() {
		super("用户存在！");
	}
}
