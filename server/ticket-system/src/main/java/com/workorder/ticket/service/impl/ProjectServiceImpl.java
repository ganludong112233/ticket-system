package com.workorder.ticket.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.workorder.ticket.persistence.dao.ProjectDao;
import com.workorder.ticket.persistence.entity.Project;
import com.workorder.ticket.service.ProjectService;
import com.workorder.ticket.service.bo.deploy.ProjectBo;

/**
 * 项目服务
 * 
 * @author wzdong
 * @Date 2019年3月26日
 * @version 1.0
 */
@Service
public class ProjectServiceImpl implements ProjectService {
	@Resource
	private ProjectDao projectDao;

	@Override
	public List<ProjectBo> queryProject(String projectName, Long parentId) {
		List<Project> projectList = projectDao
				.getByParam(projectName, parentId);
		if (CollectionUtils.isEmpty(projectList)) {
			return Collections.emptyList();
		}
		List<ProjectBo> result = new ArrayList<ProjectBo>();
		projectList.forEach(item -> result.add(new ProjectBo(item)));
		return result;
	}
}
