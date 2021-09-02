package com.zhf.tkmapperstudy.dao;

import com.zhf.tkmapperstudy.TKMapperStudyApplication;
import com.zhf.tkmapperstudy.entity.Employee;
import com.zhf.tkmapperstudy.service.impl.EmployeeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: 曾鸿发
 * @create: 2021-09-01 21:42
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TKMapperStudyApplication.class)
public class EmployeeMapperTest {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Test
    public void testApplication(){
        System.out.println(employeeService);
    }

    @Test
    public void testSelectOne(){
        // 1. 创建封装查询条件的实体类对象
        Employee employeeCondition = new Employee(null, "bob", 5560.11, null);
        // 2. 执行查询
        Employee employeeQueryResult = employeeService.getOne(employeeCondition);
        // 3. 打印
        System.out.println(employeeQueryResult);
    }

    @Test
    public void testSelectByPrimaryKey(){
        // 1. 提供id值
        Integer empId = 3;

        // 2. 执行根据主键进行的查询
        Employee employee = employeeService.selectByEmployeeId(empId);

        // 3. 打印结果
        System.out.println(employee);
    }

    @Test
    public void testExistsWithPrimaryKey(){
        // 1. 提供主键值
        Integer empId = 3;
        // 2. 执行查询
        boolean exists = employeeService.isExists(empId);
        // .3 打印结果
        System.out.println(exists);
    }

    @Test
    public void testInsert(){
        // 1. 创建实体类对象封装要保存到数据库的数据
        Employee employee = new Employee();
        employee.setEmpName("emp03").setEmpSalary(1000.0).setEmpAge(23);

        // 2. 执行插入操作
        employeeService.saveEmployee(employee);

        // 3. 获取employee对象的主键字段值
        Integer empId = employee.getEmpId();
        System.out.println("empId:" + empId);
    }
}