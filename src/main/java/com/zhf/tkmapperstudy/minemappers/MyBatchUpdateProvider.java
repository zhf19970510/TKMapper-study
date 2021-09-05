package com.zhf.tkmapperstudy.minemappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.context.annotation.Bean;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author: 曾鸿发
 * @create: 2021-09-05 15:54
 **/
public class MyBatchUpdateProvider extends MapperTemplate {

    public MyBatchUpdateProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * <foreach collection="list" item="record" separator=";">
     *     UPDATE table_emp
     *     <set>
     *         emp_name=#{record.empName},
     *         emp_age=#{record.empAge},
     *         emp_salary=#{record.empSalary}
     *     </set>
     *     where emp_id=#{record.empId}
     * </foreach>
     * @param statement
     */
    public String batchUpdate(MappedStatement statement){
        StringBuilder builder = new StringBuilder();
        builder.append("<foreach collection=\"list\" item=\"record\" separator=\";\">");

        Class<?> entityClass = super.getEntityClass(statement);

        String tableName = super.tableName(entityClass);

        String updateClause = SqlHelper.updateTable(entityClass, tableName);

        builder.append(updateClause);

        builder.append("<set>");

        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);

        String idColumn = null;
        String idHolder = null;

        for (EntityColumn entityColumn : columns) {
            String column = entityColumn.getColumn();
            boolean isPrimaryKey = entityColumn.isId();
            if(isPrimaryKey){
                idColumn = column;
                idHolder = entityColumn.getColumnHolder("record");
            }else{
                String columnHolder = entityColumn.getColumnHolder("record");
                builder.append(column).append("=").append(columnHolder).append(",");
            }
        }

        builder.append("</set>");
        builder.append("where ").append(idColumn).append("=").append(idHolder);

        builder.append("</foreach>");

        return builder.toString();
    }
}
