package com.workorder.ticket.controller.vo.common;

import java.util.Date;

/**
 * 工单处理日志
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public class TicketOpLog {
	private String processor;// 处理人
	private String op; // 操作
	private String comment;// 备注
	private Date opTime;// 处理时间

	public TicketOpLog() {
	}

	public TicketOpLog(String processor, String op, String comment, Date opTime) {
		this.processor = processor;
		this.comment = comment;
		this.op = op;
		this.opTime = opTime;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getProcessTime() {
		return opTime;
	}

	public void setProcessTime(Date processTime) {
		this.opTime = processTime;
	}

}
