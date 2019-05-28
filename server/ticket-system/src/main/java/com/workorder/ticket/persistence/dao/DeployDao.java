package com.workorder.ticket.persistence.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workorder.ticket.controller.vo.common.HistogramItem;
import com.workorder.ticket.persistence.dto.deploy.DeployQueryDto;
import com.workorder.ticket.persistence.dto.deploy.DeployWithCreatorDto;
import com.workorder.ticket.persistence.entity.Deploy;

public interface DeployDao {
	int deleteByPrimaryKey(Long id);

	int insert(Deploy record);

	Deploy getByPrimaryKey(Long id);

	DeployWithCreatorDto getWithCreator(Long id);

	List<DeployWithCreatorDto> getListByParam(DeployQueryDto deployQueryDto);

	int getCountByParam(DeployQueryDto deployQueryDto);

	int updateByPrimaryKeySelective(Deploy record);

	int updateByPrimaryKey(Deploy record);
	
	List<HistogramItem> statisticByDay(@Param("startTime") Date start,
			@Param("endTime") Date end);
}