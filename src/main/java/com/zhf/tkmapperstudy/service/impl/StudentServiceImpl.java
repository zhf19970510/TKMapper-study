package com.zhf.tkmapperstudy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhf.tkmapperstudy.dao.StudentDao;
import com.zhf.tkmapperstudy.entity.Student;
import com.zhf.tkmapperstudy.help.result.JsonResult;
import com.zhf.tkmapperstudy.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Student)表服务实现类
 *
 * @author HongFaZeng
 * @since 2021-08-30 13:14:33
 */
@Service("studentService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentServiceImpl extends ServiceImpl<StudentDao, Student> implements StudentService {

    private final StudentDao studentDao;

    @Override
    public JsonResult queryById(Integer id) {
        return JsonResult.success(studentDao.queryById(id));
    }

    @Override
    public JsonResult queryAllByLimit(Integer offset, Integer limit) {
        return JsonResult.success(studentDao.queryAllByLimit(offset, limit));
    }

    @Override
    public JsonResult queryAll() {
        return JsonResult.success(studentDao.queryAll());
    }

    @Override
    public JsonResult insertBatch(List<Student> entities) {
        return JsonResult.success(studentDao.insertBatch(entities));
    }

    @Override
    public JsonResult insertOrUpdateBatch(List<Student> entities) {
        return JsonResult.success(studentDao.insertOrUpdateBatch(entities));
    }
}
