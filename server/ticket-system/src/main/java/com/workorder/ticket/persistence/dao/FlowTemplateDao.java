package com.workorder.ticket.persistence.dao;

import com.workorder.ticket.persistence.entity.FlowTemplate;

public interface FlowTemplateDao {

	int insertSelective(FlowTemplate record);

	FlowTemplate getOneByCode(String code);
	
	int updateByPrimaryKeySelective(FlowTemplate record);

}