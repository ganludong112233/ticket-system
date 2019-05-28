package com.workorder.ticket.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sowing.common.page.Page;
import com.sowing.common.response.ListResponse;
import com.workorder.ticket.controller.vo.common.OpLog;
import com.workorder.ticket.service.BizLogService;

/**
 * 操作日志管理
 * 
 * @author wzdong
 * @Date 2019年3月26日
 * @version 1.0
 */
@RestController
public class BizLogController {
	@Resource
	private BizLogService bizLogService;

	/**
	 * 获取操作日志
	 * 
	 * @return
	 */
	@GetMapping("/oplogs")
	public ListResponse<OpLog> getLogs(Page page) {
		return ListResponse.build(bizLogService.getOpLogs(page));
	}
}
