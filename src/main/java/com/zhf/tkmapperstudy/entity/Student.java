package com.zhf.tkmapperstudy.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * (Student)表实体类
 *
 * @author HongFaZeng
 * @since 2021-08-30 13:14:31
 */
@SuppressWarnings("serial")
public class Student extends Model<Student> {

    private Integer sno;

    private String name;

    private Object birthday;

    private String department;

    private String sex;


    public Integer getSno() {
        return sno;
    }

    public void setSno(Integer sno) {
        this.sno = sno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getBirthday() {
        return birthday;
    }

    public void setBirthday(Object birthday) {
        this.birthday = birthday;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

}
