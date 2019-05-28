package com.workorder.ticket.persistence.dto.workorder;

import com.workorder.ticket.persistence.entity.WorkOrder;

/**
 * 工单信息（包含操作人姓名）Dto
 * 
 * @author wzdong
 * @Date 2019年3月6日
 * @version 1.0
 */
public class WorkOrderWithCreatorDto extends WorkOrder {

	private String createRealName;
	private String createUsername;

	public String getCreateRealName() {
		return createRealName;
	}

	public void setCreateRealName(String createRealName) {
		this.createRealName = createRealName;
	}

	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

}
