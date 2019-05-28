package com.workorder.ticket.common.enums;

/**
 * 部署申请状态
 * 
 * @author wzdong
 * @Date 2019年3月25日
 * @version 1.0
 */
public enum DeployStatus {
	// 状态 1 新建 2 审批中 3 已拒绝 4 审批完成 5 撤销
	NEW((byte) 1, "新建"), PROCESSING((byte) 2, "处理中"), REFUSED((byte) 3, "已拒绝"), COMPLETE(
			(byte) 4, "处理完成"), REVOKE((byte) 5, "撤销");
	private byte type;
	private String desc;

	private DeployStatus(byte type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public byte getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
