package com.wood.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wood.crud.bean.Employee;
import com.wood.crud.bean.Msg;
import com.wood.crud.service.EmployeeService;

/***
 * 处理员工CRUD请求
 * 
 * @author 71912
 *
 */
@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeService  employeeService;
	
	/***
	 * 单个、批量删除二合一。
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/emp/{ids}",method = RequestMethod.DELETE)
	public Msg deleteEmpById(@PathVariable("ids") String ids) {
		//批量删除
		if(ids.contains("-")) {
			List<Integer> del_ids=new ArrayList<Integer>();
			String[] str_ids=ids.split("-"); 
			//组装id的集合
			for (String string : str_ids) {
				del_ids.add(Integer.parseInt(string));
			}
			employeeService.deleteBatch(del_ids);
		}else {//单个删除
			Integer id=Integer.parseInt(ids);
		employeeService.deleteEmp(id);
		}
		return Msg.success();
	}
	
	/**
	 * 要能支持直接发送put。
	 * 配置HttpPutFormContentFilter； 
	 * @param employee
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/emp/{empId}",method = RequestMethod.PUT)
	public Msg saveEmp(Employee employee) {
		//System.out.println("employee:"+employee);
		employeeService.updateEmp(employee);
		return Msg.success();
		
	}
	
	/**
	 * 根据id查询employee
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/emp/{id}",method = RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id")Integer id) {
		
		Employee employee= employeeService.getEmp( id);
		return Msg.success().add("emp", employee);
		
	}
	
	/**
	 * 检查用户名是否重复
	 * @param empName
	 * @return
	 */
	@RequestMapping("/checkuser")
	@ResponseBody
	public Msg checkUser(@RequestParam("empName")String empName) {
		//先判断用户名是否是合法的表达式
		String regx="(^[a-z0-9_-]{3,16}$)|(^[\\u2E80-\\u9FFF]{2,5}$)";
		if(!empName.matches(regx)) {
			return Msg.fail().add("va_msg", "后端-用户名错误，应是2-5中文或6-16位英文和数字的组合！");
		}
		//数据库用户名重复校验
		boolean b= employeeService.checkUser(empName);
		if(b) {
			return Msg.success();
		}else {
		return Msg.fail().add("va_msg", "后端-用户名重复！");
		}
	}
	
	/**
	 * 员工保存
	 * 1.支持JSR303校验
	 * 2.导入Hibernate Validator
	 * 
	 */
	@RequestMapping(value = "/emp",method = RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee,BindingResult result) {
		if(result.hasErrors()) {
			//校验失败，应该返回失败，在模态框中显示校验失败的信息
			Map<String,Object> map =new HashMap<String, Object>();
			
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				System.out.println("错误的字段名："+fieldError.getField());
				System.out.println("错误的信息："+fieldError.getDefaultMessage());
			map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields", map);
			
		}else {
			employeeService.saveEmp(employee);
			return Msg.success();
		}
		
		
	}
	
	/**
	 * ResponseBody -->需要导入jackson
	 * @param pn
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(
			@RequestParam(value = "pn",defaultValue = "1") Integer pn) {
		
		//引入pagehelper分页插件
				//在查询之前只需要调用，传入页码，和每页的大小
				PageHelper.startPage(pn, 6);
				//startPage后面紧跟的这个查询就是一个分页查询。
				PageHelper.orderBy(" emp_id asc");
				List<Employee> emps= employeeService.getAll();
				//使用pageinfo包装查询后的结果，只需要将pageinfo交个页面
				//封装了详细的分页信息，包括有我们查询出来的数据，插入连续显示的页数
				PageInfo  page=new PageInfo(emps,6);
				return Msg.success().add("pageInfo", page);
	}
	
	/**
	 * 查询员工数据 分页 
	 * @return
	 */
	//@RequestMapping("/emps")
	public String getEmps(@RequestParam(value = "pn",defaultValue = "1") Integer pn,
			Model model) {
		
		//引入pagehelper分页插件
		//在查询之前只需要调用，传入页码，和每页的大小
		PageHelper.startPage(pn, 6);
		//startPage后面紧跟的这个查询就是一个分页查询。
		List<Employee> emps= employeeService.getAll();
		//使用pageinfo包装查询后的结果，只需要将pageinfo交个页面
		//封装了详细的分页信息，包括有我们查询出来的数据，插入连续显示的页数
		PageInfo  page=new PageInfo(emps,6);
		model.addAttribute("pageInfo",page);
		return "emplist";
	}




}
