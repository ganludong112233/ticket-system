package com.workorder.ticket.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sowing.common.exception.ServiceException;
import com.workorder.ticket.common.TemplateKeys;
import com.workorder.ticket.persistence.dao.FlowTemplateDao;
import com.workorder.ticket.persistence.entity.FlowTemplate;
import com.workorder.ticket.remote.ActivityServiceRpc;
import com.workorder.ticket.service.TemplateService;

/**
 * 模板管理服务
 * 
 * @author wzdong
 * @Date 2019年4月3日
 * @version 1.0
 */
@Service
public class TemplateServiceImpl implements TemplateService {
	@Resource
	private ActivityServiceRpc activityServiceRpc;
	@Resource
	private FlowTemplateDao flowTemplateDao;

	/**
	 * 部署模板
	 */
	public String deploy(String key, String name, String description) {
		if (!TemplateKeys.validKey(key)) {
			throw ServiceException.commonException("参数不合法！");
		}
		String flowEngineDefinitionId = activityServiceRpc.deploy(key);
		flowEngineDefinitionId = flowEngineDefinitionId.replaceAll("\"", "");
		
		FlowTemplate flowTemplate = flowTemplateDao.getOneByCode(key);
		if (flowTemplate != null) {
			flowTemplate.setFlowEngineDefinitionId(flowEngineDefinitionId);
			flowTemplateDao.updateByPrimaryKeySelective(flowTemplate);
		} else {
			flowTemplate = new FlowTemplate();
			flowTemplate.setFlowEngineDefinitionId(flowEngineDefinitionId);
			flowTemplate.setName(name);
			flowTemplate.setDescription(description);
			flowTemplateDao.insertSelective(flowTemplate);
		}
		return flowEngineDefinitionId;
	}
}
