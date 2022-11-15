package com.sun.job.executor;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * 分组分页任务执行器，先分组，然后分页处理数据
 */
public interface ShardingGroupPageJobExecutor<G, T> {

    /**
     * 分片执行（默认）, 调用方直接调用
     * 暂不支持node层级的并发（分片后本身并发取决于运行的实例数量）
     * 当实例数小于node数时才考虑在单个实例上继续并发执行
     */
    default void execute(ShardingGroupPageJobContext<G> context) {
        List<String> dataNodes = context.getDataNodes();
        int shardingTotalReal = Math.min(dataNodes.size(), context.getShardingTotal());
        dataNodes.stream().filter(node -> dataNodes.indexOf(node) % shardingTotalReal == context.getShardingIndex())
                .forEach(node -> {
                    context.setCurrentDataNode(node);
                    this.executeByGroup(context);
                });
    }

    /***
     * 分组执行, 调用方直接调用
     * 暂不支持group层级的并发（按实际要求复写该方法做并发即可）
     * 如果需要在组的层面并发执行策略，上下文属性的传递方式需要重新定义（可采用clone）,不然会有可见性的问题
     */
    default void executeByGroup(ShardingGroupPageJobContext<G> context) {
        List<G> groups = this.getGroups(context);
        for (G group : groups) {
            context.setGroup(group);

            int totalRecord = this.getTotalRecord(context);
            int totalPageSize = totalRecord % context.getPageSize() == 0 ? totalRecord / context.getPageSize() : totalRecord / context.getPageSize() + 1;

            for (int index = 1; index <= totalPageSize; index++) {
                boolean interrupted = Thread.currentThread().isInterrupted();
                if (interrupted) {
                    throw new CancellationException("中断任务执行");
                }
                context.setOffset((index - 1) * context.getPageSize());
                List<T> dataList = this.fetchPageData(context);
                if (CollectionUtils.isNotEmpty(dataList)) {
                    this.processData(dataList, context);
                }
            }
            this.postGroupProcessor(context);
        }
    }

    /**
     * 0.获取所有分组
     */
    List<G> getGroups(ShardingGroupPageJobContext<G> context);

    /**
     * 1.获取数据总数
     */
    int getTotalRecord(ShardingGroupPageJobContext<G> context);

    /**
     * 2.分页获取数据
     */
    List<T> fetchPageData(ShardingGroupPageJobContext<G> context);

    /**
     * 3.处理数据
     */
    void processData(List<T> dataList, ShardingGroupPageJobContext<G> context);

    /**
     * group后置处理
     */
    default void postGroupProcessor(ShardingGroupPageJobContext<G> context) {

    }
}
