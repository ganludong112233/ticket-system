package com.workorder.ticket.service.convertor;

import com.workorder.ticket.persistence.dto.workorder.WorkOrderQueryDto;
import com.workorder.ticket.persistence.dto.workorder.WorkOrderWithCreatorDto;
import com.workorder.ticket.persistence.entity.User;
import com.workorder.ticket.persistence.entity.WorkOrder;
import com.workorder.ticket.service.bo.user.UserBaseBo;
import com.workorder.ticket.service.bo.workorder.WorkOrderBo;
import com.workorder.ticket.service.bo.workorder.WorkOrderEditBo;
import com.workorder.ticket.service.bo.workorder.WorkOrderQueryBo;

/**
 * 工单对象转换器
 * 
 * @author wzdong
 * @Date 2019年3月6日
 * @version 1.0
 */
public class WorkOrderServiceConvertor {
	public static WorkOrder buildWorkOrder(WorkOrderEditBo editBo) {
		WorkOrder wo = new WorkOrder();
		wo.setId(editBo.getId());
		wo.setTitle(editBo.getTitle());
		wo.setType(editBo.getType());
		wo.setDescription(editBo.getDescription());
		wo.setComment(editBo.getComment());
		wo.setContent(editBo.getContent());
		return wo;
	}

	public static WorkOrderBo buildWorkOrderBo(
			WorkOrderWithCreatorDto workOrder, User currentProcessor) {
		WorkOrderBo bo = new WorkOrderBo();
		bo.setId(workOrder.getId());
		bo.setTitle(workOrder.getTitle());
		bo.setType(workOrder.getType());
		bo.setDescription(workOrder.getDescription());
		bo.setStatus(workOrder.getStatus());
		bo.setFlowEngineDefinitionId(workOrder.getFlowEngineDefinitionId());
		bo.setFlowEngineInstanceId(workOrder.getFlowEngineInstanceId());
		bo.setComment(workOrder.getComment());
		bo.setCreateTime(workOrder.getCreateTime());
		bo.setSubmitTime(workOrder.getSubmitTime());
		bo.setContent(workOrder.getContent());
		bo.setCreateUser(new UserBaseBo(workOrder.getCreateId(), workOrder
				.getCreateUsername(), workOrder.getCreateRealName()));
		if (currentProcessor != null) {
			bo.setCurrentProcessor(new UserBaseBo(currentProcessor.getId(),
					currentProcessor.getUsername(), currentProcessor
							.getRealname()));
		}
		return bo;
	}

	public static WorkOrderQueryDto buildWorkOrderQueryDto(
			WorkOrderQueryBo queryBo) {
		WorkOrderQueryDto dto = new WorkOrderQueryDto();
		dto.setStatus(queryBo.getStatus());
		dto.setTitle(queryBo.getTitle());
		dto.setCreator(queryBo.getCreator());
		dto.setCreateRange(queryBo.getCreateRange());
		dto.setSubmitRange(queryBo.getSubmitRange());
		dto.setPageItem(queryBo.getPageItem());
		return dto;
	}
}
