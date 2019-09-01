package com.dapeng.flow.controller;


import com.dapeng.flow.common.result.ResponseData;
import com.dapeng.flow.flowable.handler.InstanceHandler;
import com.dapeng.flow.flowable.handler.ProcessHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


/**
 * <p>
 * 部署流程定义
 * </p>
 *
 * @author liuxz
 * @since 2019-08-20
 */
@RestController
@RequestMapping("api/flow/process")
@Api(value = "Process", tags = {"流程模板"})
public class ProcessController {

    protected static Logger logger = LoggerFactory.getLogger(InstanceHandler.class);
    @Autowired
    private ProcessHandler processHandler;


    /**
     * 部署流程定义
     */
    @RequestMapping(value = "/deploy/repeat", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "部署流程定义(每次都部署）", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "流程定义名称（即：模板ID）", required = true, dataType = "String")
    })
    public ResponseData<Deployment> deployRepeat(String name) {
        logger.info("部署流程定义start");
        Deployment deploy = processHandler.deploy("processes/bpmn/leaveNew.bpmn", "processes/bpmn/leaveNew.png",name, "leave category");
        logger.info("部署流程定义end--{}:{}", deploy.getId(), deploy.getName());
        return ResponseData.success(deploy);
    }


    /**
     * 部署流程定义
     */
    @RequestMapping(value = "/deploy/noRepeat", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "部署流程定义(仅首次部署）", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "流程定义名称（即：模板ID）", required = true, dataType = "String")
    })
    public ResponseData<Deployment> deployNoRepeat(String name) {

        boolean exist = processHandler.exist(name);
        if (!exist) {
            logger.info("部署流程定义start");
            Deployment deploy = processHandler.deploy("processes/bpmn/test/test/leaveNew.bpmn", "processes/bpmn/test/test/leaveNew.png", name, "leave category");
            logger.info("部署流程定义end--{}:{}", deploy.getId(), deploy.getName());
            return ResponseData.success(deploy);
        }
        return ResponseData.success();
    }


    /**
     * 部署流程定义
     */

    @ResponseBody
    @ApiOperation(value = "部署流程模板文件", notes = "模板与工作流微服务解耦，模板文件可来自网络中某个位置，也可以来自业务项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "模板名称（模板ID）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "category", value = "模板类别", dataType = "String"),
            @ApiImplicitParam(name = "file", value = "模板文件", required = true, dataType = "__file")
    })
    @RequestMapping(value = "/deploy/file", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ResponseData<Deployment> deployByInputStream(String name, String category, MultipartFile file) {
        InputStream in = null;
        Deployment deploy = null;
        try {
            boolean exist = processHandler.exist(name);
            if (!exist) {
                in = file.getInputStream();
                deploy = processHandler.deploy(name, category, in);
            }
        } catch (Exception e) {
            logger.error("部署流程模板失败:", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("关闭输入流出错", e);
            }
        }
        return ResponseData.success(deploy);
    }
}

