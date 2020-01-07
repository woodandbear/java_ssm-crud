package com.wood.crud.test;

import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wood.crud.bean.Department;
import com.wood.crud.bean.Employee;
import com.wood.crud.dao.DepartmentMapper;
import com.wood.crud.dao.EmployeeMapper;

/**
 * 测试dao层
 * @author 71912
 * 推荐spring的项目使用spring的单元测试 ，可以自动注入我们需要的组件
 *1.导入SpringTest模块
 *2.@ContextConfiguration指定Spring位置
 *3.直接autowired要使用的组件即可
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration(value = "classpath:applicationContext.xml")
public class MapperTest {
	
	@Autowired
	DepartmentMapper departmentMapper;
	@Autowired
	EmployeeMapper   em;
	
	@Autowired
	SqlSession sqlSession;

	@Test
	public void testCRUD() {
		//1.创建spingIOC容器
		
		/*  ApplicationContext ioc= new
		  ClassPathXmlApplicationContext("applicationContext.xml");
		  DepartmentMapper departmentMapper= ioc.getBean(DepartmentMapper.class);
		*/
		System.out.println(departmentMapper);
		//1.插入
//		departmentMapper.insertSelective(new Department(null,"开发部"));
//		departmentMapper.insertSelective(new Department(null,"测试部"));
		//2.员工插入
		//em.insertSelective(new Employee(null,"kong","m","jerry@wood.com",1));
		//3.批量插入
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
		
		for(int i=0;i<1000;i++) {
			String uid= UUID.randomUUID().toString().substring(0, 5)+i;
			mapper.insertSelective(new Employee(null,uid,"m",uid+"@wood.com",1));
		}
		System.out.println("批量完成");
		
	}
	
}
