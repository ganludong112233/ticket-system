package com.workorder.ticket.controller.vo.common;

import java.util.Date;

/**
 * 操作日志
 * 
 * @author wzdong
 * @Date 2019年3月11日
 * @version 1.0
 */
public class OpLog {
	private String operator;
	private String opTarget;// 操作目标
	private String op;// 操作
	private Date opTime;// 操作时间

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOpTarget() {
		return opTarget;
	}

	public void setOpTarget(String opTarget) {
		this.opTarget = opTarget;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

}
