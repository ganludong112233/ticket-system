package com.workorder.ticket.common.enums;

/**
 * 
 * @author wzdong
 * @Date 2019年3月6日
 * @version 1.0
 */
public enum WorkOrderOp {
	// 操作 1 新建 2 提交 3 编辑 4 同意 5 拒绝 6 重新提交 7 完成 8 撤销
	NEW("新建", null), 
	SUBMIT("提交", WorkOrderStatus.PROCESSING), 
	EDIT("编辑", null), 
	AGREE("同意", null), 
	REFUSED("拒绝",WorkOrderStatus.REFUSED), 
	RESUBMIT("重新提交",WorkOrderStatus.PROCESSING), 
	COMPLETE("完成",WorkOrderStatus.COMPLETE), 
	REVOKE("撤销", WorkOrderStatus.REVOKE);

	private String op;
	private WorkOrderStatus targetStatus;

	private WorkOrderOp(String op, WorkOrderStatus targetStatus) {
		this.op = op;
		this.targetStatus = targetStatus;
	}

	public String getValue() {
		return this.op;
	}

	public WorkOrderStatus getTargetStatus() {
		return this.targetStatus;
	}
}
