package com.sun.job.repository;

import com.sun.job.mapper.DataNodeMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mycat数据阶段 数据访问层
 */
@Repository
public class DataNodeRepository {

    @Resource
    private DataNodeMapper dataNodeMapper;

    /**
     * 查询数据节点
     */
    public List<String> getDataNodes(String tableName) {
        List<String> dataNodes = dataNodeMapper.getDataNodes(tableName);
        return dataNodes.stream().map(dataNode -> "/*!mycat: datanode=" + dataNode + "*/").collect(Collectors.toList());
    }
}
