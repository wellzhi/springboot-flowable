package com.dapeng.flow.flowable.handler;


import com.dapeng.flow.flowable.ActInstance;
import com.dapeng.flow.flowable.ServiceFactory;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程实例
 *
 * @author liuxz
 * @date 2019/08/30
 */
@Component
public class InstanceHandler extends ServiceFactory implements ActInstance {

    protected static Logger logger = LoggerFactory.getLogger(InstanceHandler.class);
    @Autowired
    private HistTaskQueryHandler histTaskQueryHandler;
    @Autowired
    private TaskHandler taskHandler;

    @Override
    public ProcessInstance startProcessInstanceByKey(String processDefinitionKey) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        logger.info("流程实例ID:{}---流程定义ID:{}", pi.getId(), pi.getProcessDefinitionId());
        return pi;
    }

    @Override
    public Map<String, Object> startInstanceAndExecuteFirstTask(String processDefinitionKey, String userId, Map<String, Object> variables) throws Exception {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        String instanceId = pi.getProcessInstanceId();
        logger.info("流程实例ID:{}---流程定义ID:{}", instanceId, pi.getProcessDefinitionId());
        HistoricTaskInstance finishedTask = histTaskQueryHandler.activeTask(instanceId);
        String id = finishedTask.getId();
        taskHandler.setAssignee(id, userId);
        taskHandler.setOwner(id, userId);
        //TODO 待完善：map返回的任务信息中，owner和assignee都为空，不利于业务关联表保存处理人
        taskService.complete(id, variables);
        HistoricTaskInstance activeTask = histTaskQueryHandler.activeTask(instanceId);
        Map<String, Object> map = new HashMap<>(16);
        logger.info("旧任务ID{}--新任务ID:{}", id, activeTask.getId());
        map.put("finish", finishedTask);
        map.put("active", activeTask);
        return map;
    }

    @Override
    public ProcessInstance startProcessInstanceById(String processDefinitionId, Map<String, Object> variables) {
        ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinitionId, variables);
        logger.info("流程实例ID:{}---流程定义ID:{}", pi.getId(), pi.getProcessDefinitionId());
        return pi;
    }

    @Override
    public ProcessInstance startProcessInstanceByKey(String processDefinitionKey, Map<String, Object> variables) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        logger.info("流程实例ID:{}---流程定义ID:{}", pi.getId(), pi.getProcessDefinitionId());
        return pi;
    }

    @Override
    public ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey,
                                                     Map<String, Object> variables) throws RuntimeException, Exception {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        logger.info("流程实例ID:{}---流程定义ID:{}", pi.getId(), pi.getProcessDefinitionId());
        return pi;
    }


    @Override
    public void suspendProcessInstanceById(String processInstanceId) throws RuntimeException, Exception {
        runtimeService.suspendProcessInstanceById(processInstanceId);
        logger.info("成功中断流程实例ID:{}", processInstanceId);
    }

    @Override
    public void deleteProcessInstance(String processInstanceId, String deleteReason) throws RuntimeException, Exception {
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
        logger.info("成功删除流程实例ID:{}", processInstanceId);
    }

    @Override
    public void setAuthenticatedUserId(String authenticatedUserId) {
        identityService.setAuthenticatedUserId(authenticatedUserId);
    }

    @Override
    public ProcessInstance startInstanceAndExecuteFirstTask(String processDefinitionKey, Map<String, Object> variables, Map<String, Object> actorIds) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        logger.info("启动流程实例成功，流程实例ID:{}---流程定义ID:{}", pi.getId(), pi.getProcessDefinitionId());
        Task task = taskService.createTaskQuery().processInstanceId(pi.getProcessInstanceId()).active().singleResult();
        taskService.complete(task.getId(), actorIds);
        logger.info("第一个流程任务已执行成功taskId:{}", task.getId());
        return pi;
    }

    @Override
    public void activateProcessInstanceById(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
        logger.info("成功激活流程实例ID:{}", processInstanceId);
    }

}
