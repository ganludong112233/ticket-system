package com.workorder.ticket.service.convertor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.workorder.ticket.persistence.dto.deploy.DeployQueryDto;
import com.workorder.ticket.persistence.dto.deploy.DeployWithCreatorDto;
import com.workorder.ticket.persistence.entity.Deploy;
import com.workorder.ticket.persistence.entity.DeployProject;
import com.workorder.ticket.persistence.entity.DeployStep;
import com.workorder.ticket.persistence.entity.Project;
import com.workorder.ticket.persistence.entity.User;
import com.workorder.ticket.service.bo.deploy.DeployBo;
import com.workorder.ticket.service.bo.deploy.DeployEditBo;
import com.workorder.ticket.service.bo.deploy.DeployQueryBo;
import com.workorder.ticket.service.bo.deploy.DeployStepBo;
import com.workorder.ticket.service.bo.deploy.ProjectBo;
import com.workorder.ticket.service.bo.user.UserBaseBo;

public class DeployServiceConvertor {

	public static Deploy buildDeploy(DeployEditBo editBo) {
		if (editBo == null) {
			return null;
		}
		Deploy deploy = new Deploy();
		deploy.setId(editBo.getId());
		deploy.setTitle(editBo.getTitle());
		deploy.setType(editBo.getType());
		deploy.setVersion(editBo.getVersion());
		deploy.setContent(editBo.getContent());
		deploy.setComment(editBo.getComment());
		return deploy;
	}

	public static List<DeployProject> buildDeployProjects(DeployEditBo editBo) {
		if (editBo == null) {
			return null;
		}

		List<ProjectBo> projectBo = editBo.getProjects();
		if (CollectionUtils.isEmpty(projectBo)) {
			return null;
		}
		List<DeployProject> deployProjectList = new ArrayList<DeployProject>();
		projectBo.forEach(item -> {
			DeployProject deployProject = new DeployProject();
			deployProject.setDeployId(editBo.getId());
			deployProject.setProjectId(item.getId());
			deployProject.setProjectName(item.getName());
			deployProjectList.add(deployProject);
		});

		return deployProjectList;
	}

	public static List<DeployStep> buildDeploySteps(DeployEditBo editBo) {
		if (editBo == null) {
			return null;
		}

		List<DeployStepBo> stepBo = editBo.getDeploySteps();
		if (CollectionUtils.isEmpty(stepBo)) {
			return null;
		}
		List<DeployStep> deployStepList = new ArrayList<DeployStep>();
		stepBo.forEach(item -> {
			DeployStep deployStep = new DeployStep();
			deployStep.setDeployId(editBo.getId());
			deployStep.setStepOrder(item.getStepOrder());
			deployStep.setType(item.getType());
			deployStep.setContent(item.getContent());
			deployStepList.add(deployStep);
		});

		return deployStepList;
	}

	public static DeployBo buildDeployBo(
			DeployWithCreatorDto deployWithCreator, User currentUser,
			List<Project> projects, List<DeployStep> deployStepList) {
		DeployBo deployBo = new DeployBo();
		deployBo.setId(deployWithCreator.getId());
		deployBo.setTitle(deployWithCreator.getTitle());
		deployBo.setType(deployWithCreator.getType());
		deployBo.setVersion(deployWithCreator.getVersion());
		if (!CollectionUtils.isEmpty(projects)) {
			List<ProjectBo> projectBoList = new ArrayList<ProjectBo>();
			projects.forEach(item -> projectBoList.add(new ProjectBo(item)));
			deployBo.setProjects(projectBoList);
		}
		deployBo.setStatus(deployWithCreator.getStatus());
		deployBo.setContent(deployWithCreator.getContent());
		deployBo.setComment(deployWithCreator.getComment());
		deployBo.setCreateTime(deployWithCreator.getCreateTime());
		deployBo.setSubmitTime(deployWithCreator.getSubmitTime());

		if (!CollectionUtils.isEmpty(deployStepList)) {
			List<DeployStepBo> deploySteps = new ArrayList<DeployStepBo>();
			deployStepList.forEach(item -> deploySteps.add(new DeployStepBo(
					item)));
			deployBo.setDeploySteps(deploySteps);
		}
		deployBo.setFlowEngineDefinitionId(deployWithCreator
				.getFlowEngineDefinitionId());
		deployBo.setFlowEngineInstanceId(deployWithCreator
				.getFlowEngineInstanceId());
		UserBaseBo creator = new UserBaseBo(deployWithCreator.getCreateId(),
				deployWithCreator.getCreateUsername(),
				deployWithCreator.getCreateRealName());

		deployBo.setCreateUser(creator);
		if (currentUser != null) {
			UserBaseBo currentProcessor = new UserBaseBo(currentUser.getId(),
					currentUser.getUsername(), currentUser.getRealname());
			deployBo.setCurrentProcessor(currentProcessor);
		}
		return deployBo;
	}

	public static DeployQueryDto buildDeployQueryDto(DeployQueryBo queryBo) {
		DeployQueryDto dto = new DeployQueryDto();
		dto.setStatus(queryBo.getStatus());
		dto.setCreator(queryBo.getSubmitUser());
		dto.setProjectId(queryBo.getProjectId());
		dto.setTitle(queryBo.getTitle());
		dto.setCreateRange(queryBo.getCreateRange());
		dto.setSubmitRange(queryBo.getSubmitRange());
		dto.setPageItem(queryBo.getPageItem());
		return dto;
	}
}
