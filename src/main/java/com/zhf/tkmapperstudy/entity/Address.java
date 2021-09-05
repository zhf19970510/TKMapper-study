package com.zhf.tkmapperstudy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author: 曾鸿发
 * @create: 2021-09-05 19:00
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    private String province;
    private String city;
    private String street;
}
