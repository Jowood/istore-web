package com.istore.employee.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import com.istore.entity.Employee;

public interface EmployeeService {
	int RECORDS = 10;
	void batchInsert(List<Employee> insert) throws DocumentException, IOException;

	void batchUpdate(List<Employee> update) throws DocumentException, IOException;

	List<Employee> search(Map<String, String> params);

	void delete(Map<String, String> params) throws DocumentException, IOException;

}
