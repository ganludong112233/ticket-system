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
import com.workorder.ticket.controller.convertor.DeployControllerConvertor;
import com.workorder.ticket.controller.vo.common.FlowStepItem;
import com.workorder.ticket.controller.vo.common.HistogramItem;
import com.workorder.ticket.controller.vo.common.TicketOpLog;
import com.workorder.ticket.controller.vo.deploy.DeployEditVo;
import com.workorder.ticket.controller.vo.deploy.DeployInfoVo;
import com.workorder.ticket.controller.vo.deploy.DeployPageVo;
import com.workorder.ticket.controller.vo.deploy.DeployQueryVo;
import com.workorder.ticket.service.DeployService;
import com.workorder.ticket.service.bo.deploy.DeployBo;
import com.workorder.ticket.service.bo.deploy.DeployEditBo;
import com.workorder.ticket.service.bo.deploy.DeployQueryBo;

/**
 * 部署管理
 * 
 * @author wzdong
 * @Date 2019年3月25日
 * @version 1.0
 */
@RestController
public class DeployController {
	@Resource
	private DeployService deployService;

	/**
	 * 新建工单
	 * 
	 * @param initVo
	 * @return
	 */
	@PostMapping("/deploy/create")
	public ResponseBase create(@RequestBody DeployEditVo initVo) {
		DeployEditBo deployEditBo = DeployControllerConvertor
				.buildDeployEditBo(initVo);
		deployService.create(deployEditBo);
		return ResponseBase.success();
	}

	/**
	 * 编辑工单
	 * 
	 * @param editVo
	 * @return
	 */
	@PostMapping("/deploy/edit")
	public ResponseBase edit(@RequestBody DeployEditVo editVo) {
		DeployEditBo DeployEditBo = DeployControllerConvertor
				.buildDeployEditBo(editVo);
		deployService.edit(DeployEditBo);
		return ResponseBase.success();
	}

	/**
	 * 查询可编辑信息
	 * 
	 * @return
	 */
	@GetMapping("/deploy/editInfo")
	public SimpleResponse<DeployEditVo> getEditInfo(
			@RequestParam("deployId") Long deployId) {
		DeployBo DeployBo = deployService.getById(deployId);
		return SimpleResponse.build(DeployControllerConvertor
				.buildDeployEditVo(DeployBo));
	}

	/**
	 * 删除发版申请
	 * 
	 * @param editVo
	 * @return
	 */
	@PostMapping("/deploy/delete")
	public ResponseBase delete(@RequestParam("deployId") Long deployId) {
		deployService.delete(deployId);
		return ResponseBase.success();
	}

	/**
	 * 撤销发版申请
	 * 
	 * @param editVo
	 * @return
	 */
	@PostMapping("/deploy/revoke")
	public ResponseBase revoke(@RequestParam("deployId") Long deployId) {
		deployService.revoke(deployId);
		return ResponseBase.success();
	}

	/**
	 * 提交发版申请
	 * 
	 * @param DeployId
	 * @return
	 */
	@PostMapping("/deploy/submit")
	public ResponseBase submit(@RequestParam("deployId") Long deployId) {
		deployService.submit(deployId);
		return ResponseBase.success();
	}

	/**
	 * 处理任务
	 * 
	 * @return
	 */
	@PostMapping("/deploy/completeTask")
	public ResponseBase completeTask(@RequestParam("deployId") Long deployId,
			@RequestParam("result") Boolean result,
			@RequestParam(value = "comment", required = false) String comment) {
		deployService.completeTask(deployId, result, comment);
		return ResponseBase.success();
	}

	/**
	 * 查询发版申请详情
	 * 
	 * @return
	 */
	@GetMapping("/deploy/getInfoById")
	public SimpleResponse<DeployInfoVo> getById(
			@RequestParam("deployId") Long deployId) {
		DeployBo DeployBo = deployService.getById(deployId);
		return SimpleResponse.build(DeployControllerConvertor
				.buildDeployInfoVo(DeployBo));
	}

	/**
	 * 查询发版申请处理日志
	 * 
	 * @return
	 */
	@GetMapping("/deploy/getOpLogs")
	public ListResponse<TicketOpLog> getOpLogs(
			@RequestParam("deployId") Long deployId) {
		return ListResponse.build(deployService.getOpLogs(deployId));
	}

	/**
	 * 查询发版申请处理步骤
	 * 
	 * @return
	 */
	@GetMapping("/deploy/getFlowSteps")
	public ListResponse<FlowStepItem> getFlowSteps(
			@RequestParam("deployId") Long deployId) {
		return ListResponse.build(deployService.getFlowSteps(deployId));
	}

	/**
	 * 查询所有发版申请
	 * 
	 * @param queryVo
	 * @return
	 */
	@GetMapping("/deploy/queryList")
	public PageResponse<DeployPageVo> queryAllList(DeployQueryVo queryVo) {
		DeployQueryBo queryBo = DeployControllerConvertor
				.buildDeployQueryBo(queryVo);
		List<DeployBo> list = deployService.queryAllList(queryBo);
		int totalCount = deployService.queryAllListCount(queryBo);
		queryBo.getPageItem().setTotalCount(totalCount);
		if (CollectionUtils.isEmpty(list)) {
			return PageResponse.build(Collections.emptyList(),
					queryBo.getPageItem());
		}
		List<DeployPageVo> resultData = new ArrayList<DeployPageVo>();
		list.forEach(item -> resultData.add(DeployControllerConvertor
				.buildDeployPageVo(item)));
		return PageResponse.build(resultData, queryBo.getPageItem());
	}

	/**
	 * 查询所有待处理的发版申请
	 * 
	 * @param queryVo
	 * @return
	 */
	@GetMapping("/deploy/queryWaitingList")
	public ListResponse<DeployPageVo> queryWaitingList() {
		List<DeployBo> list = deployService.queryWaitingList();

		if (CollectionUtils.isEmpty(list)) {
			return ListResponse.build(Collections.emptyList());
		}
		List<DeployPageVo> resultData = new ArrayList<DeployPageVo>();
		list.forEach(item -> resultData.add(DeployControllerConvertor
				.buildDeployPageVo(item)));
		return ListResponse.build(resultData);
	}

	/**
	 * 查询所有待处理的发版申请Count
	 * 
	 * @param queryVo
	 * @return
	 */
	@GetMapping("/deploy/queryWaitingListCount")
	public SimpleResponse<Integer> queryWaitingListCount() {
		return SimpleResponse.build(deployService.queryWaitingListCount());
	}

	/**
	 * 查询用户处理的发版申请历史
	 * 
	 * @param queryVo
	 */
	@GetMapping("/deploy/queryHistoryList")
	public PageResponse<DeployQueryVo> queryHistoryList(DeployQueryVo queryVo) {
		DeployQueryBo queryBo = DeployControllerConvertor
				.buildDeployQueryBo(queryVo);
		deployService.queryHistoryList(queryBo);
		return PageResponse.build(null);
	}

	@GetMapping("/deploy/statistic/{rangeType}")
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
		return ListResponse.build(deployService.statisticByDay(start, end));
	}

}
