package com.istore.login.service;

import com.istore.entity.LoginResult;
import com.istore.entity.User;

public interface UserLoginService {
	public LoginResult checkLogin(User user);
}
