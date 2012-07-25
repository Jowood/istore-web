package com.istore.entity;
 

import com.framework.mapping.SerializeCloneable;

public class LoginResult extends SerializeCloneable {
	public final static int NON_LOGIN_USERNAME = -1;
	public final static int NON_LOGIN_PASSWORD = -2;
	public final static int NON_USER = -3;
	public final static int ERROR_PASSWORD = -4;
	public final static int LOGIN_SUCCESS = 1;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int status;//登录状态
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getStatus() { 
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
