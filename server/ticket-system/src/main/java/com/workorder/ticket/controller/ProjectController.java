package com.workorder.ticket.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workorder.ticket.controller.vo.deploy.ProjectVo;
import com.workorder.ticket.service.ProjectService;
import com.workorder.ticket.service.bo.deploy.ProjectBo;

/**
 * 项目管理
 * 
 * @author wzdong
 * @Date 2019年3月26日
 * @version 1.0
 */
@RestController
public class ProjectController {
	@Resource
	private ProjectService projectService;

	@GetMapping("/project/query")
	public List<ProjectVo> queryProject(
			@RequestParam(value = "projectName", required = false) String projectName,
			@RequestParam(value = "parentId", required = false) Long parentId) {
		List<ProjectBo> projectList = projectService.queryProject(projectName,
				parentId);
		if (CollectionUtils.isEmpty(projectList)) {
			return Collections.emptyList();
		}
		List<ProjectVo> result = new ArrayList<ProjectVo>();
		projectList.forEach(item -> result.add(new ProjectVo(item)));
		return result;
	}
}
