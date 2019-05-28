package com.workorder.ticket.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sowing.common.page.Page;
import com.workorder.ticket.common.ConstantsValue;
import com.workorder.ticket.controller.vo.common.OpLog;
import com.workorder.ticket.controller.vo.common.TicketOpLog;
import com.workorder.ticket.persistence.dao.BizLogDao;
import com.workorder.ticket.persistence.dao.UserDao;
import com.workorder.ticket.persistence.dto.BizLogQueryDto;
import com.workorder.ticket.persistence.dto.UserInfoDto;
import com.workorder.ticket.persistence.dto.UserQueryDto;
import com.workorder.ticket.persistence.entity.BizLog;
import com.workorder.ticket.service.BizLogService;

/**
 * 操作日志
 * 
 * @author wzdong
 * @Date 2019年3月25日
 * @version 1.0
 */
@Service
public class BizLogServiceImpl implements BizLogService {
	@Resource
	private BizLogDao bizLogDao;
	@Resource
	private UserDao userDao;

	@Override
	public void saveWorkOrderLog(Long workOrderId, String title, String op,
			String comment, Long processor) {
		saveLog(workOrderId, title, op, comment, processor,
				ConstantsValue.BIZ_TYPE_WORKORDER);
	}

	@Override
	public void saveDeployLog(Long deployId, String title, String op,
			String comment, Long processor) {
		saveLog(deployId, title, op, comment, processor,
				ConstantsValue.BIZ_TYPE_DEPLOYMENT);
	}

	/**
	 * 获取操作日志
	 * 
	 * @return
	 */
	@Override
	public List<OpLog> getOpLogs(Page pageItem) {
		BizLogQueryDto queryDto = new BizLogQueryDto();
		queryDto.setPageItem(pageItem);
		return bizLogDao.getOpLogList(queryDto);
	}

	/**
	 * 查询单个工单操作日志
	 * 
	 * @return
	 */
	@Override
	public List<TicketOpLog> getOpLogs(Long bizId, Integer type) {
		List<BizLog> logs = bizLogDao.getList(bizId, type);
		if (CollectionUtils.isEmpty(logs)) {
			return Collections.emptyList();
		}

		// 查询操作人
		UserQueryDto userQueryBo = new UserQueryDto();
		List<Long> userIds = new ArrayList<Long>();
		logs.forEach(item -> userIds.add(item.getProcessorId()));
		userQueryBo.setUserIds(userIds);
		List<UserInfoDto> opUsers = userDao.getUserList(userQueryBo);

		List<TicketOpLog> opLogs = new ArrayList<TicketOpLog>();
		logs.forEach(item -> {
			String opUserRealName = getUserRealName(
					String.valueOf(item.getProcessorId()), opUsers);
			opLogs.add(new TicketOpLog(opUserRealName, item.getAction(), item
					.getComment(), item.getProcessTime()));
		});
		return opLogs;
	}

	private void saveLog(Long deployId, String title, String op,
			String comment, Long processor, int bizType) {
		BizLog bizLog = new BizLog();
		bizLog.setBizId(deployId);
		bizLog.setBizType(bizType);
		bizLog.setBizTitle(title);
		bizLog.setAction(op);
		bizLog.setComment(comment);
		bizLog.setProcessorId(processor);
		bizLog.setProcessTime(new Date());
		bizLogDao.insertSelective(bizLog);
	}

	private String getUserRealName(String userId, List<UserInfoDto> opUsers) {

		if (StringUtils.isEmpty(userId)) {
			return null;
		}
		String realname = null;
		for (UserInfoDto userDto : opUsers) {
			if (userDto.getId().equals(Long.valueOf(userId))) {
				realname = userDto.getRealname();
				break;
			}
		}
		return realname;
	}
}
