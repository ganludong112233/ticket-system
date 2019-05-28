package com.workorder.ticket.common.enums;

/**
 * 用户类型
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public enum UserType {
	COMMON((byte) 1, "管理员"), SUPERADMIN((byte) 2, "普通用户");
	private byte type;
	private String desc;

	private UserType(byte type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public byte getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	public static boolean isSuperAdmin(byte t) {
		return SUPERADMIN.getType() == t;
	}
}
