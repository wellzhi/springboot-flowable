package com.dapeng.flow.repository.service;

/**
 * @author liuxz
 */
public interface ImageService {

    /**
     * 根据流程实例标识，生成流程跟踪图示（高亮）
     *
     * @param procInstId 流程实例标识
     * @return
     * @throws Exception
     */
    byte[] generateImageByProcInstId(String procInstId) throws Exception;

}
