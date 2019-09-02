package com.dapeng.flow.repository.model;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Auto-generated: 2019-09-02 11:55:26
 *
 * @author liuxz
 * @date 2019.09.01
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
    private Boolean isIdentityLinksInitialized;
    private String executionId;
    private String processInstanceId;
    private String processDefinitionId;
    private String taskDefinitionId;
    private String scopeId;
    private String subScopeId;
    private String scopeType;
    private String scopeDefinitionId;
    private String taskDefinitionKey;
    private String formKey;
    private Boolean isCanceled;
    private Boolean isCountEnabled;
    private Integer variableCount;
    private Integer identityLinkCount;
    private Integer subTaskCount;
    private Date claimTime;
    private String tenantId;
    private String eventName;
    private String eventHandlerId;
    private String idPrefix;
    private Boolean forcedUpdate;
    private Boolean isInserted;
    private Boolean isUpdated;
    private Boolean isDeleted;
}