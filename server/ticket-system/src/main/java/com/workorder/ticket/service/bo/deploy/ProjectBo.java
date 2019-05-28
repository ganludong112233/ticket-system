package com.workorder.ticket.service.bo.deploy;

import com.workorder.ticket.persistence.entity.Project;

public class ProjectBo {
	private Integer id;
	private String name;
	private Integer parentId;
	private String description;

	public ProjectBo() {
	}

	public ProjectBo(Project project) {
		if (project != null) {
			this.id = project.getId();
			this.name = project.getName();
			this.parentId = project.getParentId();
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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
