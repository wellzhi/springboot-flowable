package com.dapeng.flow.listener.serviceTask;


import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */


public class MyServiceTask implements JavaDelegate {

    protected static Logger logger = LoggerFactory.getLogger(MyServiceTask.class);

    /**
     * 重写委托的提交方法
     */
    @Override
    public void execute(DelegateExecution execution) {

        logger.info("serviceTask已经执行已经执行！");
        List list = new ArrayList<>();
        list.add("甲");
        list.add("乙");
        list.add("丙");
        execution.setVariable("actorIds", list);
    }
}
