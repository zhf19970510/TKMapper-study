package com.zhf.tkmapperstudy.dao;

import com.zhf.tkmapperstudy.TKMapperStudyApplication;
import com.zhf.tkmapperstudy.entity.Employee;
import com.zhf.tkmapperstudy.service.impl.EmployeeServiceImpl;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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
        Employee employeeCondition = new Employee(null, "bob", 5560.11, null, null);
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
        System.out.println(Integer.MAX_VALUE);
    }

    @Test
    public void testSelectByExample(){
        //1. 创建Example对象
        Example example = new Example(Employee.class);

        // i. 设置排序信息
        example.orderBy("empSalary").asc().orderBy("empAge").desc();

        // ii.设置“去重”
        example.setDistinct(true);

        ///iii. 设置select字段
        example.selectProperties("empName", "empSalary", "empAge");

        //2. 通过Example对象创建 Criteria对象
        Example.Criteria criteria01 = example.createCriteria();
        Example.Criteria criteria02 = example.createCriteria();

        //3. 在两个Criteria对象中分别设置查询条件
        //property参数：实体类的属性名
        // value参数：实体类的属性值
        criteria01.andGreaterThan("empSalary", 3000)
                .andLessThan("empAge", 25);
        criteria02.andLessThan("empSalary", 5000)
                .andGreaterThan("empAge", 30);

        //4. 使用OR 关键字组装两个Criteria 对象
        example.or(criteria02);

        //5.执行查询
        List<Employee> employeeList =employeeService.getEmpListByExample(example);

        employeeList.forEach(System.out::println);
    }

    @Test
    public void testSelectByRowBounds(){
        int pageNo = 3;
        int pageSize = 5;

        int index = (pageNo - 1) * pageSize;

        RowBounds rowBounds = new RowBounds(index, pageSize);

        List<Employee> employeeList = employeeService.getEmpListByBounds(rowBounds);

        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
    }
}