package com.workorder.ticket.common.enums;

public enum WorkOrderType {
	// 类型 1 SQL 2 Apollo 3 Nginx 4 其他
	SQL((byte) 1, "SQL"), Aplollo((byte) 2, "APOLLO"), Nginx((byte) 4, "Nginx"), Other(
			(byte) 4, "其他");
	private byte type;
	private String desc;

	private WorkOrderType(byte type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public byte getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDescByValue(byte value) {
		for (WorkOrderType val : values()) {
			if (val.getType() == value) {
				return val.getDesc();
			}
		}
		return null;
	}
}
