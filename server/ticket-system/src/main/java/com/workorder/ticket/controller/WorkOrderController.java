package com.workorder.ticket.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sowing.common.response.ListResponse;
import com.sowing.common.response.PageResponse;
import com.sowing.common.response.ResponseBase;
import com.sowing.common.response.SimpleResponse;
import com.workorder.ticket.common.utils.DateUtils;
import com.workorder.ticket.controller.convertor.WorkOrderControllerConvertor;
import com.workorder.ticket.controller.vo.common.FlowStepItem;
import com.workorder.ticket.controller.vo.common.HistogramItem;
import com.workorder.ticket.controller.vo.common.TicketOpLog;
import com.workorder.ticket.controller.vo.workorder.WorkOrderEditVo;
import com.workorder.ticket.controller.vo.workorder.WorkOrderInfoVo;
import com.workorder.ticket.controller.vo.workorder.WorkOrderPageVo;
import com.workorder.ticket.controller.vo.workorder.WorkOrderQueryVo;
import com.workorder.ticket.service.WorkOrderService;
import com.workorder.ticket.service.bo.workorder.WorkOrderBo;
import com.workorder.ticket.service.bo.workorder.WorkOrderEditBo;
import com.workorder.ticket.service.bo.workorder.WorkOrderQueryBo;

/**
 * 工单管理
 * 
 * @author wzdong
 * @Date 2019年3月4日
 * @version 1.0
 */
@RestController
public class WorkOrderController {

	@Resource
	private WorkOrderService workOrderService;

	/**
	 * 新建工单
	 * 
	 * @param initVo
	 * @return
	 */
	@PostMapping("/wo/create")
	public ResponseBase create(@RequestBody WorkOrderEditVo initVo) {
		WorkOrderEditBo workOrderEditBo = WorkOrderControllerConvertor
				.buildWorkOrderBo(initVo);
		workOrderService.create(workOrderEditBo);
		return ResponseBase.success();
	}

	/**
	 * 编辑工单
	 * 
	 * @param editVo
	 * @return
	 */
	@PostMapping("/wo/edit")
	public ResponseBase edit(@RequestBody WorkOrderEditVo editVo) {
		WorkOrderEditBo workOrderEditBo = WorkOrderControllerConvertor
				.buildWorkOrderBo(editVo);
		workOrderService.edit(workOrderEditBo);
		return ResponseBase.success();
	}

	/**
	 * 查询可编辑信息
	 * 
	 * @return
	 */
	@GetMapping("/wo/editInfo")
	public SimpleResponse<WorkOrderEditVo> getEditInfo(
			@RequestParam("workOrderId") Long workOrderId) {
		WorkOrderBo workOrderBo = workOrderService.getById(workOrderId);
		return SimpleResponse.build(WorkOrderControllerConvertor
				.buildWorkOrderEditVo(workOrderBo));
	}

	/**
	 * 删除工单
	 * 
	 * @param editVo
	 * @return
	 */
	@PostMapping("/wo/delete")
	public ResponseBase delete(@RequestParam("workOrderId") Long workOrderId) {
		workOrderService.delete(workOrderId);
		return ResponseBase.success();
	}

	/**
	 * 撤销工单
	 * 
	 * @param editVo
	 * @return
	 */
	@PostMapping("/wo/revoke")
	public ResponseBase revoke(@RequestParam("workOrderId") Long workOrderId) {
		workOrderService.revoke(workOrderId);
		return ResponseBase.success();
	}

	/**
	 * 提交工单
	 * 
	 * @param workOrderId
	 * @return
	 */
	@PostMapping("/wo/submit")
	public ResponseBase submit(@RequestParam("workOrderId") Long workOrderId) {
		workOrderService.submit(workOrderId);
		return ResponseBase.success();
	}

	/**
	 * 处理任务
	 * 
	 * @return
	 */
	@PostMapping("/wo/completeTask")
	public ResponseBase completeTask(
			@RequestParam("workOrderId") Long workOrderId,
			@RequestParam("result") Boolean result,
			@RequestParam(value = "comment", required = false) String comment) {
		workOrderService.completeTask(workOrderId, result, comment);
		return ResponseBase.success();
	}

	/**
	 * 查询工单详情
	 * 
	 * @return
	 */
	@GetMapping("/wo/getInfoById")
	public SimpleResponse<WorkOrderInfoVo> getById(
			@RequestParam("workOrderId") Long workOrderId) {
		WorkOrderBo workOrderBo = workOrderService.getById(workOrderId);
		return SimpleResponse.build(WorkOrderControllerConvertor
				.buildWorkOrderInfoVo(workOrderBo));
	}

	/**
	 * 查询工单处理日志
	 * 
	 * @return
	 */
	@GetMapping("/wo/getOpLogs")
	public ListResponse<TicketOpLog> getOpLogs(
			@RequestParam("workOrderId") Long workOrderId) {
		return ListResponse.build(workOrderService.getOpLogs(workOrderId));
	}

	/**
	 * 查询工单处理步骤
	 * 
	 * @return
	 */
	@GetMapping("/wo/getFlowSteps")
	public ListResponse<FlowStepItem> getFlowSteps(
			@RequestParam("workOrderId") Long workOrderId) {
		return ListResponse.build(workOrderService.getFlowSteps(workOrderId));
	}

	/**
	 * 查询所有工单
	 * 
	 * @param queryVo
	 * @return
	 */
	@GetMapping("/wo/queryList")
	public PageResponse<WorkOrderPageVo> queryAllList(WorkOrderQueryVo queryVo) {
		WorkOrderQueryBo queryBo = WorkOrderControllerConvertor
				.buildWorkOrderQueryBo(queryVo);
		List<WorkOrderBo> list = workOrderService.queryAllList(queryBo);
		int totalCount = workOrderService.queryAllListCount(queryBo);
		queryBo.getPageItem().setTotalCount(totalCount);
		if (CollectionUtils.isEmpty(list)) {
			return PageResponse.build(Collections.emptyList(),
					queryBo.getPageItem());
		}
		List<WorkOrderPageVo> resultData = new ArrayList<WorkOrderPageVo>();
		list.forEach(item -> resultData.add(WorkOrderControllerConvertor
				.buildWorkOrderPageVo(item)));
		return PageResponse.build(resultData, queryBo.getPageItem());
	}

	/**
	 * 查询所有待处理的工单
	 * 
	 * @param queryVo
	 * @return
	 */
	@GetMapping("/wo/queryWaitingList")
	public ListResponse<WorkOrderPageVo> queryWaitingList() {
		List<WorkOrderBo> list = workOrderService.queryWaitingList();

		if (CollectionUtils.isEmpty(list)) {
			return ListResponse.build(Collections.emptyList());
		}
		List<WorkOrderPageVo> resultData = new ArrayList<WorkOrderPageVo>();
		list.forEach(item -> resultData.add(WorkOrderControllerConvertor
				.buildWorkOrderPageVo(item)));
		return ListResponse.build(resultData);
	}

	/**
	 * 查询所有待处理的工单Count
	 * 
	 * @param queryVo
	 * @return
	 */
	@GetMapping("/wo/queryWaitingListCount")
	public SimpleResponse<Integer> queryWaitingListCount() {
		return SimpleResponse.build(workOrderService.queryWaitingListCount());
	}

	/**
	 * 查询用户处理的工单历史
	 * 
	 * @param queryVo
	 */
	@GetMapping("/wo/queryHistoryList")
	public PageResponse<WorkOrderPageVo> queryHistoryList(
			WorkOrderQueryVo queryVo) {
		WorkOrderQueryBo queryBo = WorkOrderControllerConvertor
				.buildWorkOrderQueryBo(queryVo);
		workOrderService.queryHistoryList(queryBo);
		return PageResponse.build(null);
	}

	@GetMapping("/wo/statistic/{rangeType}")
	public ListResponse<HistogramItem> statistic(
			@PathVariable("rangeType") String rangeType) {
		Date start = null;
		Date end = null;
		Date now = new Date();
		if ("week".equals(rangeType)) {
			start = DateUtils.getBeginOfWeek(now);
			end = DateUtils.getEndOfWeek(now);
		} else if ("month".equals(rangeType)) {
			start = DateUtils.getBeginOfMonth(now);
			end = DateUtils.getEndOfMonth(now);
		} else {
			return ListResponse.build(null);
		}
		return ListResponse.build(workOrderService.statisticByDay(start, end));
	}
}
