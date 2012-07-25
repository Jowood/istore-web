package com.istore.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry; 

import com.istore.entity.Employee;
 

public class SystemEmployee {
	protected static Map<String, Employee> parameter = new LinkedHashMap<String, Employee>();
    public static void set(String key, Employee emp) {
        parameter.put(key, emp);  
    }

    public static Employee get(String key) {
        return parameter.get(key);
    }

	public static Map<String, ?> getList(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String,Object>();
		Iterator<Entry<String, Employee>> iterator = parameter.entrySet().iterator(); 
		List<Employee> list = new ArrayList<Employee>();
		while(iterator.hasNext()) {
			Entry<String, Employee> entry = iterator.next(); 
			Employee emp = entry.getValue(); 
			if (params.containsKey("name") && 
					emp.getName().indexOf(params.get("name")) < 0) {
				continue;
			} 
			list.add(emp);
		}
		result.put("list", list);
		result.put("count", list.size());
		return result;
	}

	public static void delete(String key) {
		parameter.remove(key);
		
	}
}
