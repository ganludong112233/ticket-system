package com.workorder.ticket.common.enums;

/**
 * 用户状态
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public enum UserStatus {
	NORMAL((byte) 1, "正常"), DISABLE((byte) 2, "禁用");
	private byte type;
	private String desc;

	private UserStatus(byte type, String desc) {
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
