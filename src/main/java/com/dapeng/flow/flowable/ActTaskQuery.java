package com.dapeng.flow.flowable;


import com.dapeng.flow.repository.model.TaskVO;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;

import java.util.List;
import java.util.Map;

/**
 * 流程任务查询相关封装
 *
 * @author liuxz
 * @date 2019/08/30
 */
public interface ActTaskQuery {
    /**
     * 创建流程任务查询对象
     *
     * @return TaskQuery  流程查询对象.
     */
    public TaskQuery createTaskQuery();

    /**
     * 查询任务变量值
     *
     * @param taskId       流程任务ID.
     * @param variableName 流程变量name.
     * @return String  流程变量Value.
     */
    public String findVariableByTaskId(String taskId, String variableName);

    /**
     * 根据流程实例ID,查询活动任务列表（多实例）
     *
     * @param processInstanceId 流程实例ID.
     */
    List<Task> processInstanceId4Multi(String processInstanceId);

    /**
     * 查询任务业务主键
     *
     * @param taskId 流程任务ID.
     * @return String  业务主键.
     */
    public String findBusinessKeyByTaskId(String taskId);

    /**
     * 创建历史任务查询对象
     *
     * @return HistoricTaskInstanceQuery
     */
    public HistoricTaskInstanceQuery createHistoricTaskInstanceQuery();

    /**
     * 执行流程任务
     *
     * @param taskId 流程任务ID.
     * @return Task  流程任务.
     */

    public Task taskId(String taskId);

    /**
     * 执行流程任务
     *
     * @param taskId 流程任务ID.
     * @return TaskVO  流程任务自定义封装类.
     */

    public TaskVO queryTaskVOById(String taskId);

    /**
     * 查询活动的流程任务
     *
     * @param processInstanceId 流程实例ID.
     * @return Task  流程任务.
     */
    public Task processInstanceId(String processInstanceId);


    /**
     * 通过用户标识(任务候选人)，分页查询任务列表
     *
     * @param userId 用户标识.
     * @param start
     * @param limit
     * @return List<Task>  流程任务列表.
     */
    public List<Task> taskCandidateUser(String userId, int start, int limit);

    /**
     * 通过用户标识(实际参与者)，分页查询任务列表
     *
     * @param userId 用户标识.
     * @param start
     * @param limit
     * @return List<Task>  流程任务列表.
     */
    public List<Task> taskAssignee(String userId, int start, int limit);


    /**
     * 通过用户标识(实际参与者、或候选人)，分页查询任务列表
     *
     * @param userId 用户标识.
     * @return List<Task>  流程任务列表.
     */
    public List<Task> taskCandidateOrAssigned(String userId);

    /**
     * 通过用户标识(或候选人)，分页查询任务列表
     *
     * @param userId    用户标识.
     * @param variables 查询条件map.
     * @param start
     * @param limit
     * @return List<Task>  分页流程任务列表.
     */
    public List<Task> taskCandidateUserByCondition(String userId, Map<String, Object> variables, int start, int limit);

    /**
     * 通过用户标识(实际参与者)，分页查询任务列表
     *
     * @param userId    用户标识.
     * @param variables 查询条件map.
     * @param start
     * @param limit
     * @return List<Task>  分页流程任务列表.
     */
    public List<Task> taskAssigneeByCondition(String userId, Map<String, Object> variables, int start, int limit);

    /**
     * 通过用户标识(实际参与者、或候选人)，分页查询任务列表
     *
     * @param userId    用户标识.
     * @param variables 查询条件map.
     * @param start
     * @param limit
     * @return List<Task>  分页流程任务列表.
     */
    public List<Task> taskCandidateOrAssignedByCondition(String userId, Map<String, Object> variables, int start, int limit);

    /**
     * 通过用户标识(候选人)，统计活动任务数目
     *
     * @param userId 用户标识.
     * @return 活动任务数量.
     */
    public long countTaskCandidateUser(String userId);

    /**
     * 通过用户标识(实际参与者)，统计活动任务数目
     *
     * @param userId 用户标识.
     * @return 活动任务数量.
     */
    public long countTaskAssignee(String userId);

    /**
     * 通过用户标识(实际参与者、或候选人)，统计活动任务数目（用户参与的）
     *
     * @param userId 用户标识.
     * @return 活动任务数量.
     */
    public long countTaskCandidateOrAssigned(String userId);

    /**
     * 通过用户标识(候选人)及查询条件map，统计活动任务数目（用户参与的）
     *
     * @param userId    用户标识.
     * @param variables 查询条件map.
     * @return 活动任务数量.
     */
    public long countTaskCandidateUserByCondition(String userId, Map<String, Object> variables);

    /**
     * 通过用户标识(实际参与者)及查询条件map，统计活动任务数目（用户参与的）
     *
     * @param userId    用户标识.
     * @param variables 查询条件map.
     * @return 活动任务数量.
     */
    public long countTaskAssigneeByCondition(String userId, Map<String, Object> variables);

    /**
     * 通过用户标识(实际参与者、候选人)及查询条件map，统计活动任务数目（用户参与的）
     *
     * @param userId    用户标识.
     * @param variables 查询条件map.
     * @return 活动任务数量.
     */
    public long countTaskCandidateOrAssignedByCondition(String userId, Map<String, Object> variables);


    /**
     * 通过用户标识(实际参与者)，分页查询历史任务列表
     *
     * @param userId 用户标识.
     * @param start
     * @param limit
     * @return List<Task>  历史流程任务列表.
     */
    public List<HistoricTaskInstance> taskAssigneeHistory(String userId, int start, int limit);

    /**
     * 分页查询活动任务列表
     *
     * @param assignee 用户标识（实际参与者）.
     * @param query    编程方式查询对象.
     * @return 活动任务数量.
     */
    public long countTaskAssigneeByTaskQuery(String assignee, TaskQuery query);


    /**
     * 分页查询活动任务列表
     *
     * @param assignee 用户标识（实际参与者）.
     * @param query    编程方式查询对象.
     * @param start
     * @param limit
     * @return List<Task>  历史流程任务列表.
     */
    public List<Task> taskAssigneeByTaskQuery(String assignee, TaskQuery query, int start, int limit);

}
