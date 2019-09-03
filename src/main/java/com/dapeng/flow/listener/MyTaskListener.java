package com.dapeng.flow.listener;


import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Set;

/**
 * @author Administrator
 */
public class MyTaskListener implements TaskListener {
    protected static Logger logger = LoggerFactory.getLogger(TaskListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        String taskDefKey = delegateTask.getTaskDefinitionKey();
        Date createTime = delegateTask.getCreateTime();
        String name = delegateTask.getName();
        String eventName = delegateTask.getEventName();
        String assignee = delegateTask.getAssignee();
        Set<IdentityLink> candidates = delegateTask.getCandidates();
        logger.info("演示任务相关信息===========================================================");
        logger.info("任务Key:{}", taskDefKey);
        logger.info("任务Name:{}", name);
        logger.info("任务createTime:{}", createTime);
        logger.info("任务assignee:{}", assignee);
        logger.info("任务candidates:{}", candidates);

        logger.error("演示事件执行顺序===========================================================");
        if ("create".endsWith(eventName)) {
            logger.info("创建：create=========");
        } else if ("assignment".endsWith(eventName)) {
            logger.info("指派：assignment========");
        } else if ("complete".endsWith(eventName)) {
            logger.info("完成：complete===========");
        } else if ("delete".endsWith(eventName)) {
            logger.info("销毁：delete=============");
        }


    }


}
