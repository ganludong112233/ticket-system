package com.workorder.ticket.persistence.dto;

import com.workorder.ticket.persistence.entity.User;

/**
 * 用户信息详情
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public class UserInfoDto extends User {
	private String groupName;
	private Long userId;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
