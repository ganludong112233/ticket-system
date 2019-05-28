package com.workorder.ticket.persistence.entity;

import java.util.Date;

public class FlowTemplate {
	private Long id;

	private String flowEngineDefinitionId;

	private String name;

	private String code;

	private String description;

	private String comment;

	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFlowEngineDefinitionId() {
		return flowEngineDefinitionId;
	}

	public void setFlowEngineDefinitionId(String flowEngineDefinitionId) {
		this.flowEngineDefinitionId = flowEngineDefinitionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}