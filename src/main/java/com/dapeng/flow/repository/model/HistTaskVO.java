package com.dapeng.flow.repository.model;



import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 历史任务返回类
 *
 * @author liuxz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="HistTask对象", description="")
public class HistTaskVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private Integer revision;
    private String executionId;
    private String processInstanceId;
    private String processDefinitionId;
    private String taskDefinitionId;
    private String scopeId;
    private String subScopeId;
    private String scopeType;
    private String scopeDefinitionId;
    private Date createTime;
    private String endTime;
    private String durationInMillis;
    private String deleteReason;
    private String name;
    private String parentTaskId;
    private String description;
    private String owner;
    private String assignee;
    private String taskDefinitionKey;
    private String formKey;
    private Integer priority;
    private String dueDate;
    private Date claimTime;
    private String category;
    private String tenantId;
    private Date lastUpdateTime;
    private String queryVariables;
    private Date time;
    private Date startTime;
    private String workTimeInMillis;
    private Boolean inserted;
    private String idPrefix;
    private Boolean updated;
    private Boolean deleted;
    private Integer revisionNext;

}
