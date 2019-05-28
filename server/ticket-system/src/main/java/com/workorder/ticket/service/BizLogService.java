package com.workorder.ticket.service;

import java.util.List;

import com.sowing.common.page.Page;
import com.workorder.ticket.controller.vo.common.OpLog;
import com.workorder.ticket.controller.vo.common.TicketOpLog;

public interface BizLogService {
	public void saveWorkOrderLog(Long workOrderId, String title, String op,
			String comment, Long processor);

	public void saveDeployLog(Long deployId, String title, String op,
			String comment, Long processor);

	/**
	 * 获取操作日志
	 * 
	 * @return
	 */
	public List<OpLog> getOpLogs(Page pageItem);

	/**
	 * 查询工单操作日志
	 * 
	 * @return
	 */
	public List<TicketOpLog> getOpLogs(Long workOrderId, Integer type);
}
