package com.workorder.ticket.common;

/**
 * 常量定义
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public class ConstantsValue {
	public static final String default_password = "123456";

	public static final int ZERO = 0;

	/**
	 * 登录session key
	 */
	public static final String S_SESSION_KEY = "s_user_login_session_";
	/**
	 * session expirt time
	 */
	public static final int SESSION_EXPIRE = 60 * 30;

	public static final String SESSION_ID = "userId";

	/**
	 * 业务类型 1 工单 2 部署申请
	 */
	public static final int BIZ_TYPE_WORKORDER = 1;
	public static final int BIZ_TYPE_DEPLOYMENT = 2;

}
