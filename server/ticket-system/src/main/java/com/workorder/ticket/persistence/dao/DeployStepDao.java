package com.workorder.ticket.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workorder.ticket.persistence.entity.DeployStep;

public interface DeployStepDao {
	int deleteByPrimaryKey(Long id);

	int deleteByDeployId(Long deployId);

	int insert(DeployStep record);

	int insertBatch(@Param("records") List<DeployStep> records);

	DeployStep getByPrimaryKey(Long id);

	List<DeployStep> getByDeployId(Long deployId);

	List<DeployStep> getByDeployIds(@Param("deployIds") List<Long> deployIds);

	int updateByPrimaryKeySelective(DeployStep record);

	int updateByPrimaryKeyWithBLOBs(DeployStep record);

	int updateByPrimaryKey(DeployStep record);
}