package com.workorder.ticket.controller.vo.deploy;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.sowing.common.page.Page;

/**
 * 部署请求查询参数
 * 
 * @author wzdong
 * @Date 2019年3月25日
 * @version 1.0
 */
public class DeployQueryVo extends Page {
	private static final long serialVersionUID = 7116102675374129312L;

	private Byte status;
	private String submitor;
	private Long projectId;
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


	public String getSubmitor() {
		return submitor;
	}

	public void setSubmitor(String submitor) {
		this.submitor = submitor;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
