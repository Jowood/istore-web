package com.istore.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.istore.entity.Catalog;
import com.istore.entity.StoreFile;

public class SystemCatalog {
    //protected static Map<String, Catalog> parameter = new Hashtable<String, Catalog>(); 
	protected static Map<String, Catalog> parameter = new LinkedHashMap<String, Catalog>(); 
    public static final String PROJECT_KEY = "selectProjectKey";
    public static final String CATALOG_KEY = "selectKey";
    public static final String CATALOG_NAME = "selectName";
    public static final String FILE_NAME = "selectName"; 
    public static void set(String key, Catalog catalog) {
        parameter.put(key, catalog);  
    }

    public static Catalog get(String key) {
        return parameter.get(key);
    }

    public static boolean isExist(Catalog catalog) {
    	boolean result = false;
    	if (StringUtils.isEmpty(catalog.getParent())) {
        	Iterator<Entry<String,Catalog>> it = parameter.entrySet().iterator();
        	while(it.hasNext()) {
        		Entry<String,Catalog> entry = it.next();
        		Catalog val = entry.getValue();
        		if (val.equals(catalog)){
        			result = true;
        			break;
        		}
        	}    		
    	} else {
    		Catalog parent = get(catalog.getParent());
    		result = parent.isExist(catalog);
    	}

    	return result;
    } 
    
	public static List<Catalog> getList() { 
		return (List<Catalog>)parameter.values();
	}
	
	public static int count() {
		return parameter.size();
	}

	public static Map<String, ?> getList(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String,Object>();
		Iterator<Entry<String, Catalog>> iterator = parameter.entrySet().iterator(); 
		List<Catalog> list = new ArrayList<Catalog>();
		while(iterator.hasNext()) {
			Entry<String, Catalog> entry = iterator.next();
			String key = entry.getKey();
			Catalog catalog = entry.getValue();
			if (StringUtils.isNotEmpty(catalog.getParent())) {
				continue;
			}
			if (params.containsKey(PROJECT_KEY) && 
					!catalog.getProjectKey().equals(params.get(PROJECT_KEY))) {
				continue;
			}			
			if (params.containsKey(CATALOG_KEY) && 
					key.indexOf(params.get(CATALOG_KEY)) < 0) {
				continue;
			}
			
			if (params.containsKey(CATALOG_NAME) 
					&& catalog.getName().indexOf(params.get(CATALOG_NAME)) < 0) {
				continue;
			}
			list.add(catalog);
		}
		result.put("list", list);
		result.put("count", list.size());
		return result;
	}

	public static void delete(Catalog catalog) {
        parameter.remove(catalog.getKey()); 
	}

	public static Map<String, ?> getCatalogOfFiles(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String,Object>();
		Catalog catalog = get(params.get("catalogKey"));
		List<Object> list = new ArrayList<Object>(); 
		for(Catalog children : catalog.getChildren()) { 
			if (params.containsKey(FILE_NAME) 
					&& children.getName().indexOf(params.get(FILE_NAME)) < 0) {
				continue;
			}
			list.add(children);			
		}		
		
		for(StoreFile aFlile : catalog.getList()) { 
			if (params.containsKey(FILE_NAME) 
					&& aFlile.getName().indexOf(params.get(FILE_NAME)) < 0) {
				continue;
			}
			list.add(aFlile);			
		}
		result.put("list", list);
		result.put("count", list.size());
		return result;
	}


}
