package com.dapeng.flow.listener;


import com.dapeng.flow.flowable.handler.InstanceHandler;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 */
public class GlobalListener implements ExecutionListener {
    protected static Logger logger = LoggerFactory.getLogger(GlobalListener.class);

    @Override
    public void notify(DelegateExecution execution) {
        String defId = execution.getProcessDefinitionId();
        String instanceId = execution.getProcessInstanceId();
        String eventName = execution.getEventName();
        logger.info("流程定义ID:{}   流程实例ID:{}", defId, instanceId);
        logger.debug("监听器事件名称：{}", eventName);
        if ("start".equals(eventName)) {
            logger.debug("{}事件执行了", eventName);
        } else if ("end".equals(eventName)) {
            logger.debug("{}事件执行了", eventName);
        } else if ("take".equals(eventName)) {
            logger.debug("{}事件执行了", eventName);
        }
    }
}
