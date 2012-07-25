package com.istore.login.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import com.istore.entity.User;

public interface UserManagementService {
	public int PAGE_RECORD = 10;
	public List<User> query(Map<String, String> params); 
	public void insert(User user)throws DocumentException, IOException,UserExistException;
	public void update(User user)throws DocumentException, IOException,UserExistException;
	public void delete(User user)throws DocumentException, IOException,UserExistException;
}
