package com.zhf.tkmapperstudy.config;

import com.zhf.tkmapperstudy.help.util.StaticUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseBodyAnalysis implements ResponseBodyAdvice<Object> {

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter arg1, MediaType arg2, Class arg3, ServerHttpRequest arg4, ServerHttpResponse arg5) {
        // 删除静态ThreadLocal存值，不然会造成内存泄露
        StaticUtil.permissions.remove();
        return body;
    }

    @Override
    public boolean supports(MethodParameter arg0, Class arg1) {
        return true;
    }

}