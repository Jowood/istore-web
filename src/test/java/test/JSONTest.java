package test;

import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

import com.framework.util.BeanUtil;
import com.istore.entity.Employee;

public class JSONTest {

	@Test
	public void test() {
		String json = "[{\"name\":\"张三\",\"org\":\"测试\"}]";
		List<Employee> list = BeanUtil.createBeanList(json, Employee.class);
		assertEquals(list.size(),1);
	}

}
