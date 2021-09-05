package com.zhf.tkmapperstudy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "table_emp")
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select emp_seq.nextval from dual")
    private Integer empId;
    private String empName;
    @Column(name = "emp_salary")
    // @ColumnType(typeHandler = AddressTypeHandler.class)
    private Double empSalary;
    private Integer empAge;

    @Transient
    private String otherThings;     // 非数据库表字段
}
