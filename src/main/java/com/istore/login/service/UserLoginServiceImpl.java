package com.istore.login.service;

import com.framework.util.StringUtil;
import com.istore.entity.LoginResult;
import com.istore.entity.User;
import com.istore.system.SystemUser;

public class UserLoginServiceImpl implements UserLoginService{

	public LoginResult checkLogin(User user) { 
		LoginResult loginResult = new LoginResult();
		loginResult.setUser(user);
		if (StringUtil.isEmpty(user.getName())) {
			loginResult.setStatus(LoginResult.NON_LOGIN_USERNAME);
		} else if (StringUtil.isEmpty(user.getPassword())) {
			loginResult.setStatus(LoginResult.NON_LOGIN_PASSWORD);
		} else if (!SystemUser.isIncludeUser(user.getName())) {
			loginResult.setStatus(LoginResult.NON_USER);
		} else {
			User checkUser = SystemUser.get(user.getName());
			if (checkUser.getPassword().equals(user.getPassword())) {
				loginResult.setStatus(LoginResult.LOGIN_SUCCESS);
			} else {
				loginResult.setStatus(LoginResult.ERROR_PASSWORD);
			}
		}
		return loginResult;
	}

}
