package com.workorder.ticket.service.bo.user;

/**
 * 用户基础信息对象
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public class UserBaseBo {
	private Long userId;
	private String username;
	private String realname;

	public UserBaseBo() {
	}

	public UserBaseBo(Long userId, String username, String realname) {
		this.userId = userId;
		this.username = username;
		this.realname = realname;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

}
