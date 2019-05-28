package com.workorder.ticket.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workorder.ticket.persistence.entity.DeployProject;

public interface DeployProjectDao {
	int deleteByPrimaryKey(Long id);

	int deleteByDeployId(Long deployId);

	int insert(DeployProject record);

	int insertBatch(@Param("records") List<DeployProject> record);

	DeployProject getByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(DeployProject record);

	int updateByPrimaryKey(DeployProject record);
}