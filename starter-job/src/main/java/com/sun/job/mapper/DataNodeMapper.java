package com.sun.job.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Mycat数据节点 Mapper
 */
@Mapper
public interface DataNodeMapper {

    /**
     * 获取数据节点
     */
    @Select("show datanodes where table_name = #{tableName}")
    List<String> getDataNodes(@Param("tableName") String tableName);
}
