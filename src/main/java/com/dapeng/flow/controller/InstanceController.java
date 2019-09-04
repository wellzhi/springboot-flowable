package com.dapeng.flow.controller;


import com.dapeng.flow.common.result.ResponseData;
import com.dapeng.flow.common.utils.BeanUtils;
import com.dapeng.flow.flowable.handler.InstanceHandler;
import com.dapeng.flow.flowable.handler.TaskQueryHandler;
import com.dapeng.flow.repository.model.TaskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 流程实例相关
 * </p>
 *
 * @author liuxz
 * @since 2019-08-20
 */
@RestController
@RequestMapping("api/flow/instance")
@Api(value = "Instance", tags = {"流程实例"})
public class InstanceController {
    protected static Logger logger = LoggerFactory.getLogger(InstanceHandler.class);
    @Autowired
    private InstanceHandler instanceHandler;
    @Autowired
    private TaskQueryHandler taskQueryHandler;

    @RequestMapping(value = "/startByKey", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "启动流程实例__通过流程定义name", notes = "实例启动成功，返回当前活动任务，如果部署流程模板时指定了tenantId，那么调用此方法也要指定", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "流程定义name", required = true, dataType = "String"),
            @ApiImplicitParam(name = "tenantId", value = "系统标识", required = false, dataType = "String"),
    })
    public ResponseData startByName(String name, String tenantId, @RequestBody Map<String, Object> variables) {
        ProcessInstance pi = instanceHandler.startProcessInstanceByKeyAndTenantId(name, tenantId, variables);
        List<Task> list = taskQueryHandler.processInstanceId4Multi(pi.getProcessInstanceId());
        List<TaskVO> taskVO = BeanUtils.copyList(list, TaskVO.class);
        return ResponseData.success(taskVO);
    }


    @RequestMapping(value = "/startById", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "启动流程实例--通过流程定义ID", notes = "实例启动成功，返回当前活动任务", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "流程定义ID", required = true, dataType = "String")})
    public ResponseData<TaskVO> startById(String id, @RequestBody Map<String, Object> variables) {
        ProcessInstance pi = instanceHandler.startProcessInstanceById(id, variables);
        Task task = taskQueryHandler.processInstanceId(pi.getProcessInstanceId());
        TaskVO taskVO = BeanUtils.copyBean(task, TaskVO.class);
        return ResponseData.success(taskVO);
    }


    @RequestMapping(value = "/startAndExecute", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "启动流程实例并执行第一个流程任务", notes = "返回已执行、活动的任务map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "流程定义KEY（模板ID）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "tenantId", value = "系统标识", required = false, dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "流程启动者ID", required = true, dataType = "String"),
    })
    public ResponseData startAndExecute(String name, String tenantId, String userId, @RequestBody Map<String, Object> variables) throws Exception {
        Map<String, Object> map = instanceHandler.startInstanceAndExecuteFirstTask(name, tenantId, userId, variables);
        return ResponseData.success(map);
    }


    @RequestMapping(value = "/startExecuteAndSetActor", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "启动流程实例并执行第一个流程任务,并设置下一任务处理人", notes = "{\"days\":\"6\",\"actorIds\":[\"zhangsan\",\"lisi\"]}", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "流程定义KEY（模板ID）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "tenantId", value = "系统标识", required = false, dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "流程启动者ID", required = true, dataType = "String"),
    })
    public ResponseData startAndExecuteAndSetActor(String name, String tenantId, String userId,
                                                   @RequestBody Map<String, Object> variables) {
        Map<String, Object> map = instanceHandler.startInstanceAndExecuteFirstTask(name, tenantId, userId, variables);
        return ResponseData.success(map);
    }


    @RequestMapping(value = "/suspendById", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "挂起流程实例", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", required = true, dataType = "String")})
    public ResponseData suspendById(String processInstanceId) throws Exception {
        instanceHandler.suspendProcessInstanceById(processInstanceId);
        return ResponseData.success("流程实例挂起成功");
    }


    @RequestMapping(value = "/activateById", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "激活流程实例", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", required = true, dataType = "String")})
    public ResponseData activateById(String processInstanceId) throws Exception {
        instanceHandler.activateProcessInstanceById(processInstanceId);
        return ResponseData.success("激活流程实例");
    }


    @RequestMapping(value = "/addMultiInstanceExecution", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "多实例加签", notes = "数据变化：act_ru_task、act_ru_identitylink各生成一条记录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityDefId", value = "流程环节定义ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "instanceId", value = "流程实例ID", required = true, dataType = "String")
    })
    public ResponseData addMultiInstanceExecution(String activityDefId, String instanceId, @RequestBody Map<String, Object> variables) {
        instanceHandler.addMultiInstanceExecution(activityDefId, instanceId, variables);
        return ResponseData.success("加签成功");
    }

    @RequestMapping(value = "/deleteMultiInstanceExecution", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "多实例减签", notes = "数据变化：act_ru_task减1、act_ru_identitylink不变", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentChildExecutionId", value = "子执行流ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "flag", value = "子执行流是否已执行", required = true, dataType = "boolean")
    })
    public ResponseData deleteMultiInstanceExecution(String currentChildExecutionId, boolean flag) {
        instanceHandler.deleteMultiInstanceExecution(currentChildExecutionId, flag);
        return ResponseData.success("减签成功");
    }
}

