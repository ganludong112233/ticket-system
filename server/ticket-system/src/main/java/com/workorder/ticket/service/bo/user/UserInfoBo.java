package com.workorder.ticket.service.bo.user;

import com.workorder.ticket.service.bo.group.GroupBaseBo;

/**
 * 用户信息详情
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public class UserInfoBo extends UserSimpleBo {
	private GroupBaseBo group;
	private Boolean isGroupLeader;// 组负责人

	public GroupBaseBo getGroup() {
		return group;
	}

	public void setGroup(GroupBaseBo group) {
		this.group = group;
	}

	public Boolean getIsGroupLeader() {
		return isGroupLeader;
	}

	public void setIsGroupLeader(Boolean isGroupLeader) {
		this.isGroupLeader = isGroupLeader;
	}

}
