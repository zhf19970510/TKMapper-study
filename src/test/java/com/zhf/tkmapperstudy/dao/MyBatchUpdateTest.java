package com.zhf.tkmapperstudy.dao;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.zhf.tkmapperstudy.TKMapperStudyApplication;
import com.zhf.tkmapperstudy.entity.Employee;
import com.zhf.tkmapperstudy.service.impl.EmployeeServiceImpl;
import com.zhf.tkmapperstudy.util.SpringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 曾鸿发
 * @create: 2021-09-05 17:49
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TKMapperStudyApplication.class)
public class MyBatchUpdateTest {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Test
    public  void mainTest() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(9, "newName01", 111.11, 10, null));
        employeeList.add(new Employee(10, "newName02", 222.22, 20, null));
        employeeList.add(new Employee(11, "newName03", 333.33, 30, null));
        // employeeService.batchUpdateEmp(employeeList);
    }
}
