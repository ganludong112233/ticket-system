package com.workorder.ticket.common.enums;

public enum DeployType {
	// 类型 1 版本迭代 2 bugfix 3 代码优化
	VERSION_RELEASE((byte) 1, "版本迭代"), BUGFIX((byte) 2, "bugfix"), CODE_OPTIMIZE(
			(byte) 3, "代码优化");
	private byte type;
	private String desc;

	private DeployType(byte type, String desc) {
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
		for (DeployType val : values()) {
			if (val.getType() == value) {
				return val.getDesc();
			}
		}
		return null;
	}
}
