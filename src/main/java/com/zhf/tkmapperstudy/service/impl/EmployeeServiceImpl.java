package com.zhf.tkmapperstudy.service.impl;

import com.zhf.tkmapperstudy.dao.EmployeeMapper;
import com.zhf.tkmapperstudy.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: 曾鸿发
 * @create: 2021-09-01 16:44
 **/
@Service("employeeService")
public class EmployeeServiceImpl {

    @Autowired
    private EmployeeMapper employeeMapper;

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
}
