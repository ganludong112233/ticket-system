package com.workorder.ticket.controller.vo.deploy;

import com.workorder.ticket.service.bo.deploy.ProjectBo;

public class ProjectVo {
	private Integer id;
	private String name;
	private String description;

	public ProjectVo() {
	}

	public ProjectVo(ProjectBo project) {
		if (project != null) {
			this.id = project.getId();
			this.name = project.getName();
			this.description = project.getDescription();
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
