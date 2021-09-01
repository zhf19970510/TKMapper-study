package com.zhf.tkmapperstudy.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AspectVerify {

    @Pointcut("@annotation(com.zhf.tkmapperstudy.annotation.Permissions)")
    public void operationVerify() {
    }
}
