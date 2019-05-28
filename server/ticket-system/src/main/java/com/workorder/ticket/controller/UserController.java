package com.workorder.ticket.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sowing.common.page.Page;
import com.sowing.common.response.PageResponse;
import com.sowing.common.response.ResponseBase;
import com.sowing.common.response.SimpleResponse;
import com.workorder.ticket.controller.convertor.UserControllerConvertor;
import com.workorder.ticket.controller.vo.user.UserEditVo;
import com.workorder.ticket.controller.vo.user.UserPageVo;
import com.workorder.ticket.controller.vo.user.UserQueryVo;
import com.workorder.ticket.service.UserService;
import com.workorder.ticket.service.bo.user.UserEditBo;
import com.workorder.ticket.service.bo.user.UserInfoBo;
import com.workorder.ticket.service.common.SessionService;

/**
 * 用户信息管理
 * 
 * @author wzdong
 * @Date 2019年3月4日
 * @version 1.0
 */
@RestController
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Resource
	private UserService userService;

	@Resource
	private SessionService sessionService;

	/**
	 * 新建用户
	 * 
	 * @param initVo
	 * @return
	 */
	@PostMapping("/user/create")
	public ResponseBase create(@RequestBody UserEditVo initVo) {
		UserEditBo UserEditBo = initVo.convertToUserEditBo();
		LOGGER.info("create user:" + JSON.toJSONString(UserEditBo));
		if (userService.create(UserEditBo) > 0) {
			return ResponseBase.success();
		}
		return ResponseBase.error("create user failed!");
	}

	/**
	 * 查询用户
	 * 
	 * @param queryVo
	 * @return
	 */
	@PostMapping("/user/list")
	public PageResponse<UserPageVo> list(UserQueryVo queryVo) {
		List<UserInfoBo> userInfoList = userService.queryList(queryVo.convertToUserQueryBo());
		if (CollectionUtils.isEmpty(userInfoList)) {
			return PageResponse.buildList(Collections.emptyList());
		}
		Page page = new Page();
		page.setTotalCount(userService.queryUserCount(queryVo.convertToUserQueryBo()));
		List<UserPageVo> userPageList = new ArrayList<UserPageVo>();
		userInfoList.forEach(item -> userPageList.add(UserControllerConvertor.buildUserListVo(item)));
		return PageResponse.build(userPageList, page);
	}

	/**
	 * 编辑用户
	 * 
	 * @param editVo
	 * @return
	 */
	@PostMapping("/user/edit")
	public ResponseBase edit(@RequestBody UserEditVo editVo) {
		UserEditBo UserEditBo = editVo.convertToUserEditBo();
		LOGGER.info("update user:" + JSON.toJSONString(UserEditBo));
		if (userService.updateUser(UserEditBo) > 0) {
			return ResponseBase.success();
		}
		return ResponseBase.error("update user failed!");
	}

	/**
	 * 查询用户编辑信息
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/user/editInfo")
	public SimpleResponse<UserEditVo> getEditInfo(@RequestParam(value = "userId", required = true) Long userId) {
		LOGGER.info("get edit user:" + JSON.toJSONString(userId));
		return SimpleResponse.build(UserControllerConvertor.buildUserEditVo(userService.getUserInfoById(userId)));
	}

	/**
	 * 管理员禁用用户
	 */
	@PostMapping("/user/disable")
	public ResponseBase disable(@RequestParam(value = "userId", required = true) Long userId) {
		LOGGER.info("disable user:" + JSON.toJSONString(userId));
		if (userService.disableUser(userId) > 0) {
			return ResponseBase.success();
		}
		return ResponseBase.error("disable user failed!");
	}

	/**
	 * 管理员重置密码
	 */
	@PostMapping("/user/reset/pwd")
	public ResponseBase resetPwd(@RequestParam(value = "userId", required = true) Long userId) {
		LOGGER.info("reset user pwd:" + JSON.toJSONString(userId));
		if (userService.resetUserPwd(userId) > 0) {
			return ResponseBase.success();
		}
		return ResponseBase.error("reset user pwd failed!");
	}

	/**
	 * 用户修改密码
	 */
	@PostMapping("/user/update/pwd")
	public ResponseBase updatePwd(@RequestParam(value = "oldPwd", required = true) String oldPwd,
			@RequestParam(value="newPwd", required=true) String newPwd) {
		LOGGER.info("update user pwd:" + JSON.toJSONString(oldPwd));
		if (userService.updateUserPwd(sessionService.getCurrentUser().getUserId(), oldPwd, newPwd) > 0) {
			return ResponseBase.success();
		}
		return ResponseBase.error("update user pwd failed!");
	}
	
	/**
	 * 查询用户信息 by name
	 * 
	 * @param username
	 * @return
	 */
	@GetMapping("/user/getUserByName")
	public SimpleResponse<UserInfoBo> getUserByName(@RequestParam(value = "username", required = true) String username) {
		LOGGER.info("get user by username:" + JSON.toJSONString(username));
		UserInfoBo userInfoBo =userService.getUserByUserName(username);
		if (userInfoBo != null) {
			return SimpleResponse.build(userInfoBo);
		}
		return new SimpleResponse<UserInfoBo>(SimpleResponse.ERROR, "user not exist", null);
	}
}
