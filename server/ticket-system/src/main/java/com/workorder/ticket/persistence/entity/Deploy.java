package com.workorder.ticket.persistence.entity;

import java.util.Date;

public class Deploy {
    private Long id;

    private String title;

    private Byte type;

    private String version;

    private Byte status;

    private String content;

    private String comment;

    private String flowEngineDefinitionId;

    private String flowEngineInstanceId;

    private Long createId;

    private Date createTime;

    private Date submitTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFlowEngineDefinitionId() {
        return flowEngineDefinitionId;
    }

    public void setFlowEngineDefinitionId(String flowEngineDefinitionId) {
        this.flowEngineDefinitionId = flowEngineDefinitionId;
    }

    public String getFlowEngineInstanceId() {
        return flowEngineInstanceId;
    }

    public void setFlowEngineInstanceId(String flowEngineInstanceId) {
        this.flowEngineInstanceId = flowEngineInstanceId;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }
}