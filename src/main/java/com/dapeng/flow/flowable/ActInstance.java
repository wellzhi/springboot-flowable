package com.dapeng.flow.flowable;


import org.flowable.engine.runtime.ProcessInstance;

import java.util.Map;

/**
 * 流程实例封装
 *
 * @author liuxz
 * @date 2019/08/30
 */
public interface ActInstance {

    /**
     * 启动流程实例---通过流程定义key（模板ID)
     *
     * @param processDefinitionKey 流程定义Key，不能为空.
     * @return
     */
    ProcessInstance startProcessInstanceByKey(String processDefinitionKey);

    /**
     * 启动流程实例---流程定义ID
     *
     * @param processDefinitionId 流程定义ID，不能为空.
     * @param variables           流程实例变量。
     * @return
     */
    ProcessInstance startProcessInstanceById(String processDefinitionId, Map<String, Object> variables);

    /**
     * 启动流程实例--通过流程定义key、流程实例变量
     *
     * @param processDefinitionKey 流程定义Key，不能为空.
     * @param variables            要传递给流程实例的变量，可以为null.
     * @return 流程实例对象
     */
    ProcessInstance startProcessInstanceByKey(String processDefinitionKey, Map<String, Object> variables);

    /**
     * 启动流程实例--通过流程定义key、流程实例变量、业务系统标识
     *
     * @param processDefinitionKey 流程定义Key，不能为空.
     * @param tenantId             业务系统标识.
     * @param variables            要传递给流程实例的变量，可以为null.
     * @return 流程实例对象
     */
    ProcessInstance startProcessInstanceByKeyAndTenantId(String processDefinitionKey, String tenantId, Map<String, Object> variables);

    /**
     * 启动新流程实例----使用给定key在最新版本的流程定义中。
     * <p>
     * 可以提供业务key以将流程实例与具有明确业务含义的特定标识符相关联。
     * 例如，在订单处理中，业务密钥可以是订单ID。
     * 然后，可以使用此业务键轻松查找该流程实例.
     * 提供这样的业务密钥肯定是一个最佳实践.
     * <p>
     * 【说明】processdefinitionKey-businessKey的组合必须是唯一的。
     *
     * @param processDefinitionKey 流程定义Key，不能为空.
     * @param variables            要传递给流程实例的变量，可以为null.
     * @param businessKey          业务key
     * @return
     * @throws Exception
     */
    ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables) throws Exception;

    /**
     * 启动流程实例--通过流程定义key、业务主键key、流程实例变量、业务系统标识
     *
     * @param processDefinitionKey 流程定义Key，不能为空.
     * @param businessKey          业务主键key.
     * @param tenantId             业务系统标识.
     * @param variables            要传递给流程实例的变量，可以为null.
     * @return 流程实例对象
     */
    ProcessInstance startProcessInstanceByKeyAndTenantId(String processDefinitionKey, String businessKey,
                                                         String tenantId, Map<String, Object> variables);

    /**
     * 中断流程实例
     *
     * @param processInstanceId 流程实例id
     * @return
     * @throws Exception
     */
    void suspendProcessInstanceById(String processInstanceId) throws Exception;

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例id
     * @param deleteReason      删除原因
     * @return
     * @throws Exception
     */
    void deleteProcessInstance(String processInstanceId, String deleteReason) throws Exception;

    /**
     * 设置流程开始节点发起人
     *
     * @param authenticatedUserId 用户id
     * @return
     */
    void setAuthenticatedUserId(String authenticatedUserId);

    /**
     * 启动流程实例并执行第一个流程任务，并且设置下一任务处理人---通过流程定义key（模板ID)
     *
     * @param processDefinitionKey 流程定义Key，不能为空.
     * @param variables            流程实例变量.
     * @param actorIds             下一环节任务处理人.
     * @return
     */
    ProcessInstance startInstanceAndExecuteFirstTask(String processDefinitionKey, Map<String, Object> variables, Map<String, Object> actorIds);

    /**
     * 使用给定的id激活流程实例.
     *
     * @param processInstanceId 流程实例ID
     */
    void activateProcessInstanceById(String processInstanceId);

    /**
     * 启动流程实例并执行第一个流程任务，并且设置下一任务处理人---通过流程定义key（模板ID)
     *
     * @param processDefinitionKey 流程定义Key，不能为空.
     * @param tenantId             业务系统标识.
     * @param userId               流程实例启动人.
     * @param variables            流程实例变量.
     * @return
     */
    Map<String, Object> startInstanceAndExecuteFirstTask(String processDefinitionKey, String tenantId, String userId, Map<String, Object> variables);

    /**
     * 多实例加签
     *
     * @param activityDefId 流程环节定义Key，不能为空.
     * @param instanceId    流程实例ID.
     * @param variables     流程实例变量.
     * @return
     */
    void addMultiInstanceExecution(String activityDefId, String instanceId, Map<String, Object> variables);

    /**
     * 多实例减签
     *
     * @param currentChildExecutionId 流程环节定义Key，不能为空.
     * @param flag                    流程实例ID.
     * @return
     */
    void deleteMultiInstanceExecution(String currentChildExecutionId, boolean flag);
}
