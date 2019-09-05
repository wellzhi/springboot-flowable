package com.dapeng.flow.controller;


import com.dapeng.flow.common.result.ResponseData;
import com.dapeng.flow.common.utils.BeanUtils;
import com.dapeng.flow.flowable.handler.InstanceHandler;
import com.dapeng.flow.flowable.handler.ProcessHandler;
import com.dapeng.flow.repository.model.DeploymentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
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
        Deployment deploy = processHandler.deploy("processes/bpmn/leaveNew.bpmn", "processes/bpmn/leaveNew.png", name, "leave category");
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


    //@PostMapping(value = "/deploy/files", headers = "content-type=multipart/form-data")
    @RequestMapping(value = "/deploy/files", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    @ApiOperation(value = "一次部署多个流程定义文件", notes = "对于调用式的主子流程模板，需要一起部署")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "主模板名称（模板ID）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "category", value = "模板类别", required = false, dataType = "String"),
            @ApiImplicitParam(name = "tenantId", value = "系统标识", required = false, dataType = "String"),
            //@ApiImplicitParam(name = "files", value = "模板文件", required = true, allowMultiple = true, dataType = "__file")
    })
    @ResponseBody
    public ResponseData filesUpload(String name, String category, String tenantId, MultipartFile[] files) {
        int len = files.length;

        if (null != files && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                String name1 = files[i].getName();
                logger.error(name1);
            }
        }
        return null;
    }

    @RequestMapping(value = "/deployModels", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData filesUpload(@RequestParam("files") MultipartFile[] files) {
        int length = files.length;
        return null;
    }

    /**
     * 部署流程定义
     */

    @ResponseBody
    @ApiOperation(value = "部署流程模板文件", notes = "模板与工作流微服务解耦，模板文件可来自网络中某个位置，也可以来自业务项目。" +
            "tenantId用于记录流程定义归属于哪个业务系统，实际使用中，可以为每个系统设置一个固定标识")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "模板名称（模板ID）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "category", value = "模板类别", required = false, dataType = "String"),
            @ApiImplicitParam(name = "tenantId", value = "系统标识", required = false, dataType = "String"),
            @ApiImplicitParam(name = "file", value = "模板文件", required = true, dataType = "__file")
    })
    @RequestMapping(value = "/deploy/file", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ResponseData<DeploymentVO> deployByInputStream(String name, String tenantId, String category, MultipartFile file) {
        InputStream in = null;
        Deployment deploy = null;
        try {
            boolean exist = processHandler.exist(name);
            if (!exist) {
                in = file.getInputStream();
                deploy = processHandler.deploy(name, tenantId, category, in);
            } else {
                deploy = processHandler.deployName(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        //忽略二进制文件（模板文件、模板图片）返回
        DeploymentVO deploymentVO = BeanUtils.copyBean(deploy, DeploymentVO.class, "resources");
        return ResponseData.success(deploymentVO);
    }
}

