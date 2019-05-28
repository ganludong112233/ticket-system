package com.workorder.ticket.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workorder.ticket.persistence.dto.UserInfoDto;
import com.workorder.ticket.persistence.dto.UserQueryDto;
import com.workorder.ticket.persistence.entity.User;

public interface UserDao {
	int deleteByPrimaryKey(Long id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Long id);

	User getLeaderByGroupCode(String groupCode);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	List<UserInfoDto> getUserList(UserQueryDto record);

	int getUserCount(UserQueryDto buildUserQueryDto);

	int updateUserPwd(@Param("userId") Long userId,
			@Param("oldPwd") String oldPwd, @Param("newPwd") String newPwd);

	User getUserByUserNameAndPwd(@Param("username") String username,
			@Param("pwd") String password);

	User getUserByUserName(@Param("username")String username);
}