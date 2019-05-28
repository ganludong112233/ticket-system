package com.workorder.ticket.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sowing.common.exception.ServiceException;
import com.workorder.ticket.common.ConstantsValue;
import com.workorder.ticket.service.UserService;
import com.workorder.ticket.service.bo.user.UserInfoBo;
import com.workorder.ticket.service.common.RedisService;
import com.workorder.ticket.service.common.SessionService;

/**
 * 登录拦截器
 * 
 * @author wzdong
 * @Date 2019年3月4日
 * @version 1.0
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Value("${project.env}")
	private String env;
	@Resource
	private SessionService sessionService;
	@Resource
	private UserService userService;
	@Resource
	private RedisService redisService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			if ("dev".equals(env)) {// 模拟登陆
				UserInfoBo user = userService.getUserInfoById(1L);
				sessionService.set(user);
				return true;
			}
			response.getWriter().println(
					JSON.toJSONString(new ServiceException("401",
							"user do not login!!")));
			return false;
		}
		for (Cookie cookie : cookies) {
			if (!cookie.getName().equals(ConstantsValue.SESSION_ID)) {
				continue;
			}
			String userId = cookie.getValue();
			String cacheSessionKey = ConstantsValue.S_SESSION_KEY + userId;
			// get session in redis
			String userSessionJson = redisService.getForString(cacheSessionKey);
			if (StringUtils.isNotBlank(userSessionJson)) {
				UserInfoBo userSession = JSON.parseObject(userSessionJson,
						UserInfoBo.class);
				if (userSession != null && userSession.getUserId() != null) {
					// update session
					sessionService.set(userSession);
					redisService.setExpire(cacheSessionKey,
							ConstantsValue.SESSION_EXPIRE);
					return true;
				}
			}

		}
		response.getWriter().println(
				JSON.toJSONString(new ServiceException("401",
						"user do not login!!")));
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 由于线程池，避免用户重用风险，手动清空session缓存
		sessionService.clear();
	}

}
