package com.zhf.tkmapperstudy.mappers;

import com.zhf.tkmapperstudy.entity.Address;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: 曾鸿发
 * @create: 2021-09-05 18:58
 **/
public class AddressTypeHandler extends BaseTypeHandler<Address> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Address address, JdbcType jdbcType) throws SQLException {
        //1. 对address对象进行验证
        if(null == address){
            return;
        }
        //2. 从address对象中取出具体数据
        String province = address.getProvince();
        String city = address.getCity();
        String street = address.getStreet();

        //3. 拼装成一个字符串
        // 规则：各个值之间使用","分开
        StringBuilder builder = new StringBuilder();
        builder.append(province).append(",").append(city).append(",").append(street);
        //4. 设置参数
        preparedStatement.setString(i, builder.toString());
    }

    @Override
    public Address getNullableResult(ResultSet rs, String columnName) throws SQLException {

        // 1. 根据字段名从rs对象中获取字段值
        String columnValue = rs.getString(columnName);

        // 2. 验证columnValue是否生效
        if (null != columnValue && columnValue.length() != 0 && columnValue.contains(",")) {
            return null;
        }

        //3. 根据","对columnValue进行拆分
        String[] split = columnValue.split(",");
        //4. 从拆分结果数组中获取Address需要的具体数据
        String province = split[0];
        String city = split[1];
        String street = split[2];
        return new Address(province, city, street);
    }

    @Override
    public Address getNullableResult(ResultSet rs, int i) throws SQLException {

        // 1. 根据字段名从rs对象中获取字段值
        String columnValue = rs.getString(i);

        // 2. 验证columnValue是否生效
        if (null != columnValue && columnValue.length() != 0 && columnValue.contains(",")) {
            return null;
        }

        //3. 根据","对columnValue进行拆分
        String[] split = columnValue.split(",");
        //4. 从拆分结果数组中获取Address需要的具体数据
        String province = split[0];
        String city = split[1];
        String street = split[2];
        return new Address(province, city, street);
    }

    @Override
    public Address getNullableResult(CallableStatement callableStatement, int i) throws SQLException {

        // 1. 根据字段名从rs对象中获取字段值
        String columnValue = callableStatement.getString(i);

        // 2. 验证columnValue是否生效
        if (null != columnValue && columnValue.length() != 0 && columnValue.contains(",")) {
            return null;
        }

        //3. 根据","对columnValue进行拆分
        String[] split = columnValue.split(",");
        //4. 从拆分结果数组中获取Address需要的具体数据
        String province = split[0];
        String city = split[1];
        String street = split[2];
        return new Address(province, city, street);
    }
}
