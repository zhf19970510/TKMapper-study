package com.zhf.tkmapperstudy.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhf.tkmapperstudy.entity.Student;
import com.zhf.tkmapperstudy.help.enumeration.ErrorType;
import com.zhf.tkmapperstudy.help.result.JsonResult;
import com.zhf.tkmapperstudy.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (Student)表控制层
 *
 * @author HongFaZeng
 * @since 2021-08-30 13:14:33
 */
@Api(tags = "学生接口")
@Slf4j
@RestController
@RequestMapping("/student")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentController extends ApiController {
    /**
     * 服务对象
     */
    private final StudentService studentService;

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param student 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @GetMapping
    public R selectAll(Page<Student> page, Student student) {
        log.info("selectAll==>param:" + JSON.toJSONString(student));
        return success(this.studentService.page(page, new QueryWrapper<>(student)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        log.info("selectOne==>param:" + JSON.toJSONString(id));
        return success(this.studentService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param student 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping
    public R insert(@RequestBody Student student) {
        log.info("insert==>param:" + JSON.toJSONString(student));
        return success(this.studentService.save(student));
    }

    /**
     * 修改数据
     *
     * @param student 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PutMapping
    public R update(@RequestBody Student student) {
        log.info("update==>param:" + JSON.toJSONString(student));
        return success(this.studentService.updateById(student));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        log.info("delete==>param:" + JSON.toJSONString(idList));
        return success(this.studentService.removeByIds(idList));
    }

    /**
     * 根据学生id查询学生信息
     * @param id
     * @return
     */
    @ApiOperation("根据学生id查询学生信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "学生Id")})
    @GetMapping("/queryById")
    public JsonResult queryById(Integer id){
        log.info("queryById==>param:" + JSON.toJSONString(id));
        try {
            return studentService.queryById(id);
        } catch (Exception ex) {
            log.error("queryById==>error:" + ex.getMessage());
            return JsonResult.fail(ErrorType.SYSTEM_ERROR);
        }
    }

    /**
     * 查询指定行数据
     * @param offset
     * @param limit
     * @return
     */
    @ApiOperation("查询指定行数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "offset",value = "偏移量"), @ApiImplicitParam(name = "limit",value = "限制条数")})
    @GetMapping("/queryAllByLimit")
    public JsonResult queryAllByLimit(Integer offset, Integer limit){
        log.info("queryAllByLimit==>param:" + JSON.toJSONString(offset) + JSON.toJSONString(limit));
        try {
            return studentService.queryAllByLimit(offset, limit);
        } catch (Exception ex) {
            log.error("queryAllByLimit==>error:" + ex.getMessage());
            return JsonResult.fail(ErrorType.SYSTEM_ERROR);
        }
    }

    /**
     * 查询所有学生信息
     * @return
     */
    @ApiOperation("查询所有学生信息")
    @GetMapping("queryAll")
    public JsonResult queryAll(){
        log.info("queryAll==>param:");
        try {
            return studentService.queryAll();
        } catch (Exception ex) {
            log.error("queryAll==>error:" + ex.getMessage());
            return JsonResult.fail(ErrorType.SYSTEM_ERROR);
        }
    }
}
