package com.workorder.ticket.persistence.dto.workorder;

import java.util.List;

import com.sowing.common.page.Page;
import com.workorder.ticket.common.item.DateRange;

/**
 * 查询参数Dto
 * 
 * @author wzdong
 * @Date 2019年3月6日
 * @version 1.0
 */
public class WorkOrderQueryDto {
	private List<String> instanceIds;
	private Byte status;
	private String creator;
	private String title;
	private DateRange createRange;
	private DateRange submitRange;
	private Page pageItem;

	public List<String> getInstanceIds() {
		return instanceIds;
	}

	public void setInstanceIds(List<String> instanceIds) {
		this.instanceIds = instanceIds;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public DateRange getCreateRange() {
		return createRange;
	}

	public void setCreateRange(DateRange createRange) {
		this.createRange = createRange;
	}

	public DateRange getSubmitRange() {
		return submitRange;
	}

	public void setSubmitRange(DateRange submitRange) {
		this.submitRange = submitRange;
	}

	public Page getPageItem() {
		return pageItem;
	}

	public void setPageItem(Page pageItem) {
		this.pageItem = pageItem;
	}

}
