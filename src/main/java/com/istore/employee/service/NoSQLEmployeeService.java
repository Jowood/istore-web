package com.istore.employee.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
 
import org.apache.log4j.Logger; 
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.framework.system.SystemParameter;
import com.framework.util.FileUtil;
import com.framework.util.StringUtil;
import com.framework.util.WebUtil;
import com.istore.ebook.service.NoSQLEBookService; 
import com.istore.entity.Employee;
import com.istore.system.FileExistException;  
import com.istore.system.SystemEmployee;

public class NoSQLEmployeeService implements EmployeeService {
	private static Logger logger = Logger.getLogger(NoSQLEBookService.class.getName());
	
	@Override
	public void batchInsert(List<Employee> insert) throws DocumentException, IOException {
		logger.info("批量插入雇员");
		for (Employee emp : insert) { 
			emp.setKey(StringUtil.getPrimaryKey());
			Document doc = getEmployeeDocument(); 
            Element root = doc.getRootElement();
            Element userEL = root.addElement("Employee");
            
            userEL.addAttribute("key", emp.getKey()); 
            userEL.addElement("name").addText(emp.getName());
            userEL.addElement("key").addText(emp.getKey()); 
            userEL.addElement("org").addText(emp.getOrg()); 
            userEL.addElement("start").addText(emp.getStart()); 
            userEL.addElement("end").addText(emp.getEnd());  
            userEL.addElement("visa").addText(emp.getVisa());  
            userEL.addElement("address").addText(emp.getAddress()); 
            logger.info("批量插入雇员--写入文件");
            FileUtil.writeXML(doc, getEmployeeFile());
            logger.info("批量插入雇员--更新内存");
			SystemEmployee.set(emp.getKey(), emp);
		} 	
	}
	
	private Document getEmployeeDocument() throws DocumentException { 
        SAXReader reader = new SAXReader(); 
        return reader.read(getEmployeeFile());
	}
	
	private File getEmployeeFile() {
		String userFile = WebUtil.getServletContext().getRealPath("/WEB-INF/config/employee.xml");
		File file = new File(userFile);
        if (!file.exists()) {    //没有找到用户对应的配置文件　用户不存在
        	throw new FileExistException();
        }
        return file;
	}

	@Override
	public void batchUpdate(List<Employee> update) throws DocumentException, IOException { 
		logger.info("批量更新雇员");
		for (Employee emp : update) {  
			Document doc = getEmployeeDocument(); 
	        List<?> list = doc.selectNodes("/Employees/Employee[@key='"+ emp.getKey() + "']" );
			Iterator<?> iter = list.iterator(); 
			while (iter.hasNext()) {
				Element el = (Element) iter.next();   
				el.element("name").setText(emp.getName()); 
				el.element("org").setText(emp.getOrg()); 
				el.element("start").setText(emp.getStart()); 
				el.element("end").setText(emp.getEnd());  
				el.element("visa").setText(emp.getVisa());  
				el.element("address").setText(emp.getAddress()); 	 
			} 
            logger.info("批量更新雇员--写入文件");
            FileUtil.writeXML(doc, getEmployeeFile());
            logger.info("批量更新雇员--更新内存");
			SystemEmployee.set(emp.getKey(), emp);
		}		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Employee> search(Map<String, String> params) {
		logger.info("查询雇员");
		Map<String, ?> result = SystemEmployee.getList(params);
		int page = 1;
		if (params.containsKey(SystemParameter.PAGE_NUM)) {
			page = Integer.parseInt(params.get(SystemParameter.PAGE_NUM));
		}
		int count = Integer.parseInt(result.get("count").toString());
		List<Employee> list = new ArrayList<Employee>(); 
		params.put(SystemParameter.PAGE_NUM, String.valueOf(page));
		params.put("pages", String.valueOf(count/RECORDS + (count%RECORDS > 0 ? 1 :0)));
		params.put("count", String.valueOf(count));
		params.put("pageNum", String.valueOf(RECORDS));
		List<Employee> records= (List<Employee>)result.get("list");
		if (count == 0) return list;
		for(int index = (page - 1) *  RECORDS; 
				index < records.size() && index < page * RECORDS; index++) {
			list.add(records.get(index));
		}

		return list; 
	}

	@Override
	public void delete(Map<String, String> params) throws DocumentException,
			IOException {
		logger.info("删除雇员");
        Document doc = getEmployeeDocument();
        List<?> list = doc.selectNodes("/Employees/Employee[@key='"+ params.get("key") + "']" );
		Iterator<?> iter = list.iterator(); 
		Element root = doc.getRootElement();
		while (iter.hasNext()) {
			Element el = (Element) iter.next();
			root.remove(el);
		} 
		logger.info("删除雇员--写入文件");
        FileUtil.writeXML(doc, getEmployeeFile()); 
        logger.info("删除雇员--更新内存");
        SystemEmployee.delete(params.get("key"));
		
	}

}
