package com.istore.entity;

import java.util.List;
import java.util.Vector;

import com.framework.mapping.SerializeCloneable;

public class Catalog extends SerializeCloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String key;
	private String parent;
	private String level;
	private String projectKey;
	private List<StoreFile> list;
	private List<Catalog> children;
	public Catalog() {
		list = new Vector<StoreFile>();
		children = new Vector<Catalog>();
	}   



	public List<Catalog> getChildren() {
		return children;
	}



	public void setChildren(List<Catalog> children) {
		this.children = children;
	}



	public String getParent() {
		return parent;
	}


	public void setParent(String parent) {
		this.parent = parent;
	}


	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
	}


	public List<StoreFile> getList() {
		return list;
	}

	public void setList(List<StoreFile> list) {
		this.list = list;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public void delete(StoreFile file) { 
		for(int index = 0; index < list.size(); index++) {
			StoreFile aFile = list.get(index);
			if (aFile.getKey().equals(file.getKey())) {
				list.remove(index);
				break;
			}
		}
	}

	public boolean isExistFile(StoreFile file) {
		boolean result = false;
		for (StoreFile aFile : list) {
			if (aFile.getName().equals(file.getName())) {
				result = true;
				break;
			}
		}
		return result;
	}

	public String getProjectKey() {
		return projectKey;
	}
	
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;		
	}
	 
	public boolean equals(Catalog catalog) { 
		return this.projectKey.equals(catalog.getProjectKey()) && this.name.equals(catalog.getName()); 
	}



	public void addChildren(Catalog catalog) {
		children.add(catalog); 
	}



	public boolean isExist(Catalog catalog) {
		boolean result = false;
		for(Catalog aChildren : children) {
			if (aChildren.getName().equals(catalog.getName())) {
				result = true;
				break;
			}
		}
		return result;		
	}



	public void deleteChildren(Catalog aCatalog) {
		for(int index = 0; index < children.size(); index++) {
			Catalog aChildren = children.get(index);
			if (aChildren.getKey().equals(aCatalog.getKey())) {
				children.remove(index);
				return;
			}
		}
	}
}
