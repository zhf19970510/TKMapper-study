package com.zhf.tkmapperstudy.minemappers;

import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * @author: 曾鸿发
 * @create: 2021-09-05 15:59
 **/
public interface MyBatchUpdateMapper<T> {

    @UpdateProvider(type = MyBatchUpdateProvider.class, method = "dynamicSQL")
    void batchUpdate(List<T> list);
}
