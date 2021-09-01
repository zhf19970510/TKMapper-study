package com.zhf.tkmapperstudy.config;


import com.alibaba.fastjson.JSON;
import com.zhf.tkmapperstudy.help.exception.CustomException;
import com.zhf.tkmapperstudy.help.result.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public JsonResult doHandleCustomException(CustomException c) {
        return JSON.parseObject(c.getMessage(), JsonResult.class);
    }

}
