package com.workorder.ticket.controller.vo.user;

import com.sowing.common.page.Page;
import com.workorder.ticket.service.bo.user.UserQueryBo;

/**
 * 用户查询参数
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public class UserQueryVo extends Page {
	private static final long serialVersionUID = 5394978360792625390L;
	private Long groupId;
	private String realName;
	private String username;
	
	public UserQueryBo convertToUserQueryBo() {
		UserQueryBo userQueryBo = new UserQueryBo();
		userQueryBo.setUsername(this.username);
		userQueryBo.setRealname(this.realName);
		userQueryBo.setGroupId(this.groupId);
		if (getPageNum() != null && getPageNum() != null) {
			userQueryBo.setPage(new Page(getPageNum(), getPageSize()));
		}
		return userQueryBo;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
