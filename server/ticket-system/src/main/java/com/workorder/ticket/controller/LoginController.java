package com.workorder.ticket.controller;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sowing.common.exception.ParamException;
import com.sowing.common.response.ResponseBase;
import com.sowing.common.response.SimpleResponse;
import com.workorder.ticket.common.ConstantsValue;
import com.workorder.ticket.common.enums.UserStatus;
import com.workorder.ticket.service.UserService;
import com.workorder.ticket.service.bo.user.UserInfoBo;
import com.workorder.ticket.service.common.RedisService;
import com.workorder.ticket.service.common.SessionService;

/**
 * 登录管理
 * 
 * @author wzdong
 * @Date 2019年3月4日
 * @version 1.0
 */
@RestController
public class LoginController {

	Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Resource
	private RedisService redisService;
	@Resource
	private UserService userService;
	@Resource
	private SessionService sessionService;

	/**
	 * user login 
	 * @param username
	 * @param password
	 * @param response
	 * @return
	 */
	@PostMapping("/login")
	public SimpleResponse<UserInfoBo> login(@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "password", required = true) String password, HttpServletResponse response) {
		LOGGER.info("username{} do login...", username);
		UserInfoBo userinfo = userService.getUserByUserNameAndPwd(username, password);
		if (userinfo == null) {
			return new SimpleResponse<UserInfoBo>(ResponseBase.ERROR, "用户名密码错误!", null);
		}
		if (String.valueOf(userinfo.getStatus()).equals(String.valueOf(UserStatus.DISABLE.getType()))) {
			return new SimpleResponse<UserInfoBo>(ResponseBase.ERROR, "用户已禁用!", null);
			
		}
		cacheUserLogin(response, userinfo);
		LOGGER.info("username{} do login success!!", username);
		return SimpleResponse.build(userinfo);
	}
	
	/**
	 * user login 
	 * @param username
	 * @param password
	 * @param response
	 * @return
	 */
	@PostMapping("/logout")
	public ResponseBase logout(@RequestParam(value = "userId", required = true) String userId,
			 HttpServletResponse response) {
		LOGGER.info("userId{} do logout...", userId);
		UserInfoBo currentUser = sessionService.getCurrentUser();
		if (currentUser == null || !userId.equals(String.valueOf(currentUser.getUserId()))) {
			throw new ParamException("param exception!!");
		}
		cleanUserLoginSession(response, currentUser);
		LOGGER.info("userId{} do logout success!!", userId);
		return ResponseBase.success();
	}

	private void cleanUserLoginSession(HttpServletResponse response, UserInfoBo currentUser) {
		LOGGER.info("user logout clean session:{}", JSON.toJSONString(currentUser));
		redisService.removeKey(ConstantsValue.S_SESSION_KEY + currentUser.getUserId());
		Cookie cookie = new Cookie(ConstantsValue.S_SESSION_KEY + currentUser.getUserId(), null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		LOGGER.info("user{} logout clean session success!!", JSON.toJSONString(currentUser.getUserId()));
		
	}

	private void cacheUserLogin(HttpServletResponse response, UserInfoBo userinfo) {
		LOGGER.info("cache login user:{}", JSON.toJSONString(userinfo));
		redisService.setForString(ConstantsValue.S_SESSION_KEY + userinfo.getUserId(), JSON.toJSONString(userinfo),
				ConstantsValue.SESSION_EXPIRE);
		Cookie cookie = new Cookie(ConstantsValue.SESSION_ID, String.valueOf(userinfo.getUserId()));
		response.addCookie(cookie);
		LOGGER.info("user{} login cache session seccess!!",userinfo.getUsername(), JSON.toJSONString(userinfo));
		

	}

}
