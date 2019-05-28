package com.workorder.ticket.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.workorder.ticket.common.ConstantsValue;
import com.workorder.ticket.common.enums.UserStatus;
import com.workorder.ticket.persistence.dao.GroupDao;
import com.workorder.ticket.persistence.dao.UserDao;
import com.workorder.ticket.persistence.dto.UserInfoDto;
import com.workorder.ticket.persistence.entity.Group;
import com.workorder.ticket.persistence.entity.User;
import com.workorder.ticket.service.UserService;
import com.workorder.ticket.service.bo.user.UserEditBo;
import com.workorder.ticket.service.bo.user.UserInfoBo;
import com.workorder.ticket.service.bo.user.UserQueryBo;
import com.workorder.ticket.service.convertor.UserServiceConvertor;

/**
 * 用户服务
 * 
 * @author wzdong
 * @Date 2019年3月4日
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	@Resource
	private UserDao userDao;

	@Resource
	private GroupDao groupDao;

	@Override
	@Transactional
	public int create(UserEditBo userEditBo) {
		User user = UserServiceConvertor.userConvertor(userEditBo);
		user.setPassword(ConstantsValue.default_password);
		int count = userDao.insertSelective(user);
		if (count < 1) {
			return count;
		}
		updateGroupLeader(user, userEditBo.getIsGroupLeader() == null ? false : userEditBo.getIsGroupLeader());
		LOGGER.info("create user:{}", JSON.toJSON(user));
		return count;
	}

	@Override
	@Transactional
	public int updateUser(UserEditBo userEditBo) {
		User user = UserServiceConvertor.userConvertor(userEditBo);
		user.setUpdateTime(new Date());
		updateGroupLeader(user, userEditBo.getIsGroupLeader());
		LOGGER.info("update user:{}", JSON.toJSON(user));
		return userDao.updateByPrimaryKeySelective(user);
	}

	private void updateGroupLeader(User user, Boolean isGroupLeader) {
		if (user == null || user.getId() == null || user.getGroupId() == null) {
			LOGGER.info("update user to group leader failed!! user:{}", JSON.toJSON(user));
			return;
		}
		LOGGER.info("first cancel user group leader by userId:{}", JSON.toJSON(user.getId()));
		groupDao.cancelGroupLeaderForUser(user.getId());

		if (isGroupLeader) {
			Group record = new Group();
			record.setId(user.getGroupId());
			record.setUserId(user.getId());
			record.setUpdateTime(new Date());
			LOGGER.info("update user to group leader:{}", JSON.toJSON(record));
			groupDao.updateByPrimaryKeySelective(record);
		}
	}

	@Override
	public List<UserInfoBo> queryList(UserQueryBo userQueryBo) {
		LOGGER.info("query user list request:{}", JSON.toJSON(userQueryBo));
		List<UserInfoDto> userList = userDao.getUserList(UserServiceConvertor.buildUserQueryDto(userQueryBo));
		LOGGER.info("query user list response:{}", JSON.toJSON(userList));
		if (CollectionUtils.isEmpty(userList)) {
			return Collections.emptyList();
		}
		List<UserInfoBo> result = new ArrayList<UserInfoBo>();
		userList.forEach(item -> {
			result.add(UserServiceConvertor.buildUserListBo(item));
		});
		return result;
	}

	@Override
	public int queryUserCount(UserQueryBo userQueryBo) {
		return userDao.getUserCount(UserServiceConvertor.buildUserQueryDto(userQueryBo));
	}

	@Override
	public UserInfoBo getUserInfoById(Long userId) {
		LOGGER.info("query user by userId:{}", JSON.toJSON(userId));
		User user = userDao.selectByPrimaryKey(userId);
		if (user == null) {
			return null;
		}
		Group group = null;
		if (user.getGroupId() != null) {
			LOGGER.info("query user group by group id:{}", JSON.toJSON(user.getGroupId()));
			group = groupDao.selectByPrimaryKey(user.getGroupId());
		}
		return UserServiceConvertor.buildUserInfoBo(user, group);
	}

	@Override
	public int disableUser(Long userId) {
		LOGGER.info("disable User userId:{}", JSON.toJSON(userId));
		if (userId == null) {
			return 0;
		}
		User record = new User();
		record.setId(userId);
		record.setForbiddenTime(new Date());
		record.setUpdateTime(new Date());
		record.setStatus(UserStatus.DISABLE.getType());
		return userDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int resetUserPwd(Long userId) {
		LOGGER.info("reset User Pwd userId:{}", JSON.toJSON(userId));
		if (userId == null) {
			return 0;
		}
		User record = new User();
		record.setId(userId);
		record.setUpdateTime(new Date());
		record.setPassword(ConstantsValue.default_password);
		return userDao.updateByPrimaryKeySelective(record);

	}

	@Override
	public int updateUserPwd(Long userId, String oldPwd, String newPwd) {
		LOGGER.info("update User Pwd userId:{}", JSON.toJSON(userId));
		if (userId == null) {
			return 0;
		}
		return userDao.updateUserPwd(userId, oldPwd, newPwd);
	}

	@Override
	public UserInfoBo getUserByUserNameAndPwd(String username, String password) {
		LOGGER.info("get User by username{} and Pwd{}", username, password);
		User user = userDao.getUserByUserNameAndPwd(username, password);
		if (user == null) {
			return null;
		}
		Group group = null;
		if (user.getGroupId() != null) {
			LOGGER.info("query user group by group id:{}", JSON.toJSON(user.getGroupId()));
			group = groupDao.selectByPrimaryKey(user.getGroupId());
		}
		return UserServiceConvertor.buildUserInfoBo(user, group);
	}

	@Override
	public UserInfoBo getUserByUserName(String username) {
		if (username == null) {
			return null;
		}
		User user = userDao.getUserByUserName(username);
		if (user == null) {
			return null;
		}
		Group group = null;
		if (user.getGroupId() != null) {
			LOGGER.info("query user group by group id:{}", JSON.toJSON(user.getGroupId()));
			group = groupDao.selectByPrimaryKey(user.getGroupId());
		}
		return UserServiceConvertor.buildUserInfoBo(user, group);
	}
}
