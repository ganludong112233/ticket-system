package com.workorder.ticket.controller.vo.user;

import com.workorder.ticket.common.enums.UserStatus;
import com.workorder.ticket.service.bo.user.UserEditBo;

/**
 * 编辑用户信息
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public class UserEditVo {
	private Long id;
	private String username;
	private String realname;
	private String email;
	private String mobile;
	private Byte type;
	private Long groupId;
	private Boolean isGroupLeader;
	
	public UserEditBo convertToUserEditBo() {
		UserEditBo edit = new UserEditBo();
		edit.setId(this.id);
		edit.setEmail(this.email);
		edit.setGroupId(this.groupId);
		edit.setIsGroupLeader(this.isGroupLeader);
		edit.setMobile(this.mobile);
		edit.setRealname(this.realname);
		edit.setType(this.type);
		edit.setUsername(this.username);
		edit.setStatus(UserStatus.NORMAL.getType());
		return edit;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Boolean getIsGroupLeader() {
		return isGroupLeader;
	}

	public void setIsGroupLeader(Boolean isGroupLeader) {
		this.isGroupLeader = isGroupLeader;
	}

}
