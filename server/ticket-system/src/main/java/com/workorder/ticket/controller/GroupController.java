package com.workorder.ticket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sowing.common.response.ListResponse;
import com.workorder.ticket.controller.vo.group.GroupListVo;

/**
 * 分组管理
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
@RestController
public class GroupController {
	@GetMapping("/group/list")
	public ListResponse<GroupListVo> queryList() {
		return ListResponse.build(null);
	}
}
