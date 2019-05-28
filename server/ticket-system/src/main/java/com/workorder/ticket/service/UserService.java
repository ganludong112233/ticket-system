package com.workorder.ticket.service;

import java.util.List;

import com.workorder.ticket.service.bo.user.UserEditBo;
import com.workorder.ticket.service.bo.user.UserInfoBo;
import com.workorder.ticket.service.bo.user.UserQueryBo;

/**
 * 用户服务
 * 
 * @author wzdong
 * @Date 2019年3月4日
 * @version 1.0
 */
public interface UserService {

	int create(UserEditBo userEditBo);
	
	int updateUser(UserEditBo userEditBo);

	List<UserInfoBo> queryList(UserQueryBo userQueryBo);

	int queryUserCount(UserQueryBo userQueryBo);

	UserInfoBo getUserInfoById(Long userId);

	int disableUser(Long userId);

	int resetUserPwd(Long userId);

	int updateUserPwd(Long userId, String oldPwd, String newPwd);

	UserInfoBo getUserByUserNameAndPwd(String username, String password);

	UserInfoBo getUserByUserName(String username);


}
