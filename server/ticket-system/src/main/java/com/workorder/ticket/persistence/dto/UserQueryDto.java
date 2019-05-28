package com.workorder.ticket.persistence.dto;

import java.util.List;

import com.sowing.common.page.Page;

/**
 * 分页查询user
 * 
 * @author 76109
 *
 */
public class UserQueryDto {
	private Long groupId;
	private List<Long> userIds;
	private String username;
	private String realname;
	private Page page;

	public UserQueryDto() {
	}

	public UserQueryDto(Long groupId, List<Long> userIds, String username,
			String realname, Page page) {
		this.groupId = groupId;
		this.userIds = userIds;
		this.username = username;
		this.realname = realname;
		this.page = page;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

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

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
