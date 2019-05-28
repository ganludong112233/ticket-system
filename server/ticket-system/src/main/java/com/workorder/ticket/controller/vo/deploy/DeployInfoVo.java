package com.workorder.ticket.controller.vo.deploy;

import java.util.List;

/**
 * 部署请求详情
 * 
 * @author wzdong
 * @Date 2019年3月25日
 * @version 1.0
 */
public class DeployInfoVo {

	private Long id;
	private String title;
	private Byte type;
	private String version;
	private List<ProjectVo> projects;
	private String content;
	private String comment;
	private List<DeployStepVo> deploySteps;

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

	public List<ProjectVo> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectVo> projects) {
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

	public List<DeployStepVo> getDeploySteps() {
		return deploySteps;
	}

	public void setDeploySteps(List<DeployStepVo> deploySteps) {
		this.deploySteps = deploySteps;
	}

}
