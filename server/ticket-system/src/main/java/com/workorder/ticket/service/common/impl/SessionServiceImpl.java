package com.workorder.ticket.service.common.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sowing.common.exception.ServiceException;
import com.workorder.ticket.common.enums.UserType;
import com.workorder.ticket.service.bo.user.UserInfoBo;
import com.workorder.ticket.service.common.SessionService;

/**
 * 管理登录管理员信息
 * 
 * @author wzdong
 *
 */
@Service
public class SessionServiceImpl implements SessionService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SessionServiceImpl.class);
	private static ThreadLocal<UserInfoBo> localSession = new ThreadLocal<UserInfoBo>();

	/**
	 * 获取当前登录用户信息
	 * 
	 * @return
	 */
	public UserInfoBo getCurrentUser() {
		UserInfoBo userInfo = localSession.get();
		if (userInfo == null) {
			LOGGER.debug("请求session为空，请先登录！");
			throw ServiceException.unLoginException("请先登录！");
		}
		return userInfo;
	}

	/**
	 * 判断当前用户是否是管理员
	 * 
	 * @return
	 */
	public boolean isSuperAdmin() {
		return UserType.isSuperAdmin(getCurrentUser().getType());
	}

	/**
	 * 设置用户缓存
	 * 
	 * @param UserInfoBo
	 */
	public void set(UserInfoBo UserInfoBo) {
		localSession.set(UserInfoBo);
	}

	/**
	 * 清空用户缓存
	 */
	public void clear() {
		localSession.remove();
	}
}
