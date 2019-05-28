package com.workorder.ticket.service;

import java.util.List;

import com.workorder.ticket.service.bo.deploy.ProjectBo;

/**
 * 项目服务
 * 
 * @author wzdong
 * @Date 2019年3月26日
 * @version 1.0
 */
public interface ProjectService {
	public List<ProjectBo> queryProject(String projectName, Long parentId);
}
