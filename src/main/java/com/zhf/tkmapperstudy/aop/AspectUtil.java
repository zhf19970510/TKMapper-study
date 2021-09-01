package com.zhf.tkmapperstudy.aop;

import com.zhf.tkmapperstudy.help.util.HelpUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.util.CastUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


public class AspectUtil {

    /**
     * 获取注解信息
     */
    public static <T extends Annotation> T getAnnotationInfo(JoinPoint joinPoint, Class<T> annotationClass) {
        T result = null;
        try {
            MethodSignature sig = CastUtils.cast(joinPoint.getSignature());
            Method method = joinPoint.getTarget().getClass().getMethod(sig.getName(), sig.getParameterTypes());
            result = method.getAnnotation(annotationClass);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String getKey(JoinPoint joinPoint) {
        MethodSignature sig = CastUtils.cast(joinPoint.getSignature());
        Class<?>[] parameterTypes = sig.getParameterTypes();
        StringBuilder builder = new StringBuilder();
        for (Class<?> var1 : parameterTypes) {
            String str = var1.toString();
            builder.append(str.substring(str.lastIndexOf(".") + 1)).append(".class,");
        }
        return joinPoint.getTarget().getClass().getName() + ":" + joinPoint.getSignature().getName()
                + "(" + HelpUtil.substringLastOne(builder.toString()) + ")";
    }

}
