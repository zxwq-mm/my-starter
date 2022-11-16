package com.sun.job.service;

import com.sun.job.executor.ShardingGroupPageJobContext;
import com.sun.job.repository.DataNodeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Function;

/**
 * 分片任务上下文 服务
 */
@Service
public class ShardingJobContextService {

    @Resource
    private DataNodeRepository dataNodeRepository;

    /**
     * 创建分片任务上下文
     */
    public <G> ShardingGroupPageJobContext<G> create(String param, int shardingIndex, int shardingTotal, String tableName) {
        List<String> dataNodes = dataNodeRepository.getDataNodes(tableName);
        return ShardingGroupPageJobContext.create(param, dataNodes, shardingIndex, shardingTotal);
    }

    /**
     * 创建分片任务上下文
     */
    public <G> ShardingGroupPageJobContext<G> create(String param, int shardingIndex, int shardingTotal, String tableName, Function<G, G> cloneFunction) {
        List<String> dataNodes = dataNodeRepository.getDataNodes(tableName);
        return ShardingGroupPageJobContext.create(param, dataNodes, shardingIndex, shardingTotal, cloneFunction);
    }

    /**
     * 创建分片任务上下文
     */
    public <G> ShardingGroupPageJobContext<G> create(String param, int shardingIndex, int shardingTotal, String tableName, int pageSize, Function<G, G> cloneFunction) {
        List<String> dataNodes = dataNodeRepository.getDataNodes(tableName);
        return ShardingGroupPageJobContext.create(param, dataNodes, shardingIndex, shardingTotal, pageSize, cloneFunction);
    }

    /**
     * 创建分片任务上下文
     */
    public <G> ShardingGroupPageJobContext<G> create(String param, String tableName, Function<G, G> cloneFunction) {
        List<String> dataNodes = dataNodeRepository.getDataNodes(tableName);
        return ShardingGroupPageJobContext.create(param, dataNodes, cloneFunction);
    }

}
