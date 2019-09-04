package com.dapeng.flow.flowable.handler;


import com.dapeng.flow.common.utils.BeanUtils;
import com.dapeng.flow.flowable.ActTaskQuery;
import com.dapeng.flow.flowable.ServiceFactory;
import com.dapeng.flow.flowable.common.VariablesEnum;
import com.dapeng.flow.repository.model.TaskVO;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 流程任务查询
 *
 * @author liuxz
 * @date 2019/08/30
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class TaskQueryHandler extends ServiceFactory implements ActTaskQuery {

    protected static Logger logger = LoggerFactory.getLogger(TaskQueryHandler.class);


    @Override
    public TaskQuery createTaskQuery() {

        return taskService.createTaskQuery();
    }

    @Override
    public HistoricTaskInstanceQuery createHistoricTaskInstanceQuery() {
        return historyService.createHistoricTaskInstanceQuery();
    }

    @Override
    public Task taskId(String taskId) {
        return createTaskQuery().taskId(taskId).singleResult();
    }

    @Override
    public TaskVO queryTaskVOById(String taskId) {

        Task task = createTaskQuery().taskId(taskId).singleResult();
        return BeanUtils.copyBean(task, TaskVO.class);
    }

    @Override
    public List<Task> taskCandidateUser(String candidateUser, int start, int limit) {

        return createTaskQuery().taskCandidateUser(candidateUser)
                .orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .listPage(start, limit);
    }

    @Override
    public List<Task> taskAssignee(String assignee, int start, int limit) {

        return createTaskQuery().taskAssignee(assignee).orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .listPage(start, limit);
    }

    @Override
    public List<Task> taskCandidateOrAssigned(String userId) {
        return createTaskQuery().taskCandidateOrAssigned(userId)
                .orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .list();
    }

    @Override
    public List<HistoricTaskInstance> taskAssigneeHistory(String assignee, int start, int limit) {

        return createHistoricTaskInstanceQuery().taskAssignee(assignee).orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .listPage(start, limit);
    }


    public TaskQuery buildTaskQueryByVariables(Map<String, Object> args) {
        TaskQuery tq = createTaskQuery();
        if (args != null && args.size() > 0) {
            for (Entry<String, Object> entry : args.entrySet()) {
                if (VariablesEnum.activityName.toString().equals(entry.getKey()) ||
                        VariablesEnum.orgName.toString().equals(entry.getKey())) {
                    tq.processVariableValueLike(entry.getKey(), String.valueOf(entry.getValue()));
                } else {
                    tq.processVariableValueEquals(entry.getKey(), entry.getValue());
                }
            }
        }

        return tq;
    }

    @Override
    public List<Task> taskCandidateUserByCondition(String candidateUser, Map<String, Object> variables, int start, int limit) {
        return buildTaskQueryByVariables(variables).taskCandidateUser(candidateUser)
                .orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .listPage(start, limit);
    }

    @Override
    public List<Task> taskAssigneeByCondition(String assignee, Map<String, Object> variables, int start, int limit) {
        return buildTaskQueryByVariables(variables).taskAssignee(assignee).orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .listPage(start, limit);
    }

    @Override
    public List<Task> taskCandidateOrAssignedByCondition(String userId, Map<String, Object> variables, int start,
                                                         int limit) {
        return buildTaskQueryByVariables(variables).taskCandidateOrAssigned(userId).orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .listPage(start, limit);
    }

    @Override
    public long countTaskCandidateUser(String candidateUser) {
        return createTaskQuery().taskCandidateUser(candidateUser)
                .orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .count();
    }

    @Override
    public long countTaskAssignee(String assignee) {
        return createTaskQuery().taskAssignee(assignee).orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .count();
    }

    @Override
    public long countTaskCandidateOrAssigned(String userId) {
        return createTaskQuery().taskCandidateOrAssigned(userId).orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .count();
    }

    @Override
    public long countTaskCandidateUserByCondition(String candidateUser, Map<String, Object> variables) {
        return buildTaskQueryByVariables(variables).taskCandidateUser(candidateUser)
                .orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .count();
    }

    @Override
    public long countTaskAssigneeByCondition(String assignee, Map<String, Object> variables) {
        return buildTaskQueryByVariables(variables).taskAssignee(assignee).orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .count();
    }

    @Override
    public long countTaskCandidateOrAssignedByCondition(String userId, Map<String, Object> variables) {
        return createTaskQuery().taskCandidateOrAssigned(userId).orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .count();
    }

    @Override
    public Task processInstanceId(String processInstanceId) {

        return createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
    }

    @Override
    public List<Task> processInstanceId4Multi(String processInstanceId) {
        List<Task> resultList = new ArrayList<>();
        TaskQuery taskQuery = createTaskQuery().processInstanceId(processInstanceId).active();

        long count = taskQuery.count();
        //多实例情况，当前活动任务不止一条数据
        if (count > 1) {
            resultList.addAll(taskQuery.list());
        } else {
            resultList.add(taskQuery.singleResult());
        }
        return resultList;
    }

    @Override
    public String findBusinessKeyByTaskId(String taskId) {

        Task task = this.createTaskQuery().taskId(taskId).singleResult();

        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();

        if (pi != null) {
            return pi.getBusinessKey();
        }

        return null;
    }

    @Override
    public String findVariableByTaskId(String taskId, String variableName) {

        Object value = taskService.getVariable(taskId, variableName);

        return String.valueOf(value);
    }

    @Override
    public long countTaskAssigneeByTaskQuery(String assignee, TaskQuery query) {
        return query.taskAssignee(assignee).orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .count();
    }

    @Override
    public List<Task> taskAssigneeByTaskQuery(String assignee, TaskQuery query, int start, int limit) {
        return query.taskAssignee(assignee).orderByTaskPriority().desc()
                .orderByTaskCreateTime().asc()
                .listPage(start, limit);
    }

}