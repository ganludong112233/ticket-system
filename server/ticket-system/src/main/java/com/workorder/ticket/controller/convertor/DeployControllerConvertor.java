package com.workorder.ticket.controller.convertor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.sowing.common.page.Page;
import com.workorder.ticket.common.item.DateRange;
import com.workorder.ticket.controller.vo.deploy.DeployEditVo;
import com.workorder.ticket.controller.vo.deploy.DeployInfoVo;
import com.workorder.ticket.controller.vo.deploy.DeployPageVo;
import com.workorder.ticket.controller.vo.deploy.DeployQueryVo;
import com.workorder.ticket.controller.vo.deploy.DeployStepVo;
import com.workorder.ticket.controller.vo.deploy.ProjectVo;
import com.workorder.ticket.service.bo.deploy.DeployBo;
import com.workorder.ticket.service.bo.deploy.DeployEditBo;
import com.workorder.ticket.service.bo.deploy.DeployQueryBo;
import com.workorder.ticket.service.bo.deploy.DeployStepBo;
import com.workorder.ticket.service.bo.deploy.ProjectBo;
import com.workorder.ticket.service.bo.user.UserBaseBo;

public class DeployControllerConvertor {
	public static DeployEditBo buildDeployEditBo(DeployEditVo editVo) {
		DeployEditBo bo = new DeployEditBo();
		bo.setId(editVo.getId());
		bo.setTitle(editVo.getTitle());
		bo.setType(editVo.getType());
		bo.setVersion(editVo.getVersion());
		List<ProjectVo> projectList = editVo.getProjects();
		if (!CollectionUtils.isEmpty(projectList)) {
			List<ProjectBo> projectBoList = new ArrayList<ProjectBo>();
			projectList
					.forEach(item -> projectBoList.add(buildProjectVo(item)));
			bo.setProjects(projectBoList);
		}
		bo.setContent(editVo.getContent());
		bo.setComment(editVo.getComment());
		List<DeployStepVo> stepList = editVo.getDeploySteps();
		if (!CollectionUtils.isEmpty(stepList)) {
			List<DeployStepBo> stepBoList = new ArrayList<DeployStepBo>();
			stepList.forEach(item -> stepBoList.add(buildDeployStepBo(item)));
			bo.setDeploySteps(stepBoList);
		}
		return bo;
	}

	public static DeployStepBo buildDeployStepBo(DeployStepVo stepVo) {
		DeployStepBo step = new DeployStepBo();
		step.setId(stepVo.getId());
		step.setStepOrder(stepVo.getStepOrder());
		step.setType(stepVo.getType());
		step.setContent(stepVo.getContent());
		return step;
	}

	public static ProjectBo buildProjectVo(ProjectVo projectVo) {
		ProjectBo projectBo = new ProjectBo();
		projectBo.setId(projectVo.getId());
		projectBo.setName(projectVo.getName());
		projectBo.setDescription(projectVo.getDescription());
		return projectBo;
	}

	public static DeployQueryBo buildDeployQueryBo(DeployQueryVo queryVo) {
		DeployQueryBo bo = new DeployQueryBo();
		bo.setStatus(queryVo.getStatus());
		bo.setSubmitUser(queryVo.getSubmitor());
		bo.setProjectId(queryVo.getProjectId());
		bo.setTitle(queryVo.getTitle());
		if (queryVo.getStartTime() != null || queryVo.getEndTime() != null) {
			bo.setCreateRange(new DateRange(queryVo.getStartTime(), queryVo
					.getEndTime()));
		}
		if (queryVo.getPageSize() != null && queryVo.getPageNum() != null) {
			bo.setPageItem(new Page(queryVo.getPageNum(), queryVo.getPageSize()));
		}
		return bo;
	}

	public static DeployPageVo buildDeployPageVo(DeployBo deployBo) {
		DeployPageVo pageVo = new DeployPageVo();
		pageVo.setId(deployBo.getId());
		pageVo.setTitle(deployBo.getTitle());
		pageVo.setType(deployBo.getType());
		pageVo.setVersion(deployBo.getVersion());
		pageVo.setProjects(buildProjectVoList(deployBo.getProjects()));
		pageVo.setStatus(deployBo.getStatus());
		UserBaseBo processorUser = deployBo.getCurrentProcessor();
		if (processorUser != null) {
			pageVo.setCurrentProcessor(processorUser.getRealname());
		}
		UserBaseBo creatorUser = deployBo.getCreateUser();
		if (creatorUser != null) {
			pageVo.setCreator(creatorUser.getRealname());
			pageVo.setCreatorId(creatorUser.getUserId());
		}
		pageVo.setCreateTime(deployBo.getCreateTime());
		pageVo.setSubmitTime(deployBo.getSubmitTime());
		return pageVo;
	}

	public static DeployEditVo buildDeployEditVo(DeployBo deployBo) {
		DeployEditVo editVo = new DeployEditVo();
		editVo.setId(deployBo.getId());
		editVo.setTitle(deployBo.getTitle());
		editVo.setType(deployBo.getType());
		editVo.setVersion(deployBo.getVersion());
		editVo.setProjects(buildProjectVoList(deployBo.getProjects()));
		editVo.setContent(deployBo.getContent());
		editVo.setComment(deployBo.getComment());
		editVo.setDeploySteps(buildDeployStepVoList(deployBo.getDeploySteps()));
		return editVo;
	}

	public static DeployInfoVo buildDeployInfoVo(DeployBo deployBo) {
		DeployInfoVo info = new DeployInfoVo();
		info.setId(deployBo.getId());
		info.setTitle(deployBo.getTitle());
		info.setType(deployBo.getType());
		info.setVersion(deployBo.getVersion());
		List<ProjectBo> projectBoList = deployBo.getProjects();
		if (!CollectionUtils.isEmpty(projectBoList)) {
			List<ProjectVo> projectVoList = new ArrayList<ProjectVo>();
			projectBoList.forEach(item -> projectVoList
					.add(new ProjectVo(item)));
			info.setProjects(projectVoList);
		}

		info.setContent(deployBo.getContent());
		info.setComment(deployBo.getComment());
		List<DeployStepBo> steps = deployBo.getDeploySteps();
		if (!CollectionUtils.isEmpty(steps)) {
			List<DeployStepVo> deploySteps = new ArrayList<DeployStepVo>();
			steps.forEach(item -> deploySteps.add(new DeployStepVo(item)));
			info.setDeploySteps(deploySteps);
		}

		return info;
	}

	private static List<ProjectVo> buildProjectVoList(
			List<ProjectBo> projectBoList) {
		if (CollectionUtils.isEmpty(projectBoList)) {
			return Collections.emptyList();
		}
		List<ProjectVo> projectVoList = new ArrayList<ProjectVo>();
		projectBoList.forEach(item -> projectVoList.add(new ProjectVo(item)));
		return projectVoList;
	}

	private static List<DeployStepVo> buildDeployStepVoList(
			List<DeployStepBo> deployStepBoList) {
		if (CollectionUtils.isEmpty(deployStepBoList)) {
			return Collections.emptyList();
		}
		List<DeployStepVo> deployStepVoList = new ArrayList<DeployStepVo>();
		deployStepBoList.forEach(item -> deployStepVoList.add(new DeployStepVo(
				item)));
		return deployStepVoList;
	}
}
