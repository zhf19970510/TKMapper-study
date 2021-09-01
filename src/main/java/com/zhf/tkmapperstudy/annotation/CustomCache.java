package com.zhf.tkmapperstudy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomCache {

    /**
     * 缓存名称，不填使用自定义默认名称（目标方法类全路径:目标方法名(参数类型)）
     */
    String value() default "";

    /**
     * 是否删除缓存，默认为false，为true时须指定要删除的缓存名称
     */
    boolean delete() default false;

}
