package com.workorder.ticket.controller.convertor;

import com.sowing.common.page.Page;
import com.workorder.ticket.common.enums.WorkOrderType;
import com.workorder.ticket.common.item.DateRange;
import com.workorder.ticket.controller.vo.workorder.WorkOrderEditVo;
import com.workorder.ticket.controller.vo.workorder.WorkOrderInfoVo;
import com.workorder.ticket.controller.vo.workorder.WorkOrderPageVo;
import com.workorder.ticket.controller.vo.workorder.WorkOrderQueryVo;
import com.workorder.ticket.service.bo.workorder.WorkOrderBo;
import com.workorder.ticket.service.bo.workorder.WorkOrderEditBo;
import com.workorder.ticket.service.bo.workorder.WorkOrderQueryBo;

/**
 * 工单VO转换器
 * 
 * @author wzdong
 * @Date 2019年3月7日
 * @version 1.0
 */
public class WorkOrderControllerConvertor {

	public static WorkOrderEditBo buildWorkOrderBo(WorkOrderEditVo editVo) {
		WorkOrderEditBo editBo = new WorkOrderEditBo();

		if (editVo != null) {
			editBo.setId(editVo.getId());
			editBo.setTitle(editVo.getTitle());
			editBo.setType(editVo.getType());
			editBo.setDescription(editVo.getDescription());
			editBo.setComment(editVo.getComment());
			editBo.setContent(editVo.getContent());
		}

		return editBo;
	}

	public static WorkOrderEditVo buildWorkOrderEditVo(WorkOrderBo bo) {
		WorkOrderEditVo editVo = new WorkOrderEditVo();

		if (bo != null) {
			editVo.setId(bo.getId());
			editVo.setTitle(bo.getTitle());
			editVo.setType(bo.getType());
			editVo.setDescription(bo.getDescription());
			editVo.setComment(bo.getComment());
			editVo.setContent(bo.getContent());
		}

		return editVo;
	}

	public static WorkOrderInfoVo buildWorkOrderInfoVo(WorkOrderBo bo) {
		WorkOrderInfoVo workOrderInfo = new WorkOrderInfoVo();

		if (bo != null) {
			workOrderInfo.setId(bo.getId());
			workOrderInfo.setTitle(bo.getTitle());
			workOrderInfo.setType(bo.getType());
			workOrderInfo
					.setTypeDesc(WorkOrderType.getDescByValue(bo.getType()));
			workOrderInfo.setDescription(bo.getDescription());
			workOrderInfo.setComment(bo.getComment());
			workOrderInfo.setContent(bo.getContent());
		}

		return workOrderInfo;
	}

	public static WorkOrderQueryBo buildWorkOrderQueryBo(WorkOrderQueryVo vo) {
		WorkOrderQueryBo queryBo = new WorkOrderQueryBo();
		queryBo.setStatus(vo.getStatus());
		queryBo.setCreator(vo.getSubmitUser());
		queryBo.setTitle(vo.getTitle());
		if (vo.getStartTime() != null || vo.getEndTime() != null) {
			queryBo.setCreateRange(new DateRange(vo.getStartTime(), vo
					.getEndTime()));
		}
		if (vo.getPageSize() != null && vo.getPageNum() != null) {
			queryBo.setPageItem(new Page(vo.getPageNum(), vo.getPageSize()));
		}
		return queryBo;
	}

	public static WorkOrderPageVo buildWorkOrderPageVo(WorkOrderBo bo) {
		WorkOrderPageVo vo = new WorkOrderPageVo();

		vo.setId(bo.getId());
		vo.setTitle(bo.getTitle());
		vo.setType(bo.getType());
		vo.setStatus(bo.getStatus());
		vo.setComment(bo.getComment());

		if (bo.getCurrentProcessor() != null) {
			vo.setCurrentProcessor(bo.getCurrentProcessor().getRealname());
		}
		if (bo.getCreateUser() != null) {
			vo.setCreatorId(bo.getCreateUser().getUserId());
			vo.setCreator(bo.getCreateUser().getRealname());
		}
		vo.setCreateTime(bo.getCreateTime());
		vo.setSubmitTime(bo.getSubmitTime());
		return vo;
	}
}
