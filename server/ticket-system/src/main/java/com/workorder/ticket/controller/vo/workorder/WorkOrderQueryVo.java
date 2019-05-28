package com.workorder.ticket.controller.vo.workorder;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.sowing.common.page.Page;

/**
 * 工单查询参数
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public class WorkOrderQueryVo extends Page {
	private static final long serialVersionUID = 1L;
	private Byte status;
	private String submitUser;
	private String title;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getSubmitUser() {
		return submitUser;
	}

	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
