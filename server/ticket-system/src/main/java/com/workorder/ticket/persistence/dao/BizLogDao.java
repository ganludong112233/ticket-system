package com.workorder.ticket.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workorder.ticket.controller.vo.common.OpLog;
import com.workorder.ticket.persistence.dto.BizLogQueryDto;
import com.workorder.ticket.persistence.entity.BizLog;

public interface BizLogDao {
	int deleteByPrimaryKey(Long id);

	int insert(BizLog record);

	int insertSelective(BizLog record);

	BizLog selectByPrimaryKey(Long id);

	List<BizLog> getList(@Param("bizId") Long bizId,
			@Param("bizType") Integer bizType);

	int updateByPrimaryKeySelective(BizLog record);

	int updateByPrimaryKey(BizLog record);

	List<OpLog> getOpLogList(BizLogQueryDto queryDto);
}