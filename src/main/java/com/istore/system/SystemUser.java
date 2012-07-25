package com.istore.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.istore.entity.User;

public class SystemUser {
	
    private static final String USER_LOGIN_NAME = "selectLoginName";
	private static final String USER_NAME = "selectName";
	protected static Map<String, User> users = new Hashtable<String, User>();

    public static void set(String loginName, User value) {
    	users.put(loginName, value);
    }

    public static User get(String loginName) {
        return users.get(loginName);
    }
    
    public static boolean isIncludeUser(String name) {
    	return users.containsKey(name);
    }

	public static void delete(User user) {
		users.remove(user.getLoginName());
		
	}

	public static Map<String,?> query(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String,Object>();
		Iterator<Entry<String, User>> iterator = users.entrySet().iterator();
		List<User> list = new ArrayList<User>();
		while(iterator.hasNext()) {
			Entry<String, User> entry = iterator.next();
			String key = entry.getKey();
			User user = entry.getValue();
			if (params.containsKey(USER_LOGIN_NAME) && 
					key.indexOf(params.get(USER_LOGIN_NAME)) < 0) {
				continue;
			}
			if (params.containsKey(USER_NAME) 
					&& user.getName().indexOf(params.get(USER_NAME)) < 0) {
				continue;
			}
			list.add(user);
		}
		result.put("list", list);
		result.put("count", list.size());
		return result;
	}
}
