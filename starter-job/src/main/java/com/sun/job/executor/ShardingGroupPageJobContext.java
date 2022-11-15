package com.sun.job.executor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Function;

/**
 * 分片分组分页任务上下文
 */
@Getter
@Builder(access = AccessLevel.PUBLIC)
public class ShardingGroupPageJobContext<G> {

    /**
     * 默认分页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 200;

    /**
     * 当前分页索引
     */
    private final int shardingIndex;

    /**
     * 所有分片数量
     */
    private final int shardingTotal;

    /**
     * 执行的所有数据阶段
     */
    @Getter(AccessLevel.PUBLIC)
    private final List<String> dataNodes;

    /**
     * 分页大小
     */
    private final int pageSize;

    /**
     * 参数
     */
    private final String param;

    /**
     * 执行的当前数据节点
     */
    @Setter(AccessLevel.PUBLIC)
    private String currentDataNode;

    /**
     * 当前分页偏移量
     */
    @Setter(AccessLevel.PUBLIC)
    private int offset;

    /**
     * 分组
     */
    @Setter(AccessLevel.PUBLIC)
    private G group;

    /**
     * 克隆分组
     */
    @Setter
    private Function<G, G> cloneFunction;

    /**
     * 构建ShardingGroupPageJobContext, 使用指定分页大小，适用于仅分组执行的场景
     */
    public static <G> ShardingGroupPageJobContext<G> create(String param, int pageSize) {
        return ShardingGroupPageJobContext.<G>builder().pageSize(pageSize).param(param).build();
    }

    /**
     * 构建ShardingGroupPageJobContext, 使用默认分页大小，适用于仅分组执行的场景
     */
    public static <G> ShardingGroupPageJobContext<G> create(String param) {
        return ShardingGroupPageJobContext.<G>builder().pageSize(DEFAULT_PAGE_SIZE).param(param).build();
    }

    /**
     * 构建ShardingGroupPageJobContext, 使用默认分页大小，适用于分片执行的场景
     */
    public static <G> ShardingGroupPageJobContext<G> create(String param, List<String> dataNodes, int shardingIndex, int shardingTotal) {
        return create(param, dataNodes, shardingIndex, shardingTotal, DEFAULT_PAGE_SIZE, null);
    }

    /**
     * 构建ShardingGroupPageJobContext, 使用指定分页大小，适用于分组执行的场景（并发执行）
     */
    public static <G> ShardingGroupPageJobContext<G> create(String param, List<String> dataNodes, int shardingIndex, int shardingTotal, Function<G, G> cloneFunction) {
        return create(param, dataNodes, shardingIndex, shardingTotal, DEFAULT_PAGE_SIZE, cloneFunction);
    }

    /**
     * 构建ShardingGroupPageJobContext, 使用指定分页大小，适用于分组执行的场景（并发执行）
     */
    public static <G> ShardingGroupPageJobContext<G> create(String param, List<String> dataNodes, int shardingIndex, int shardingTotal, int pageSize, Function<G, G> cloneFunction) {
        return ShardingGroupPageJobContext.<G>builder()
                .shardingIndex(shardingIndex)
                .shardingTotal(shardingTotal)
                .dataNodes(dataNodes)
                .pageSize(pageSize)
                .param(param)
                .cloneFunction(cloneFunction)
                .build();
    }

    /**
     * 构建ShardingGroupPageJobContext, 使用指定分页大小，适用于仅分组执行的场景（并发执行）
     */
    public static <G> ShardingGroupPageJobContext<G> create(String param, List<String> dataNodes, Function<G, G> cloneFunction) {
        return ShardingGroupPageJobContext.<G>builder()
                .dataNodes(dataNodes)
                .param(param)
                .cloneFunction(cloneFunction)
                .build();
    }

    /**
     * 克隆上下文
     */
    public ShardingGroupPageJobContext<G> cloneContext() {
        return ShardingGroupPageJobContext.<G>builder()
                .shardingIndex(this.shardingIndex)
                .shardingTotal(this.shardingTotal)
                .dataNodes(this.dataNodes)
                .pageSize(this.pageSize)
                .param(this.param)
                .currentDataNode(this.currentDataNode)
                .offset(this.offset)
                .group(this.cloneFunction.apply(this.group))
                .build();
    }
}