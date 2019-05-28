package com.workorder.ticket.service.bo.user;

import java.util.List;

import com.sowing.common.page.Page;

/**
 * 用户查询BO
 * 
 * @author 76109
 *
 */
public class UserQueryBo {
	private List<Long> userIds;
	private String username;
	private String realname;
	private Long groupId;
	private Page page;

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
