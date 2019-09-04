package com.dapeng.flow.flowable.handler;


import com.dapeng.flow.common.utils.BeanUtils;
import com.dapeng.flow.flowable.ActInstance;
import com.dapeng.flow.flowable.ServiceFactory;
import com.dapeng.flow.repository.model.TaskVO;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.impl.cmd.AddMultiInstanceExecutionCmd;
import org.flowable.engine.impl.cmd.DeleteMultiInstanceExecutionCmd;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
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
    private TaskHandler taskHandler;
    @Autowired
    private TaskQueryHandler taskQueryHandler;

    @Override
    public ProcessInstance startProcessInstanceByKey(String processDefinitionKey) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        logger.info("流程实例ID:{}---流程定义ID:{}", pi.getId(), pi.getProcessDefinitionId());
        return pi;
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
    public ProcessInstance startProcessInstanceByKeyAndTenantId(String processDefinitionKey, String tenantId, Map<String, Object> variables) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKeyAndTenantId(processDefinitionKey, variables, tenantId);
        logger.info("流程实例ID:{}---流程定义ID:{}", pi.getId(), pi.getProcessDefinitionId());
        return pi;
    }

    @Override
    public ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        logger.info("流程实例ID:{}---流程定义ID:{}", pi.getId(), pi.getProcessDefinitionId());
        return pi;
    }

    @Override
    public ProcessInstance startProcessInstanceByKeyAndTenantId(String processDefinitionKey, String businessKey,
                                                                String tenantId, Map<String, Object> variables) {
        ProcessInstance pi = runtimeService
                .startProcessInstanceByKeyAndTenantId(processDefinitionKey, businessKey, variables, tenantId);
        logger.info("流程实例ID:{}---流程定义ID:{}", pi.getId(), pi.getProcessDefinitionId());
        return pi;
    }


    @Override
    public void suspendProcessInstanceById(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
        logger.info("成功中断流程实例ID:{}", processInstanceId);
    }

    @Override
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
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

    @Override
    public Map<String, Object> startInstanceAndExecuteFirstTask(String processDefinitionKey, String tenantId, String userId, Map<String, Object> variables) {
        ProcessInstance pi = null;
        if (StringUtils.isNotBlank(tenantId)) {
            pi = runtimeService.startProcessInstanceByKeyAndTenantId(processDefinitionKey, variables, tenantId);
        } else {
            pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        }
        String instanceId = pi.getProcessInstanceId();
        logger.info("流程实例ID:{}---流程定义ID:{}", instanceId, pi.getProcessDefinitionId());
        Task task = taskQueryHandler.processInstanceId(instanceId);
        String id = task.getId();
        taskHandler.setAssignee(id, userId);
        taskHandler.setOwner(id, userId);
        task.setAssignee(userId);
        task.setOwner(userId);
        taskService.complete(id, variables);
        Task activeTask = taskQueryHandler.processInstanceId(instanceId);
        Map<String, Object> map = new HashMap<>(16);
        logger.info("旧任务ID{}--新任务ID:{}", id, activeTask.getId());
        //剔除返回懒加载属性，否则json解析报错
        TaskVO taskVO = BeanUtils.copyBean(task, TaskVO.class);
        TaskVO activeTaskVO = BeanUtils.copyBean(activeTask, TaskVO.class);

        map.put("finish", taskVO);
        map.put("active", activeTaskVO);

        return map;
    }

    @Override
    public void addMultiInstanceExecution(String activityDefId, String instanceId, Map<String, Object> variables) {
        managementService.executeCommand(new AddMultiInstanceExecutionCmd(activityDefId, instanceId, variables));
    }

    @Override
    public void deleteMultiInstanceExecution(String currentChildExecutionId, boolean flag) {
        managementService.executeCommand(new DeleteMultiInstanceExecutionCmd(currentChildExecutionId, flag));
    }

}
