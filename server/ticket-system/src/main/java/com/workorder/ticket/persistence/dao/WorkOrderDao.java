package com.workorder.ticket.persistence.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workorder.ticket.controller.vo.common.HistogramItem;
import com.workorder.ticket.persistence.dto.workorder.WorkOrderQueryDto;
import com.workorder.ticket.persistence.dto.workorder.WorkOrderWithCreatorDto;
import com.workorder.ticket.persistence.entity.WorkOrder;

public interface WorkOrderDao {
	int deleteByPrimaryKey(Long id);

	int insert(WorkOrder record);

	int insertSelective(WorkOrder record);

	WorkOrder getByPrimaryKey(Long id);

	WorkOrderWithCreatorDto getWithCreator(Long id);

	List<WorkOrderWithCreatorDto> getListByParam(
			WorkOrderQueryDto workOrderQueryDto);

	int getCountByParam(WorkOrderQueryDto workOrderQueryDto);

	int updateByPrimaryKeySelective(WorkOrder record);

	int updateByPrimaryKeyWithBLOBs(WorkOrder record);

	int updateByPrimaryKey(WorkOrder record);
	
	List<HistogramItem> statisticByDay(@Param("startTime") Date start,
			@Param("endTime") Date end);

	List<HistogramItem> statisticByWeek(@Param("startTime") Date start,
			@Param("endTime") Date end);

	List<HistogramItem> statisticByMonth(@Param("startTime") Date start,
			@Param("endTime") Date end);
}