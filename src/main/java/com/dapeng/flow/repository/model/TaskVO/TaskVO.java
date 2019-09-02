package com.dapeng.flow.repository.model;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * activiti大部分方法返回的实体类中，由于存在转json时的懒加载问题，需要自定义实体类接收。
 *
 * @author liuxz
 * @since 2019-08-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="Task对象", description="")
public class TaskVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String owner;
    private Integer assigneeUpdatedCount;
    private String originalAssignee;
    private String assignee;
    private String parentTaskId;
    private String name;
    private String localizedName;
    private String description;
    private String localizedDescription;
    private Integer priority;
    private Date createTime;
    private Date dueDate;
    private Integer suspensionState;
    private String category;
    private boolean isIdentityLinksInitialized;
    private String executionId;
    private String processInstanceId;
    private String processDefinitionId;
    private String taskDefinitionKey;
    private String formKey;
    private boolean isDeleted;
    private boolean isCanceled;
    private String eventName;
    private String tenantId;
    private boolean forcedUpdate;
    private Date claimTime;
}
