package com.dapeng.flow.flowable;



import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;

import java.util.List;

/**
 * 历史任务封装
 *
 * @author liuxz
 * @date 2019/08/30
 */
public interface ActHistTaskQuery {
    /**
     * 历史流程任务查询对象获取
     *
     * @return
     */
    HistoricTaskInstanceQuery createHistoricTaskInstanceQuery();

    /**
     * 通过流程实例id，查询活动任务
     *
     * @param instanceId
     * @return
     */
    HistoricTaskInstance activeTask(String instanceId);

    /**
     * 通过流程任务id，查询活动任务
     *
     * @param taskId 流程任务ID
     * @return
     */
    HistoricTaskInstance finishedTask(String taskId);

    /**
     * 通过流程实例ID，查询所有任务
     *
     * @param instanceId 流程实例ID
     * @return
     */
    List<HistoricTaskInstance> listByInstanceId(String instanceId);

    /**
     * 通过流程实例ID，分页查询所有任务
     *
     * @param instanceId 流程实例ID
     * @param start 查询页数
     * @param limit 查询数量
     * @return
     */
    List<HistoricTaskInstance> pageListByInstanceId(String instanceId, int start, int limit);
}
