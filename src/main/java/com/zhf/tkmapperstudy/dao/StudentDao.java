package com.zhf.tkmapperstudy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhf.tkmapperstudy.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Student)表数据库访问层
 *
 * @author HongFaZeng
 * @since 2021-08-30 13:14:32
 */
public interface StudentDao extends BaseMapper<Student> {
    /**
     * 根据学生id查询学生信息
     * @param id
     * @return
     */
    Student queryById(Integer id);

    /**
     * 查询指定行数据
     * @param offset
     * @param limit
     * @return
     */
    List<Student> queryAllByLimit(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 查询所有学生信息
     * @return
     */
    List<Student> queryAll();

    int insertBatch(List<Student> entities);

    int insertOrUpdateBatch(List<Student> entities);


}
