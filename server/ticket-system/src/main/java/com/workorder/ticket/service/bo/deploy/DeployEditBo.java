package com.workorder.ticket.service.bo.deploy;

import java.util.List;

/**
 * 部署请求编辑对象
 * 
 * @author wzdong
 * @Date 2019年3月25日
 * @version 1.0
 */
public class DeployEditBo {
	private Long id;
	private String title;
	private Byte type;
	private String version;
	private List<ProjectBo> projects;
	private String content;
	private String comment;
	private List<DeployStepBo> deploySteps;

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

	public void setProjects(List<ProjectBo> projects) {
		this.projects = projects;
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

}
