package com.workorder.ticket.service.bo.deploy;

import java.util.Date;
import java.util.List;

import com.workorder.ticket.service.bo.user.UserBaseBo;

public class DeployBo {
	private Long id;
	private String title;
	private Byte type;
	private String version;
	private List<ProjectBo> projects;
	private Byte status;
	private String content;
	private String comment;
	private List<DeployStepBo> deploySteps;
	private String flowEngineDefinitionId;
	private String flowEngineInstanceId;
	private Date createTime;
	private Date submitTime;
	private UserBaseBo createUser;
	private UserBaseBo currentProcessor;// 当前处理人

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

	public List<ProjectBo> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectBo> projectIds) {
		this.projects = projectIds;
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

	public List<DeployStepBo> getDeploySteps() {
		return deploySteps;
	}

	public void setDeploySteps(List<DeployStepBo> deploySteps) {
		this.deploySteps = deploySteps;
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

	public UserBaseBo getCreateUser() {
		return createUser;
	}

	public void setCreateUser(UserBaseBo createUser) {
		this.createUser = createUser;
	}

	public UserBaseBo getCurrentProcessor() {
		return currentProcessor;
	}

	public void setCurrentProcessor(UserBaseBo currentProcessor) {
		this.currentProcessor = currentProcessor;
	}

}
