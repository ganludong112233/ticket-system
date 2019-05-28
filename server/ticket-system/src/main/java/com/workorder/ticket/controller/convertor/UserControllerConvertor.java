package com.workorder.ticket.controller.convertor;

import com.workorder.ticket.controller.vo.user.UserEditVo;
import com.workorder.ticket.controller.vo.user.UserPageVo;
import com.workorder.ticket.service.bo.user.UserInfoBo;

public class UserControllerConvertor {

	public static UserPageVo buildUserListVo(UserInfoBo userBo) {
		if (userBo == null) {
			return null;
		}
		UserPageVo userPageVo = new UserPageVo();
		userPageVo.setEmail(userBo.getEmail());
		userPageVo.setGroup(userBo.getGroup().getName());
		userPageVo.setId(userBo.getUserId());
		userPageVo.setMobile(userBo.getMobile());
		userPageVo.setRealname(userBo.getRealname());
		userPageVo.setStatus(userBo.getStatus().toString());
		userPageVo.setType(userBo.getType().toString());
		userPageVo.setUsername(userBo.getUsername());
		return userPageVo;
	}

	public static UserEditVo buildUserEditVo(UserInfoBo userInfo) {
		if (userInfo == null) {
			return null;
		}
		UserEditVo userEditVo = new UserEditVo();
		userEditVo.setId(userInfo.getUserId());
		userEditVo.setEmail(userInfo.getEmail());
		userEditVo.setMobile(userInfo.getMobile());
		userEditVo.setRealname(userInfo.getRealname());
		userEditVo.setType(userInfo.getType());
		userEditVo.setUsername(userInfo.getUsername());
		userEditVo.setIsGroupLeader(userInfo.getIsGroupLeader());
		if (userInfo.getGroup() != null) {
			userEditVo.setGroupId(userInfo.getGroup().getId());
		}
		return userEditVo;
	}
	
}
