package com.dapeng.flow.flowable.handler;

import com.dapeng.flow.flowable.ActTask;
import com.dapeng.flow.flowable.ServiceFactory;
import org.flowable.engine.runtime.ChangeActivityStateBuilder;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程任务
 *
 * @author liuxz
 * @date 2019/08/30
 */
@Component
public class TaskHandler extends ServiceFactory implements ActTask {

    protected static Logger logger = LoggerFactory.getLogger(TaskHandler.class);
    @Autowired
    private HistTaskQueryHandler histTaskQueryHandler;
    @Autowired
    private TaskHandler taskHandler;

    @Override
    public void claim(String taskId, String userId) throws RuntimeException, Exception {

        taskService.claim(taskId, userId);
    }

    @Override
    public void unclaim(String taskId) throws RuntimeException, Exception {

        taskService.unclaim(taskId);
    }

    @Override
    public void complete(String taskId) throws RuntimeException, Exception {

        this.complete(taskId, null);

        logger.info("-----------任务ID：{},已完成-----------", taskId);
    }

    @Override
    public void complete(String taskId, Map<String, Object> variables) throws RuntimeException, Exception {

        taskService.complete(taskId, variables);
    }

    @Override
    public Map<String, Object> complete(String taskId, Map<String, Object> variables, boolean localScope) {
        HistoricTaskInstance finishTask = histTaskQueryHandler.finishedTask(taskId);
        taskService.complete(taskId, variables, localScope);
        HistoricTaskInstance activeTask = histTaskQueryHandler.activeTask(finishTask.getProcessInstanceId());
        Map<String, Object> map = new HashMap<>(16);
        map.put("finish", finishTask);
        map.put("active", activeTask);
        return map;
    }

    @Override
    public void delegate(String taskId, String userId) {

        taskService.delegateTask(taskId, userId);
    }


    @Override
    public void resolveTask(String taskId) {
        taskService.resolveTask(taskId);
    }


    @Override
    public void setAssignee(String taskId, String userId) {

        taskService.setAssignee(taskId, userId);
    }

    @Override
    public void setOwner(String taskId, String userId) {
        taskService.setOwner(taskId, userId);
    }

    @Override
    public void delete(String taskId) {
        taskService.deleteTask(taskId);
    }

    @Override
    public void deleteWithReason(String taskId, String reason) {
        taskService.deleteTask(taskId, reason);
    }

    @Override
    public void addCandidateUser(String taskId, String userId) throws RuntimeException, Exception {

        taskService.addCandidateUser(taskId, userId);
    }

    @Override
    public Comment addComment(String taskId, String processInstanceId, String message)
            throws RuntimeException, Exception {

        return taskService.addComment(taskId, processInstanceId, message);
    }

    @Override
    public List<Comment> getTaskComments(String taskId) throws RuntimeException, Exception {

        return taskService.getTaskComments(taskId);
    }

    @Override
    public void withdraw(String processInstanceId, String currentActivityId, String newActivityId) {
        //List<String> currentActivityIds = new ArrayList<>();
        //currentActivityIds.add(currentActivityId);
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(processInstanceId)
                //.moveActivityIdsToSingleActivityId(currentActivityIds, newActivityId)
                .moveActivityIdTo(currentActivityId, newActivityId)
                .changeState();
    }

    @Override
    public void setVariableLocal(String taskId, String variableName, Object value) {
        taskService.setVariableLocal(taskId, variableName, value);
    }

    @Override
    public void setVariablesLocal(String taskId, Map<String, ? extends Object> variables) {
        taskService.setVariablesLocal(taskId, variables);
    }

}
