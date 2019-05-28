package com.workorder.ticket.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workorder.ticket.persistence.dto.deploy.DeployProjectDto;
import com.workorder.ticket.persistence.entity.Project;

public interface ProjectDao {
	int deleteByPrimaryKey(Integer id);

	int insert(Project record);

	int insertSelective(Project record);

	Project getByPrimaryKey(Integer id);

	List<Project> getByParam(@Param("projectName") String projectName,
			@Param("parentId") Long parentId);

	List<Project> getByDeployId(Long deployId);

	List<DeployProjectDto> getByDeployIds(
			@Param("deployIds") List<Long> deployIds);

	int updateByPrimaryKeySelective(Project record);

	int updateByPrimaryKey(Project record);
}