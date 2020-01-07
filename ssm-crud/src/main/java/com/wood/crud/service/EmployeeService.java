package com.wood.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wood.crud.bean.Employee;
import com.wood.crud.bean.EmployeeExample;
import com.wood.crud.bean.EmployeeExample.Criteria;
import com.wood.crud.dao.EmployeeMapper;

@Service
public class EmployeeService {
	
	@Autowired
	EmployeeMapper employeeMapper;

	/**
	 * 查询所有员工
	 * @return
	 */
	public List<Employee> getAll() {
		
		return employeeMapper.selectByExampleWithDept(null);
	}
	/***
	 * 员工保存
	 * @param employee
	 */
	public void saveEmp(Employee employee) {
		
		employeeMapper.insertSelective(employee);
	}
	
	/**
	 * 检验用户名是否重复
	 * @param empName
	 * @return true :不重复 fasle：重复
	 */
	public boolean checkUser(String empName) {
		EmployeeExample example =new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmpNameEqualTo(empName);
		long count = employeeMapper.countByExample(example);
		return count == 0;
	}
	/**
	 * 按照员工id查询
	 * @param id
	 * @return
	 */
	public Employee getEmp(Integer id) {
		Employee employee = employeeMapper.selectByPrimaryKey(id);
		return employee;
	}
	/**
	 * 员工更新
	 * @param employee
	 */
	public void updateEmp(Employee employee) {
		employeeMapper.updateByPrimaryKeySelective(employee);
		
	}
	/**
	 * 员工删除
	 * @param id
	 */
	public void deleteEmp(Integer id) {
		employeeMapper.deleteByPrimaryKey(id);
		
	}
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deleteBatch(List<Integer> ids) {
		EmployeeExample example=new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmpIdIn(ids);
		employeeMapper.deleteByExample(example);
	}

}
