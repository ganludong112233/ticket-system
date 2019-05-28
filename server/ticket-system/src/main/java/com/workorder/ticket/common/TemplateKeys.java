package com.workorder.ticket.common;
/**
 * 模板的key
 * @author wzdong
 * @Date 2019年4月3日
 * @version 1.0
 */
public enum TemplateKeys {
	RELEASE_FIXBUG("deploy_fixbug"), 
	RELEASE_OPTIMIZE("deploy_optimize"), 
	RELEASE_VERSION("deploy_version"), 
	WORK_ORDER("work_order_process");

	private String key;

	private TemplateKeys(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public static boolean validKey(String key) {
		if(key == null){
			return false;
		}
		for (TemplateKeys k : values()) {
			if (k.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}
}
