package com.sun.job.executor;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 分片并发执行器（Node并发执行）
 */
public interface ShardingJobNodeConcurrentExecutor<G> {

    /**
     * node层级并发（实例数小于node数时才考虑）
     * @param context
     */
    default void execute(ShardingGroupPageJobContext<G> context) {
        List<String> dataNodes = context.getDataNodes();
        int shardingTotalReal = Math.min(dataNodes.size(), context.getShardingTotal());

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 6);
        try {
            List<CompletableFuture<?>> futures = Lists.newArrayList();
            for (String node : dataNodes) {
                if (dataNodes.indexOf(node) % shardingTotalReal == context.getShardingIndex()) {
                    futures.add(CompletableFuture.runAsync(() -> {
                        ShardingGroupPageJobContext<G> cloneContext = context.cloneContext();
                        cloneContext.setCurrentDataNode(node);
                        this.executeByNode(cloneContext);
                    }, executorService));
                }
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } finally {
            executorService.shutdown();
        }
    }

    /**
     * 按Node处理数据
     */
    void executeByNode(ShardingGroupPageJobContext<G> context);
}
