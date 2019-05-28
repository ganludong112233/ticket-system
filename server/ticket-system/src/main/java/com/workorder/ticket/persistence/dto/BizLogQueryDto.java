package com.workorder.ticket.persistence.dto;

import com.sowing.common.page.Page;
import com.workorder.ticket.common.item.DateRange;

public class BizLogQueryDto {
	private String bizId;
	private Byte bizType;
	private String action;
	private DateRange createRange;
	private Page pageItem;

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public Byte getBizType() {
		return bizType;
	}

	public void setBizType(Byte bizType) {
		this.bizType = bizType;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public DateRange getCreateRange() {
		return createRange;
	}

	public void setCreateRange(DateRange createRange) {
		this.createRange = createRange;
	}

	public Page getPageItem() {
		return pageItem;
	}

	public void setPageItem(Page pageItem) {
		this.pageItem = pageItem;
	}

}
