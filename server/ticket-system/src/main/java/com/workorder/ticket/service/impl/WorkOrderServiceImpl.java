package com.workorder.ticket.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sowing.common.exception.ServiceException;
import com.workorder.ticket.common.ConstantsValue;
import com.workorder.ticket.common.TemplateKeys;
import com.workorder.ticket.common.enums.WorkOrderOp;
import com.workorder.ticket.common.enums.WorkOrderStatus;
import com.workorder.ticket.common.utils.DateUtils;
import com.workorder.ticket.controller.vo.common.FlowStepItem;
import com.workorder.ticket.controller.vo.common.HistogramItem;
import com.workorder.ticket.controller.vo.common.TicketOpLog;
import com.workorder.ticket.model.ActivityStart;
import com.workorder.ticket.model.TaskInfo;
import com.workorder.ticket.persistence.dao.BizLogDao;
import com.workorder.ticket.persistence.dao.FlowTemplateDao;
import com.workorder.ticket.persistence.dao.GroupDao;
import com.workorder.ticket.persistence.dao.UserDao;
import com.workorder.ticket.persistence.dao.WorkOrderDao;
import com.workorder.ticket.persistence.dto.UserInfoDto;
import com.workorder.ticket.persistence.dto.UserQueryDto;
import com.workorder.ticket.persistence.dto.workorder.WorkOrderQueryDto;
import com.workorder.ticket.persistence.dto.workorder.WorkOrderWithCreatorDto;
import com.workorder.ticket.persistence.entity.FlowTemplate;
import com.workorder.ticket.persistence.entity.Group;
import com.workorder.ticket.persistence.entity.User;
import com.workorder.ticket.persistence.entity.WorkOrder;
import com.workorder.ticket.remote.ActivityServiceRpc;
import com.workorder.ticket.service.BizLogService;
import com.workorder.ticket.service.WorkOrderService;
import com.workorder.ticket.service.bo.user.UserInfoBo;
import com.workorder.ticket.service.bo.workorder.WorkOrderBo;
import com.workorder.ticket.service.bo.workorder.WorkOrderEditBo;
import com.workorder.ticket.service.bo.workorder.WorkOrderQueryBo;
import com.workorder.ticket.service.common.SessionService;
import com.workorder.ticket.service.common.SmsService;
import com.workorder.ticket.service.convertor.WorkOrderServiceConvertor;

/**
 * 工单服务
 * 
 * @author wzdong
 * @Date 2019年3月4日
 * @version 1.0
 */
@Service
public class WorkOrderServiceImpl implements WorkOrderService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WorkOrderServiceImpl.class);

	private static String OPERATOR_GROUP_CODE = "operator";

	@Resource
	private WorkOrderDao workOrderDao;
	@Resource
	private FlowTemplateDao flowTemplateDao;
	@Resource
	private UserDao userDao;
	@Resource
	private GroupDao groupDao;
	@Resource
	private BizLogDao bizLogDao;
	@Resource
	private BizLogService bizLogService;

	@Resource
	private SmsService smsService;
	@Resource
	private SessionService sessionService;
	@Resource
	private BizLogService workOrderLogService;

	@Resource
	private ActivityServiceRpc activityServiceRpc;

	/**
	 * 新建工单
	 * 
	 * @param initVo
	 * @return
	 */
	@Override
	public void create(WorkOrderEditBo initBo) {
		LOGGER.info("用户[{}]新建工单！", sessionService.getCurrentUser()
				.getUsername());
		WorkOrder workOrder = WorkOrderServiceConvertor.buildWorkOrder(initBo);
		workOrder.setCreateId(sessionService.getCurrentUser().getUserId());
		workOrder.setCreateTime(new Date());
		workOrder.setStatus(WorkOrderStatus.NEW.getType());
		workOrder.setFlowEngineDefinitionId(getWorkOrderFlowDefinitId());
		workOrderDao.insertSelective(workOrder);
		updateStatusAndLog(workOrder, WorkOrderOp.NEW, "");
		LOGGER.info("用户[{}]新建工单[{}]成功！", sessionService.getCurrentUser()
				.getUsername(), workOrder.getId());
	}

	/**
	 * 编辑工单
	 * 
	 * @param editVo
	 * @return
	 */
	@Override
	public void edit(WorkOrderEditBo editBo) {
		WorkOrder workOrder = getAndCheckDataAuth(editBo.getId());
		// 新建或拒绝才能编辑
		if (workOrder == null
				|| (WorkOrderStatus.NEW.getType() != workOrder.getStatus() && WorkOrderStatus.REFUSED
						.getType() != workOrder.getStatus())) {
			LOGGER.info("workorder:{}", JSON.toJSONString(workOrder));
			throw ServiceException.commonException("不合法请求！");
		}
		LOGGER.info("用户[{}]编辑工单[{}]！", sessionService.getCurrentUser()
				.getUsername(), editBo.getId());
		WorkOrder updateWorkOrder = WorkOrderServiceConvertor
				.buildWorkOrder(editBo);
		workOrderDao.updateByPrimaryKeySelective(updateWorkOrder);
		updateStatusAndLog(workOrder, WorkOrderOp.EDIT, "");
		LOGGER.info("用户[{}]编辑工单[{}]成功！", sessionService.getCurrentUser()
				.getUsername(), editBo.getId());
	}

	/**
	 * 删除工单
	 * 
	 * @param editVo
	 * @return
	 */
	@Override
	public void delete(Long workOrderId) {
		WorkOrder workOrder = getAndCheckDataAuth(workOrderId);
		if (WorkOrderStatus.NEW.getType() != workOrder.getStatus()) {
			throw ServiceException.commonException("不可删除！");
		}
		workOrderDao.deleteByPrimaryKey(workOrderId);
		LOGGER.info("用户[{}]删除工单[{}]成功！", sessionService.getCurrentUser()
				.getUsername(), workOrderId);
	}

	/**
	 * 撤销工单
	 * 
	 * @param editVo
	 * @return
	 */
	@Override
	public void revoke(Long workOrderId) {
		WorkOrder workOrder = getAndCheckDataAuth(workOrderId);
		LOGGER.info("用户[{}]撤销工单[{}]！", sessionService.getCurrentUser()
				.getUsername(), workOrderId);
		if (workOrder.getFlowEngineInstanceId() != null) {
			LOGGER.info("用户[{}]撤销工单[{}],删除流程引擎实例[{}]！", sessionService
					.getCurrentUser().getUsername(), workOrderId, workOrder
					.getFlowEngineInstanceId());
			activityServiceRpc.deleteTask(workOrder.getFlowEngineInstanceId());
		}
		updateStatusAndLog(workOrder, WorkOrderOp.REVOKE, "");
		LOGGER.info("用户[{}]撤销工单[{}]成功！", sessionService.getCurrentUser()
				.getUsername(), workOrderId);
	}

	/**
	 * 提交工单
	 * 
	 * @param workOrderId
	 * @return
	 */
	@Override
	@Transactional
	public void submit(Long workOrderId) {
		WorkOrder workOrder = getAndCheckDataAuth(workOrderId);
		if (WorkOrderStatus.NEW.getType() == workOrder.getStatus()) {
			LOGGER.info("用户[{}]提交工单[{}]！", sessionService.getCurrentUser()
					.getUsername(), workOrderId);
			submit(workOrder);
		} else if (WorkOrderStatus.REFUSED.getType() == workOrder.getStatus()) {
			LOGGER.info("用户[{}]重新提交工单[{}]！", sessionService.getCurrentUser()
					.getUsername(), workOrderId);
			resubmit(workOrder);
		} else {
			LOGGER.warn("用户[{}]提交工单[{}]，工单状态不合法，status：{}！", sessionService
					.getCurrentUser().getUsername(), workOrderId, workOrder
					.getStatus());
			throw ServiceException.commonException("不合法请求！");
		}
		LOGGER.info("用户[{}]提交工单[{}]成功！", sessionService.getCurrentUser()
				.getUsername(), workOrderId);
	}

	/**
	 * 处理任务
	 * 
	 * @return
	 */
	@Override
	public void completeTask(Long workOrderId, Boolean result, String comment) {
		WorkOrder workorder = workOrderDao.getByPrimaryKey(workOrderId);
		// 工单状态校验
		if (workorder == null
				|| (workorder.getStatus() != WorkOrderStatus.PROCESSING
						.getType() && workorder.getStatus() != WorkOrderStatus.REFUSED
						.getType())) {
			throw ServiceException.commonException("非法请求！");
		}
		LOGGER.info("用户[{}]处理工单[{}],instanceId:[{}]！", sessionService
				.getCurrentUser().getUsername(), workOrderId, workorder
				.getFlowEngineInstanceId());
		// 流程引擎处理工单
		TaskInfo taskInfo = activityServiceRpc.getCurrentTask(workorder
				.getFlowEngineInstanceId());
		if (taskInfo == null) {
			throw ServiceException.commonException("没有任务需要处理！");
		}
		String processor = String.valueOf(sessionService.getCurrentUser()
				.getUserId());
		activityServiceRpc.completeTask(taskInfo.getTaskId(), processor,
				result, comment);

		// 处理结果 【同意】 OR 【拒绝】
		if (result != null) {
			if (result == false) {
				LOGGER.info("用户[{}]审核拒绝工单[{}]！", sessionService
						.getCurrentUser().getUsername(), workOrderId);
				updateStatusAndLog(workorder, WorkOrderOp.REFUSED, comment);
				noticeResult(workorder, workorder.getCreateId(), "拒绝处理");
			} else {
				LOGGER.info("用户[{}]审核通过工单[{}]！", sessionService
						.getCurrentUser().getUsername(), workOrderId);
				updateStatusAndLog(workorder, WorkOrderOp.AGREE, comment);
			}
		}
		LOGGER.info("用户[{}]处理工单[{}]成功！", sessionService.getCurrentUser()
				.getUsername(), workOrderId);

		// 判断流程是否结束
		TaskInfo nextTask = activityServiceRpc.getCurrentTask(workorder
				.getFlowEngineInstanceId());
		if (nextTask == null) {
			LOGGER.info("工单[{}]处理结束！", workOrderId);
			updateStatusAndLog(workorder, WorkOrderOp.COMPLETE, "");
			noticeResult(workorder, workorder.getCreateId(), "处理完成");
		} else {
			// 通知
			noticeProcess(workorder, Long.valueOf(nextTask.getAssigee()));
		}

	}

	/**
	 * 查询工单详情
	 * 
	 * @return
	 */
	@Override
	public WorkOrderBo getById(Long workOrderId) {
		WorkOrderWithCreatorDto workorderWithCreator = workOrderDao
				.getWithCreator(workOrderId);
		// 查询工单当前处理人
		User currentProcessor = getCurrentProcessor(workorderWithCreator
				.getFlowEngineInstanceId());
		return WorkOrderServiceConvertor.buildWorkOrderBo(workorderWithCreator,
				currentProcessor);
	}

	/**
	 * 查询工单操作日志
	 * 
	 * @return
	 */
	@Override
	public List<TicketOpLog> getOpLogs(Long workOrderId) {
		return bizLogService.getOpLogs(workOrderId,
				ConstantsValue.BIZ_TYPE_WORKORDER);
	}

	/**
	 * 查询工单处理步骤
	 * 
	 * @return
	 */
	@Override
	public List<FlowStepItem> getFlowSteps(Long workOrderId) {
		WorkOrder workOrder = getAndCheckDataAuth(workOrderId);
		if (workOrder.getFlowEngineInstanceId() == null) {
			return Collections.emptyList();
		}
		List<TaskInfo> histortyTaskList = activityServiceRpc
				.findHistoryTasks(workOrder.getFlowEngineInstanceId());
		if (workOrder.getFlowEngineInstanceId() == null) {
			return Collections.emptyList();
		}
		UserQueryDto userQueryBo = new UserQueryDto();
		List<Long> userIds = new ArrayList<Long>();
		histortyTaskList.forEach(item -> {
			if (item.getAssigee() != null) {
				userIds.add(Long.valueOf(item.getAssigee()));
			}
		});
		userQueryBo.setUserIds(userIds);
		List<UserInfoDto> opUsers = userDao.getUserList(userQueryBo);

		List<FlowStepItem> steps = new ArrayList<FlowStepItem>();
		histortyTaskList.forEach(item -> {
			String assigneer = getUserRealName(item.getAssigee(), opUsers);
			steps.add(new FlowStepItem(item.getTaskName(), assigneer, item
					.getComment()));
		});
		return steps;
	}

	/**
	 * 查询所有工单
	 * 
	 * @param queryVo
	 * @return
	 */
	@Override
	public List<WorkOrderBo> queryAllList(WorkOrderQueryBo queryBo) {
		WorkOrderQueryDto queryDto = WorkOrderServiceConvertor
				.buildWorkOrderQueryDto(queryBo);
		return queryByParam(queryDto);
	}

	/**
	 * 查询所有工单Count
	 * 
	 * @param queryVo
	 * @return
	 */
	@Override
	public int queryAllListCount(WorkOrderQueryBo queryBo) {
		WorkOrderQueryDto queryDto = WorkOrderServiceConvertor
				.buildWorkOrderQueryDto(queryBo);
		return workOrderDao.getCountByParam(queryDto);
	}

	/**
	 * 查询所有待处理的工单
	 * 
	 * @param queryVo
	 * @return
	 */
	@Override
	public List<WorkOrderBo> queryWaitingList() {
		// 查询待处理任务
		List<TaskInfo> taskInfoList = activityServiceRpc.findTasksByUserId(
				getWorkOrderFlowDefinitId(), sessionService.getCurrentUser()
						.getUserId().toString());
		if (CollectionUtils.isEmpty(taskInfoList)) {
			return Collections.emptyList();
		}
		// 根据任务查询工单
		List<String> instanceIds = new ArrayList<String>();
		taskInfoList.forEach(item -> instanceIds.add(item.getInstanceId()));
		WorkOrderQueryDto queryDto = new WorkOrderQueryDto();
		queryDto.setInstanceIds(instanceIds);

		List<WorkOrderBo> result = queryByParam(queryDto);
		if (CollectionUtils.isEmpty(result)) {
			return Collections.emptyList();
		}
		// 过滤非处理中的请求
		Iterator<WorkOrderBo> itr = result.iterator();
		while (itr.hasNext()) {
			WorkOrderBo workOrderBo = itr.next();
			if (WorkOrderStatus.PROCESSING.getType() != workOrderBo.getStatus()) {
				itr.remove();
			}
		}
		return result;
	}

	/**
	 * 查询所有待处理的工单Count
	 * 
	 * @param queryVo
	 * @return
	 */
	@Override
	public int queryWaitingListCount() {
		return queryWaitingList().size();
	}

	/**
	 * 查询用户处理的工单历史
	 * 
	 * @param queryVo
	 */
	@Override
	public List<WorkOrderBo> queryHistoryList(WorkOrderQueryBo queryBo) {
		// TODO:
		return null;
	}

	/**
	 * 查询用户处理的工单历史Count
	 * 
	 * @param queryVo
	 */
	@Override
	public int queryHistoryListCount(WorkOrderQueryBo queryBo) {
		// TODO:
		return 0;
	}

	/**
	 * 按天统计工单
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	@Override
	public List<HistogramItem> statisticByDay(Date start, Date end) {
		List<HistogramItem> list = workOrderDao.statisticByDay(start, end);
		List<HistogramItem> result = new ArrayList<HistogramItem>();
		Map<String, HistogramItem> map = new HashMap<String, HistogramItem>();
		list.forEach(item -> map.put(item.getItem(), item));
		// 日期补全
		int between = DateUtils.differentDaysByMillisecond(start, end);
		int offset = 0;
		do {
			String day = DateUtils.format(
					DateUtils.getAddedDate(start, offset),
					DateUtils.DATE_FORMAT_YYYY_MM_DD);
			offset++;
			HistogramItem histogramItem = map.get(day);
			if (histogramItem == null) {
				result.add(new HistogramItem(day));
			} else {
				result.add(histogramItem);
			}
		} while (offset <= between);

		return result;
	}

	/**
	 * 首次提交
	 * 
	 * @param workOrder
	 */
	private void submit(WorkOrder workOrder) {

		// 开启工单流程
		Map<String, Object> variables = new HashMap<String, Object>();
		UserInfoBo proposer = sessionService.getCurrentUser();
		Group group = groupDao.selectByPrimaryKey(proposer.getGroup().getId());
		User leader = userDao.selectByPrimaryKey(group.getUserId());
		User operatorUser = userDao.getLeaderByGroupCode(OPERATOR_GROUP_CODE);
		if (operatorUser == null || leader == null) {
			throw ServiceException.commonException("配置异常！");
		}
		variables.put("proposer", proposer.getUserId());
		variables.put("editor", proposer.getUserId());
		variables.put("leaderAuditor", leader.getId());
		variables.put("operatorAuditor", operatorUser.getId());

		LOGGER.info("用户[{}]提交工单[{}]，处理人：{}！", sessionService.getCurrentUser()
				.getUserId(), workOrder.getId(), variables);

		String instanceId = activityServiceRpc
				.startProcesses(new ActivityStart(workOrder
						.getFlowEngineDefinitionId(), variables));
		instanceId = instanceId.replaceAll("\"", "");
		LOGGER.info("用户[{}]提交工单[{}]，启动流程，instanceId:{}！", sessionService
				.getCurrentUser().getUserId(), workOrder.getId(), instanceId);
		// 保存工单流程实例ID
		workOrder.setSubmitTime(new Date());
		workOrder.setFlowEngineInstanceId(instanceId);
		updateStatusAndLog(workOrder, WorkOrderOp.SUBMIT, "");
		// 提交工单
		completeTask(workOrder.getId(), null, "");

	}

	/**
	 * 重新提交工单
	 * 
	 * @param workOrderId
	 * @return
	 */
	private void resubmit(WorkOrder workOrder) {

		// 重新提交工单
		completeTask(workOrder.getId(), null, "");

		workOrder.setSubmitTime(new Date());
		updateStatusAndLog(workOrder, WorkOrderOp.RESUBMIT, "");
	}

	/**
	 * 校验工单数据编辑权限
	 * 
	 * @param workOrderId
	 */
	private WorkOrder getAndCheckDataAuth(Long workOrderId) {

		WorkOrder workOrder = workOrderDao.getByPrimaryKey(workOrderId);
		if (workOrder == null
				|| sessionService.getCurrentUser().getUserId() != workOrder
						.getCreateId()) {
			throw ServiceException.commonException("非法请求！");
		}
		return workOrder;
	}

	/**
	 * 获取当前处理人
	 * 
	 * @param flowEngineInstanceId
	 * @return
	 */
	private User getCurrentProcessor(String flowEngineInstanceId) {
		if (flowEngineInstanceId == null) {
			return null;
		}
		TaskInfo taskInfo = activityServiceRpc
				.getCurrentTask(flowEngineInstanceId);
		User currentProcessor = null;
		if (taskInfo != null) {
			currentProcessor = userDao.selectByPrimaryKey(Long.valueOf(taskInfo
					.getAssigee()));
		}
		return currentProcessor;
	}

	/**
	 * 查询工单流程引擎模板ID
	 * 
	 * @return
	 */
	private String getWorkOrderFlowDefinitId() {
		FlowTemplate flow = flowTemplateDao.getOneByCode(TemplateKeys.WORK_ORDER
				.getKey());
		return flow.getFlowEngineDefinitionId();
	}

	private List<WorkOrderBo> queryByParam(WorkOrderQueryDto workQueryDto) {
		List<WorkOrderWithCreatorDto> list = workOrderDao
				.getListByParam(workQueryDto);
		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptyList();
		}
		List<WorkOrderBo> result = new ArrayList<WorkOrderBo>();
		list.forEach(item -> result.add(WorkOrderServiceConvertor
				.buildWorkOrderBo(item,
						getCurrentProcessor(item.getFlowEngineInstanceId()))));
		return result;
	}

	/**
	 * 记录日志以及更改状态
	 * 
	 * @param workOrder
	 * @param op
	 */
	private void updateStatusAndLog(WorkOrder workOrder, WorkOrderOp op,
			String comment) {
		// 更新数据状态
		if (op.getTargetStatus() != null) {
			workOrder.setStatus(op.getTargetStatus().getType());
			workOrderDao.updateByPrimaryKeySelective(workOrder);
		}
		// 日志
		workOrderLogService.saveWorkOrderLog(workOrder.getId(), workOrder
				.getTitle(), op.getValue(), comment, sessionService
				.getCurrentUser().getUserId());
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

	/**
	 * 获取短信通知处理模板
	 * 
	 * @param workOrderId
	 * @param title
	 * @param submitor
	 * @return
	 */
	private String getNoticeProcessMessage(Long workOrderId, String title,
			String submitor) {
		String messageTemplet = "【工单系统】提示您有一个待处理工单，请前往系统处理。工单编号：[number]，工单标题：[title]，提交人：[sumitor]";
		messageTemplet = messageTemplet.replaceAll("number",
				workOrderId.toString());
		messageTemplet = messageTemplet.replaceAll("title", title);
		messageTemplet = messageTemplet.replaceAll("sumitor", submitor);
		return messageTemplet;
	}

	/**
	 * 获取短信通知处理结果模板
	 * 
	 * @param workOrderId
	 * @param title
	 * @param submitor
	 * @return
	 */
	private String getNoticeResultMessage(String workOrderTitle, String result) {
		String messageTemplet = "【工单系统】提示您,您提交的工单[title],已处理，处理结果[result]。";
		messageTemplet = messageTemplet.replaceAll("title", workOrderTitle);
		messageTemplet = messageTemplet.replaceAll("result", result);
		return messageTemplet;
	}

	/**
	 * 通知处理
	 * 
	 * @param workOrder
	 * @param noticeUserId
	 */
	private void noticeProcess(WorkOrder workOrder, Long noticeUserId) {
		User nextProcessor = userDao.selectByPrimaryKey(Long
				.valueOf(noticeUserId));
		User submitor = userDao.selectByPrimaryKey(workOrder.getCreateId());
		if (nextProcessor == null) {
			LOGGER.info("通知用户[{}]处理工单[{}]失败，用户不存在！", noticeUserId,
					workOrder.getId());
		}
		// TODO:邮件短信息通知
		try {
			smsService.send(
					nextProcessor.getMobile(),
					getNoticeProcessMessage(workOrder.getId(),
							workOrder.getTitle(), submitor.getRealname()));
		} catch (Exception e) {
			LOGGER.error("通知用户处理工单失败，excption:{}", e);
			e.printStackTrace();
		}
		LOGGER.info("通知用户[{}]处理工单[{}]！", nextProcessor.getUsername(),
				workOrder.getId());
	}

	/**
	 * 通知处理结果
	 * 
	 * @param workOrder
	 * @param noticeUserId
	 */
	private void noticeResult(WorkOrder workOrder, Long noticeUserId,
			String result) {
		User nextProcessor = userDao.selectByPrimaryKey(Long
				.valueOf(noticeUserId));
		if (nextProcessor == null) {
			LOGGER.info("通知用户[{}]工单处理结果[{}]失败，用户不存在！", noticeUserId, result);

		}
		// TODO:邮件短信息通知
		try {
			smsService.send(nextProcessor.getMobile(),
					getNoticeResultMessage(workOrder.getTitle(), result));
		} catch (Exception e) {
			LOGGER.error("通知用户工单处理结果失败，excption:{}", e);
			e.printStackTrace();
		}
		LOGGER.info("通知用户[{}]工单处理结果[{}]成功！", nextProcessor.getUsername(),
				result);
	}
}
