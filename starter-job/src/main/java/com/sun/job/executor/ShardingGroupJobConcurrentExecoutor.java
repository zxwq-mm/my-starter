package com.sun.job.executor;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface ShardingGroupJobConcurrentExecoutor<G> {

    /**
     * 分片执行（默认）, 调用方直接调用
     * 暂不支持node层级的并发（分片后本身并发取决于运行的实例数量）
     * 当实例数小于node数时才考虑在单个实例上继续并发执行
     */
    default void execute(ShardingGroupPageJobContext<G> context) {
        List<String> dataNodes = context.getDataNodes();
        int shardingTotalReal = Math.min(dataNodes.size(), context.getShardingTotal());
        dataNodes.stream()
                .filter(node -> dataNodes.indexOf(node) % shardingTotalReal == context.getShardingIndex())
                .forEach(node -> {
                    context.setCurrentDataNode(node);
                    this.executeByGroup(context);
                });
    }

    /**
     * node前置处理
     */
    default void preNodeProcessor(ShardingGroupPageJobContext<G> context) {
        // node前置处理  空方法
    }

    /***
     * 分组执行, 调用方直接调用
     * 支持group层级的并发（按实际要求复写该方法做并发即可）
     * 如果需要在组的层面并发执行策略，上下文属性的传递方式需要重新定义（可采用clone）,不然会有可见性的问题
     */
    default void executeByGroup(ShardingGroupPageJobContext<G> context) {
        List<G> groups = this.getGroups(context);
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 6);
        List<CompletableFuture<?>> futures = Lists.newArrayList();

        for (G group : groups) {
            futures.add(CompletableFuture.runAsync(() -> {
                context.setGroup(group);
                ShardingGroupPageJobContext<G> cloneContext = context.cloneContext();
                this.processData(cloneContext);
                this.postGroupProcessor(cloneContext);
            }, executorService));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    /**
     * 0.获取所有分组
     */
    List<G> getGroups(ShardingGroupPageJobContext<G> context);

    /**
     * 1.处理数据
     */
    void processData(ShardingGroupPageJobContext<G> context);

    /**
     * group后置处理
     */
    default void postGroupProcessor(ShardingGroupPageJobContext<G> context) {

    }

}
