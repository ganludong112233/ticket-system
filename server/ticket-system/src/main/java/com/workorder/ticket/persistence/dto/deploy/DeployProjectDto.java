package com.workorder.ticket.persistence.dto.deploy;

import com.workorder.ticket.persistence.entity.Project;

public class DeployProjectDto extends Project {
	private Long deployId;

	public Long getDeployId() {
		return deployId;
	}

	public void setDeployId(Long deployId) {
		this.deployId = deployId;
	}

}
