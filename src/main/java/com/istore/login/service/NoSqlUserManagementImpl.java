package com.istore.login.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.framework.system.SystemParameter;
import com.framework.util.FileUtil;
import com.framework.util.StringUtil;
import com.framework.util.WebUtil;
import com.istore.entity.User;
import com.istore.system.FileExistException; 
import com.istore.system.SystemUser;

public class NoSqlUserManagementImpl implements UserManagementService {
	private static Logger logger = Logger.getLogger(NoSqlUserManagementImpl.class.getName());
	
	@SuppressWarnings("unchecked")
	public List<User> query(Map<String, String> params) {
		logger.info("用户管理--查询");
		Map<String, ?> result = SystemUser.query(params);
		int page = 1;
		if (params.containsKey(SystemParameter.PAGE_NUM)) {
			page = Integer.parseInt(params.get(SystemParameter.PAGE_NUM));
		}
		int count = Integer.parseInt(result.get("count").toString());
		List<User> list = new ArrayList<User>(); 
		List<User> records= (List<User>)result.get("list");
		if (count == 0) return list;
		for(int index = (page - 1) *  PAGE_RECORD; 
				index < records.size() && index < page * PAGE_RECORD; index++) {
			list.add(records.get(index));
		}
		params.put("page", String.valueOf(page));
		params.put("pages", String.valueOf(count/PAGE_RECORD + (count% PAGE_RECORD > 0 ? 1 : 0)));
		params.put("count", String.valueOf(count));
		params.put("pageNum", String.valueOf(PAGE_RECORD));
		return list;
	}

	public void insert(User user) throws DocumentException, IOException,UserExistException {
		logger.info("用户管理--插入");
		if (SystemUser.get(user.getLoginName()) == null) {
			Document doc = getUserDocument(); 
            Element root = doc.getRootElement();
            Element userEL = root.addElement("User");
            userEL.addAttribute("loginName", user.getLoginName());
            userEL.addElement("Name").addText(user.getName());
            userEL.addElement("LoginName").addText(user.getLoginName());
            userEL.addElement("Password").addText(user.getPassword());
            FileUtil.writeXML(doc, getUserFile());
            SystemUser.set(user.getLoginName(), user); 
		} else {
			throw new UserExistException();
		}

	}

	public void update(User user) throws DocumentException, IOException,UserExistException {
		logger.info("用户管理--修改");
		User old = SystemUser.get(user.getLoginName());
		if (old != null) { 
	        Document doc = getUserDocument();
	        List<?> list = doc.selectNodes("/Users/User[@loginName='"+ user.getLoginName() + "']" );
			Iterator<?> iter = list.iterator(); 
			while (iter.hasNext()) {
				Element el = (Element) iter.next();  
				if (!StringUtil.isEmpty(user.getLoginName())) {
					Attribute att = el.attribute("loginName");
					att.setValue(user.getLoginName());
					el.element("LoginName").setText(user.getLoginName());	
					old.setLoginName(user.getLoginName());
				}
				if (!StringUtil.isEmpty(user.getName())) {
					el.element("Name").setText(user.getName());	
					old.setName(user.getName());
				}
				if (!StringUtil.isEmpty(user.getPassword())) {
					el.element("Password").setText(user.getPassword());	
					old.setPassword(user.getPassword());
				}
			}
            FileUtil.writeXML(doc, getUserFile());
            SystemUser.set(user.getLoginName(),old); 
		} else {
			throw new UserExistException();
		}

	}

	public void delete(User user) throws DocumentException, IOException,UserExistException {
		logger.info("用户管理--删除");
		if (SystemUser.get(user.getLoginName()) != null) {
	        Document doc = getUserDocument();
	        List<?> list = doc.selectNodes("/Users/User[@loginName='"+ user.getLoginName() + "']" ); 
			Iterator<?> iter = list.iterator(); 
			Element root = doc.getRootElement();
			while (iter.hasNext()) {
				Element el = (Element) iter.next();
				root.remove(el);
			}
            FileUtil.writeXML(doc, getUserFile());
            SystemUser.delete(user);
		} else {
			throw new UserExistException();
		}

	} 
	
	public File getUserFile() {
		String userFile = WebUtil.getServletContext().getRealPath("/WEB-INF/config/user.xml");
		File file = new File(userFile);
        if (!file.exists()) {    //没有找到用户对应的配置文件　用户不存在
        	throw new FileExistException();
        }
        return file;
	}
	
	public Document getUserDocument() throws DocumentException { 
        SAXReader reader = new SAXReader(); 
        return reader.read(getUserFile());
	}


}
