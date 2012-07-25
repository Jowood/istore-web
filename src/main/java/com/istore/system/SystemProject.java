package com.istore.system;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.istore.entity.Project;

public class SystemProject {
	//protected static Map<String, Project> parameter = new Hashtable<String, Project>();
	protected static Map<String, Project> parameter = new LinkedHashMap<String, Project>();
	protected static Map<String, String> snapshot = new Hashtable<String, String>();
    public static void set(String key, Project project) {
        parameter.put(key, project);   
        snapshot.put(project.getName(), key);
    }
    
    public static Project get(String key) {
        return parameter.get(key);
    }
    
    public static List<Project> getList() {
    	Iterator<Entry<String, Project>> it = parameter.entrySet().iterator();
    	List<Project> list = new ArrayList<Project>();
    	while(it.hasNext()) {
    		Entry<String, Project> entry = it.next();
    		list.add(entry.getValue());
    	}
    	return list;    	
    }

	public static String getKey(String name) {
		
		return snapshot.get(name);
	}

	public static void delete(Project project) {
		Project temp = parameter.remove(project.getKey());
		snapshot.remove(temp.getName());
	}

	public static boolean isExist(Project project) { 
		return snapshot.containsKey(project.getName());
	}
    
}
