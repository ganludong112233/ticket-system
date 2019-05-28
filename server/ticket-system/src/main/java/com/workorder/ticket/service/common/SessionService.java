package com.workorder.ticket.service.common;

import com.workorder.ticket.service.bo.user.UserInfoBo;

public interface SessionService {
	/**
	 * 获取当前登录用户信息
	 * 
	 * @return
	 */
	public UserInfoBo getCurrentUser();

	/**
	 * 判断当前用户是否是管理员
	 * 
	 * @return
	 */
	public boolean isSuperAdmin();

	/**
	 * 设置用户缓存
	 * 
	 * @param UserInfoBo
	 */
	public void set(UserInfoBo UserInfoBo);

	/**
	 * 清空用户缓存
	 */
	public void clear();
}
