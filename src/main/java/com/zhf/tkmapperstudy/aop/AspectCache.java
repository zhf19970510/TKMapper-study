package com.zhf.tkmapperstudy.aop;

import com.alibaba.fastjson.JSON;
import com.zhf.tkmapperstudy.annotation.CustomCache;
import com.zhf.tkmapperstudy.help.result.JsonResult;
import com.zhf.tkmapperstudy.help.util.HelpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class AspectCache {

    private final static String cacheName = "com.zhf.tkmapperstudy.aop.AspectCache:";
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("@annotation(com.zhf.tkmapperstudy.annotation.CustomCache)")
    public void operationVerify() {
    }

    @Around("operationVerify()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        CustomCache customCache = AspectUtil.getAnnotationInfo(joinPoint, CustomCache.class);
        String cacheKey = customCache.value();
        if (HelpUtil.isEmpty(cacheKey)) {
            cacheKey = AspectUtil.getKey(joinPoint);
        } else {
            cacheKey = cacheName + cacheKey;
        }
        if (customCache.delete()) {
            redisTemplate.delete(cacheKey);
        } else {
            String info = redisTemplate.opsForValue().get(cacheKey);
            if (HelpUtil.isNotEmpty(info)) {
                return JSON.parseObject(info, JsonResult.class);
            }
        }
        Object ret = null;
        try {
            // 执行目标方法
            ret = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        if (!customCache.delete()) {
            redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(ret), 300, TimeUnit.SECONDS);
        }
        return ret;
    }
}
