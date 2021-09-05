package com.zhf.tkmapperstudy.minemappers;

import tk.mybatis.mapper.common.base.select.SelectAllMapper;
import tk.mybatis.mapper.common.example.SelectByExampleMapper;

/**
 * @author: 曾鸿发
 * @create: 2021-09-05 16:04
 **/
public interface MyMapper<T> extends SelectAllMapper<T>, SelectByExampleMapper<T>,MyBatchUpdateMapper<T> {

}
