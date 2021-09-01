package com.zhf.tkmapperstudy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhf.tkmapperstudy.entity.Student;
import com.zhf.tkmapperstudy.help.result.JsonResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Student)表服务接口
 *
 * @author HongFaZeng
 * @since 2021-08-30 13:14:33
 */
public interface StudentService extends IService<Student> {
    /**
     * 根据学生id查询学生信息
     * @param id
     * @return
     */
    JsonResult queryById(Integer id);

    /**
     * 查询指定行数据
     * @param offset
     * @param limit
     * @return
     */
    JsonResult queryAllByLimit(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 查询所有学生信息
     * @return
     */
    JsonResult queryAll();

    JsonResult insertBatch(List<Student> entities);

    JsonResult insertOrUpdateBatch(List<Student> entities);


}
