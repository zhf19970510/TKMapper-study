package com.zhf.tkmapperstudy.service.impl;

import com.zhf.tkmapperstudy.dao.EmpMapper;
import com.zhf.tkmapperstudy.dao.EmployeeMapper;
import com.zhf.tkmapperstudy.entity.Employee;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.swing.*;
import java.util.List;

/**
 * @author: 曾鸿发
 * @create: 2021-09-01 16:44
 **/
@Service("employeeService")
public class EmployeeServiceImpl {

    @Autowired
    private EmployeeMapper employeeMapper;

    // public void batchUpdateEmp(List<Employee> employeeList) {
    //     employeeMapper.batchUpdate(employeeList);
    // }

    public Employee getOne(Employee employeeCondition) {

        return employeeMapper.selectOne(employeeCondition);
    }

    public Employee selectByEmployeeId(Integer empId) {
        return employeeMapper.selectByPrimaryKey(empId);
    }

    public boolean isExists(Integer empId){
        return employeeMapper.existsWithPrimaryKey(empId);
    }

    public void saveEmployee(Employee employee) {
        employeeMapper.insert(employee);
    }

    public List<Employee> getEmpListByExample(Example example) {
        return employeeMapper.selectByExample(example);
    }

    public List<Employee> getEmpListByBounds(RowBounds rowBounds) {
        return employeeMapper.selectByRowBounds(new Employee(), rowBounds);
    }
}
