package com.dapeng.flow.controller;


import com.dapeng.flow.common.result.ResponseData;
import com.dapeng.flow.common.utils.BeanUtils;
import com.dapeng.flow.flowable.handler.HistTaskQueryHandler;
import com.dapeng.flow.flowable.handler.TaskHandler;
import com.dapeng.flow.flowable.handler.TaskQueryHandler;
import com.dapeng.flow.repository.model.HistTaskVO;
import com.dapeng.flow.repository.model.TaskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.HistoryService;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>
 * 流程任务相关
 * </p>
 *
 * @author liuxz
 * @since 2019-08-28
 */
@RestController
@RequestMapping("api/flow/query")
@Api(value = "Query", tags = {"查询相关"})
public class QueryController {

    protected static Logger logger = LoggerFactory.getLogger(QueryController.class);
    @Autowired
    private TaskHandler taskHandler;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskQueryHandler taskQueryHandler;
    @Autowired
    private HistTaskQueryHandler histTaskQueryHandler;

    @RequestMapping(value = "/task/taskId ", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "任务查询", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String")})
    public ResponseData queryTask(String taskId) {
        Task task = taskQueryHandler.taskId(taskId);
        TaskVO taskVO = BeanUtils.copyBean(task, TaskVO.class);
        return ResponseData.success(taskVO);
    }

    @RequestMapping(value = "/task/list/userId ", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "根据用户ID，查询该用户参与的活动任务列表", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String")})
    public ResponseData queryUserList(String userId) {
        List<Task> tasks = taskQueryHandler.taskCandidateOrAssigned(userId);
        List<TaskVO> list = BeanUtils.copyList(tasks,TaskVO.class);
        return ResponseData.success(list);
    }




    @RequestMapping(value = "/comment ", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询批注信息", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String")})
    public ResponseData getTaskComments(String taskId) throws Exception {
        List<Comment> taskComments = taskHandler.getTaskComments(taskId);
        return ResponseData.success(taskComments);
    }


    @RequestMapping(value = "/task/list/instanceId", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询任务列表（所有）", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "instanceId", value = "流程实例ID", required = true, dataType = "String")})
    public Object listByInstanceId(String instanceId) {
        List<HistoricTaskInstance> list = histTaskQueryHandler.listByInstanceId(instanceId);
        List copyList = BeanUtils.copyList(list, HistTaskVO.class);
        return ResponseData.success(copyList);
    }

    @RequestMapping(value = "/task/pageList/instanceId", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "分页查询任务列表（所有）", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "instanceId", value = "流程实例ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required = false, defaultValue = "1", dataType = "int"),
            @ApiImplicitParam(name = "step", value = "数量", required = false, defaultValue = "20", dataType = "int"),
    })
    public Object pageListByInstanceId(String instanceId, Integer page, Integer step) {
        List<HistoricTaskInstance> list = histTaskQueryHandler.pageListByInstanceId(instanceId, page, step);
        return ResponseData.success(list);
    }


    @RequestMapping(value = "/task/list/unclaim", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询未签收任务列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required = false, defaultValue = "1", dataType = "int"),
            @ApiImplicitParam(name = "step", value = "数量", required = false, defaultValue = "20", dataType = "int")})
    public Object unclaim(String userId, Integer page, Integer step) {
        List<Task> taskList = taskQueryHandler.taskCandidateUser(userId, page, step);
        List list = BeanUtils.copyList(taskList, TaskVO.class);
        return ResponseData.success(list);
    }

    @RequestMapping(value = "/task/list/claimed", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询已签收任务列表", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String")})
    public Object claimed(String userId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).list();
        return ResponseData.success(list);
    }


    @RequestMapping(value = "/task/active", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "根据实例ID，查询当前活动任务", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "instanceId", value = "实例ID", required = true, dataType = "String")})
    public ResponseData<HistoricTaskInstance> query(String instanceId) throws Exception {
        //单实例任务--单个活动任务
        //方式一：需要处理懒加载问题
        //Task task = taskQueryHandler.processInstanceId(instanceId);
        //TaskVO taskVO = new TaskVO();
        //BeanUtils.copyProperties(task, taskVO);

        //方式二：正常使用，通过historyService查询当前活动任务，无需进行转换
        HistoricTaskInstance historicTaskInstance =
                historyService
                        .createHistoricTaskInstanceQuery()
                        .processInstanceId(instanceId)
                        .unfinished().singleResult();

        //多实例并行任务--多个活动任务
        //List<Task> list = taskService.createTaskQuery().processInstanceId(instanceId).active().list();

        return ResponseData.success(historicTaskInstance);
    }


}

