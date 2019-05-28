package com.workorder.ticket.service.convertor;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.workorder.ticket.common.enums.UserStatus;
import com.workorder.ticket.persistence.dto.UserInfoDto;
import com.workorder.ticket.persistence.dto.UserQueryDto;
import com.workorder.ticket.persistence.entity.Group;
import com.workorder.ticket.persistence.entity.User;
import com.workorder.ticket.service.bo.group.GroupBaseBo;
import com.workorder.ticket.service.bo.user.UserEditBo;
import com.workorder.ticket.service.bo.user.UserInfoBo;
import com.workorder.ticket.service.bo.user.UserQueryBo;

/**
 * 用户对像转换器
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public class UserServiceConvertor {
	static Logger logger = LoggerFactory.getLogger(UserServiceConvertor.class);

	public static User userConvertor(UserEditBo userEditBo) {
		User user = new User();
		user.setId(userEditBo.getId());
		user.setEmail(userEditBo.getEmail());
		user.setGroupId(userEditBo.getGroupId());
		user.setMobile(userEditBo.getMobile());
		user.setRealname(userEditBo.getRealname());
		user.setType(userEditBo.getType());
		user.setUsername(userEditBo.getUsername());
		user.setStatus(UserStatus.NORMAL.getType());
		user.setCreateTime(new Date());
		logger.info("conver userEditBo to User:{}", JSON.toJSONString(user));
		return user;

	}

	public static UserQueryDto buildUserQueryDto(UserQueryBo userQueryBo) {
		if (userQueryBo == null) {
			return new UserQueryDto();
		}
		UserQueryDto dto = new UserQueryDto(userQueryBo.getGroupId(),
				userQueryBo.getUserIds(), userQueryBo.getUsername(),
				userQueryBo.getRealname(), userQueryBo.getPage());
		logger.info("conver userQueryBo to UserQueryDto:{}",
				JSON.toJSONString(dto));
		return dto;
	}

	public static UserInfoBo buildUserListBo(UserInfoDto userInfoDto) {
		if (userInfoDto == null) {
			return null;
		}
		GroupBaseBo group = new GroupBaseBo();
		group.setId(userInfoDto.getGroupId());
		group.setName(userInfoDto.getGroupName());
		UserInfoBo infoBo = new UserInfoBo();
		infoBo.setGroup(group);
		if (userInfoDto.getGroupId() != null && userInfoDto.getUserId() != null
				&& userInfoDto.getUserId().equals(userInfoDto.getId())) {
			infoBo.setIsGroupLeader(true);
		}
		infoBo.setUserId(userInfoDto.getId());
		infoBo.setEmail(userInfoDto.getEmail());
		infoBo.setMobile(userInfoDto.getMobile());
		infoBo.setRealname(userInfoDto.getRealname());
		infoBo.setType(userInfoDto.getType());
		infoBo.setUsername(userInfoDto.getUsername());
		infoBo.setStatus(userInfoDto.getStatus());
		infoBo.setCreateTime(userInfoDto.getCreateTime());
		logger.info("conver userInfoDto to UserInfoBo:{}",
				JSON.toJSONString(infoBo));
		return infoBo;
	}

	public static UserInfoBo buildUserInfoBo(User user, Group group) {
		if (user == null) {
			return null;
		}
		UserInfoBo infoBo = new UserInfoBo();
		infoBo.setUserId(user.getId());
		infoBo.setEmail(user.getEmail());
		infoBo.setMobile(user.getMobile());
		infoBo.setRealname(user.getRealname());
		infoBo.setType(user.getType());
		infoBo.setUsername(user.getUsername());
		infoBo.setStatus(user.getStatus());
		infoBo.setCreateTime(user.getCreateTime());
		infoBo.setIsGroupLeader(false);
		if (group != null) {
			GroupBaseBo baseGroup = new GroupBaseBo();
			baseGroup.setId(group.getId());
			baseGroup.setName(group.getName());
			infoBo.setGroup(baseGroup);
			if (group.getUserId() != null
					&& group.getUserId().equals(user.getId())) {
				infoBo.setIsGroupLeader(true);
			}
		}
		logger.info("conver user and group to UserInfoBo:{}",
				JSON.toJSONString(infoBo));
		return infoBo;
	}
}
