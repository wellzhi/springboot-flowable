package com.dapeng.flow.controller;


import com.dapeng.flow.common.result.ResponseData;
import com.dapeng.flow.repository.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


/**
 * @author liuxz
 * @since 2019-03-22
 */
@RestController
@RequestMapping("api/flow/img")
@Api(value = "Test", tags = {"跟踪高亮"})
public class ImageController {
    protected static Logger logger = LoggerFactory.getLogger(ImageController.class);
    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/generateImage", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ApiOperation(value = "流程跟踪图", notes = "图片生成，接口返回")
    @ApiImplicitParams({@ApiImplicitParam(name = "procInstId", value = "流程实例ID", required = true, dataType = "String")})
    @ResponseBody
    public byte[] generateImage(String procInstId) throws Exception {
        return imageService.generateImageByProcInstId(procInstId);
    }

    @RequestMapping(value = "/viewProcessImg", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "流程跟踪图", notes = "生成到本地文件夹下，前端再读取", produces = "application/json")
    public void viewProcessImg(String processId, HttpServletResponse response) throws IOException {
        OutputStream os = null;
        try {
            String directory = "F:" + File.separator + "temp" + File.separator;
            final String suffix = ".png";
            File folder = new File(directory);
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.getName().equals(processId + suffix)) {
                    file.delete();
                }
            }
            byte[] processImage = imageService.generateImageByProcInstId(processId);
            File dest = new File(directory + processId + suffix);
            os = new FileOutputStream(dest, true);
            os.write(processImage, 0, processImage.length);
            os.flush();
        } catch (Exception e) {
            logger.error("流程跟踪图生成失败： {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
}

