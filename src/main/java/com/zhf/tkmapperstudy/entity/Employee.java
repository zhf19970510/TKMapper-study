package com.zhf.tkmapperstudy.entity;

import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "table_emp")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select emp_seq.nextval from dual")
    private Integer empId;
    private String empName;
    @Column(name = "emp_salary")
    private Double empSalary;
    private Integer empAge;
}
