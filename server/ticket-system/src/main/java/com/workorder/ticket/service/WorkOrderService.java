package com.workorder.ticket.service;

import java.util.Date;
import java.util.List;

import com.workorder.ticket.controller.vo.common.FlowStepItem;
import com.workorder.ticket.controller.vo.common.HistogramItem;
import com.workorder.ticket.controller.vo.common.TicketOpLog;
import com.workorder.ticket.service.bo.workorder.WorkOrderBo;
import com.workorder.ticket.service.bo.workorder.WorkOrderEditBo;
import com.workorder.ticket.service.bo.workorder.WorkOrderQueryBo;

/**
 * 工单服务
 * 
 * @author wzdong
 * @Date 2019年3月4日
 * @version 1.0
 */
public interface WorkOrderService {
	/**
	 * 新建工单
	 * 
	 * @param initVo
	 * @return
	 */
	public void create(WorkOrderEditBo initBo);

	/**
	 * 编辑工单
	 * 
	 * @param editVo
	 * @return
	 */
	public void edit(WorkOrderEditBo editBo);

	/**
	 * 删除工单
	 * 
	 * @param editVo
	 * @return
	 */
	public void delete(Long workOrderId);

	/**
	 * 撤销工单
	 * 
	 * @param editVo
	 * @return
	 */
	public void revoke(Long workOrderId);

	/**
	 * 提交工单
	 * 
	 * @param workOrderId
	 * @return
	 */
	public void submit(Long workOrderId);

	/**
	 * 查询工单详情
	 * 
	 * @return
	 */
	public WorkOrderBo getById(Long workOrderId);

	/**
	 * 查询工单处理日志
	 * 
	 * @return
	 */
	public List<TicketOpLog> getOpLogs(Long workOrderId);

	/**
	 * 查询工单处理步骤
	 * 
	 * @return
	 */
	public List<FlowStepItem> getFlowSteps(Long workOrderId);

	/**
	 * 查询所有工单
	 * 
	 * @param queryVo
	 * @return
	 */
	public List<WorkOrderBo> queryAllList(WorkOrderQueryBo queryBo);

	/**
	 * 查询所有工单Count
	 * 
	 * @param queryVo
	 * @return
	 */
	public int queryAllListCount(WorkOrderQueryBo queryBo);

	/**
	 * 查询所有待处理的工单
	 * 
	 * @param queryVo
	 * @return
	 */
	public List<WorkOrderBo> queryWaitingList();

	/**
	 * 查询所有待处理的工单Count
	 * 
	 * @param queryVo
	 * @return
	 */
	public int queryWaitingListCount();

	/**
	 * 查询用户处理的工单历史
	 * 
	 * @param queryVo
	 */
	public List<WorkOrderBo> queryHistoryList(WorkOrderQueryBo queryBo);

	/**
	 * 查询用户处理的工单历史Count
	 * 
	 * @param queryVo
	 */
	public int queryHistoryListCount(WorkOrderQueryBo queryBo);

	/**
	 * 处理任务
	 * 
	 * @return
	 */
	public void completeTask(Long workOrderId, Boolean result, String comment);

	/**
	 * 按天统计工单
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<HistogramItem> statisticByDay(Date start, Date end);

}
