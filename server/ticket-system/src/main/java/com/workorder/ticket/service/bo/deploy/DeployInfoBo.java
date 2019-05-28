package com.workorder.ticket.service.bo.deploy;

import java.util.List;

public class DeployInfoBo extends DeployBo {
	private List<DeployStepBo> deploySteps;

	public List<DeployStepBo> getDeploySteps() {
		return deploySteps;
	}

	public void setDeploySteps(List<DeployStepBo> deploySteps) {
		this.deploySteps = deploySteps;
	}

}
