package com.sun.job.executor;

import java.util.List;
import java.util.concurrent.CompletionException;

/**
 * 分组任务执行器，按分组处理数据(不做分页处理)
 */
public interface ShardingGroupJobExecutor<G> {

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
                    try {
                        this.executeByGroup(context);
                    } catch (InterruptedException e) {
                        throw new CompletionException("任务终止! 异常日志序号：" + System.currentTimeMillis() + " 原因: " + e.getMessage(), e);
                    }
                });
    }

    /***
     * 分组执行, 调用方直接调用
     * 暂不支持group层级的并发（按实际要求复写该方法做并发即可）
     * 如果需要在组的层面并发执行策略，上下文属性的传递方式需要重新定义（可采用clone）,不然会有可见性的问题
     */
    default void executeByGroup(ShardingGroupPageJobContext<G> context) throws InterruptedException {
        List<G> groups = this.getGroups(context);
        for (G group : groups) {
            boolean interrupted = Thread.currentThread().isInterrupted();
            if (interrupted) {
                throw new InterruptedException();
            }

            context.setGroup(group);
            this.processData(context);
            this.postGroupProcessor(context);
        }
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
