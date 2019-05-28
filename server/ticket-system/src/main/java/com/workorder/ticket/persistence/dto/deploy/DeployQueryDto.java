package com.workorder.ticket.persistence.dto.deploy;

import java.util.List;

import com.sowing.common.page.Page;
import com.workorder.ticket.common.item.DateRange;

/**
 * 部署申请查询参数
 * 
 * @author wzdong
 * @Date 2019年3月25日
 * @version 1.0
 */
public class DeployQueryDto {

	private List<String> instanceIds;
	private Byte status;
	private String creator;
	private Long projectId;
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

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
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
