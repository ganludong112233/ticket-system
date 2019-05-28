package com.workorder.ticket.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sowing.common.exception.ParamException;
import com.sowing.common.exception.ServiceException;
import com.workorder.ticket.common.ConstantsValue;
import com.workorder.ticket.common.TemplateKeys;
import com.workorder.ticket.common.enums.DeployOp;
import com.workorder.ticket.common.enums.DeployStatus;
import com.workorder.ticket.common.enums.DeployType;
import com.workorder.ticket.common.utils.DateUtils;
import com.workorder.ticket.controller.vo.common.FlowStepItem;
import com.workorder.ticket.controller.vo.common.HistogramItem;
import com.workorder.ticket.controller.vo.common.TicketOpLog;
import com.workorder.ticket.model.ActivityStart;
import com.workorder.ticket.model.TaskInfo;
import com.workorder.ticket.persistence.dao.BizLogDao;
import com.workorder.ticket.persistence.dao.DeployDao;
import com.workorder.ticket.persistence.dao.DeployProjectDao;
import com.workorder.ticket.persistence.dao.DeployStepDao;
import com.workorder.ticket.persistence.dao.FlowTemplateDao;
import com.workorder.ticket.persistence.dao.GroupDao;
import com.workorder.ticket.persistence.dao.ProjectDao;
import com.workorder.ticket.persistence.dao.UserDao;
import com.workorder.ticket.persistence.dto.UserInfoDto;
import com.workorder.ticket.persistence.dto.UserQueryDto;
import com.workorder.ticket.persistence.dto.deploy.DeployProjectDto;
import com.workorder.ticket.persistence.dto.deploy.DeployQueryDto;
import com.workorder.ticket.persistence.dto.deploy.DeployWithCreatorDto;
import com.workorder.ticket.persistence.entity.Deploy;
import com.workorder.ticket.persistence.entity.DeployProject;
import com.workorder.ticket.persistence.entity.DeployStep;
import com.workorder.ticket.persistence.entity.FlowTemplate;
import com.workorder.ticket.persistence.entity.Group;
import com.workorder.ticket.persistence.entity.Project;
import com.workorder.ticket.persistence.entity.User;
import com.workorder.ticket.remote.ActivityServiceRpc;
import com.workorder.ticket.service.BizLogService;
import com.workorder.ticket.service.DeployService;
import com.workorder.ticket.service.bo.deploy.DeployBo;
import com.workorder.ticket.service.bo.deploy.DeployEditBo;
import com.workorder.ticket.service.bo.deploy.DeployQueryBo;
import com.workorder.ticket.service.bo.user.UserInfoBo;
import com.workorder.ticket.service.common.SessionService;
import com.workorder.ticket.service.common.SmsService;
import com.workorder.ticket.service.convertor.DeployServiceConvertor;

/**
 * 部署申请服务
 * 
 * @author wzdong
 * @Date 2019年3月25日
 * @version 1.0
 */
@Service
public class DeployServiceImpl implements DeployService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeployServiceImpl.class);

	@Resource
	private DeployDao deployDao;
	@Resource
	private DeployStepDao deployStepDao;
	@Resource
	private DeployProjectDao deployProjectDao;
	@Resource
	private ProjectDao projectDao;
	@Resource
	private FlowTemplateDao flowTemplateDao;
	@Resource
	private UserDao userDao;
	@Resource
	private GroupDao groupDao;
	@Resource
	private BizLogDao bizLogDao;

	@Resource
	private SmsService smsService;
	@Resource
	private SessionService sessionService;
	@Resource
	private BizLogService bizLogService;

	@Resource
	private ActivityServiceRpc activityServiceRpc;

	/**
	 * 新建部署申请
	 * 
	 * @param initVo
	 * @return
	 */
	@Override
	@Transactional
	public void create(DeployEditBo initBo) {
		LOGGER.info("用户[{}]新建部署申请！", sessionService.getCurrentUser()
				.getUsername());
		// 1.插入申请表
		Deploy deploy = DeployServiceConvertor.buildDeploy(initBo);
		deploy.setCreateId(sessionService.getCurrentUser().getUserId());
		deploy.setCreateTime(new Date());
		deploy.setStatus(DeployStatus.NEW.getType());
		deploy.setFlowEngineDefinitionId(getDeployFlowDefinitId(initBo
				.getType()));
		deployDao.insert(deploy);
		initBo.setId(deploy.getId());

		// 2.插入申请项目表
		List<DeployProject> deployProjectList = DeployServiceConvertor
				.buildDeployProjects(initBo);
		if (CollectionUtils.isEmpty(deployProjectList)) {
			throw ParamException.paramException("参数异常！");
		}
		deployProjectDao.insertBatch(deployProjectList);
		// 3.插入申请步骤表
		List<DeployStep> deployStepList = DeployServiceConvertor
				.buildDeploySteps(initBo);
		AtomicInteger stepOrder = new AtomicInteger(1);
		deployStepList.forEach(item -> {
			item.setStepOrder(stepOrder.addAndGet(1));
		});
		if (CollectionUtils.isEmpty(deployStepList)) {
			throw ParamException.paramException("参数异常！");
		}
		deployStepDao.insertBatch(deployStepList);

		// 日志
		updateStatusAndLog(deploy, DeployOp.NEW, "");
		LOGGER.info("用户[{}]新建部署申请[{}]成功！", sessionService.getCurrentUser()
				.getUsername(), deploy.getId());
	}

	/**
	 * 编辑部署申请
	 * 
	 * @param editVo
	 * @return
	 */
	@Override
	@Transactional
	public void edit(DeployEditBo editBo) {
		Deploy deploy = getAndCheckDataAuth(editBo.getId());
		// 新建或拒绝才能编辑
		if (deploy == null
				|| (DeployStatus.NEW.getType() != deploy.getStatus() && DeployStatus.REFUSED
						.getType() != deploy.getStatus())) {
			LOGGER.info("Deploy:{}", JSON.toJSONString(deploy));
			throw ServiceException.commonException("不合法请求！");
		}
		LOGGER.info("用户[{}]编辑部署申请[{}]！", sessionService.getCurrentUser()
				.getUsername(), editBo.getId());
		// 1.编辑申请表
		Deploy updateDeploy = DeployServiceConvertor.buildDeploy(editBo);
		deployDao.updateByPrimaryKeySelective(updateDeploy);
		// 2.编辑申请项目表
		List<DeployProject> deployProjectList = DeployServiceConvertor
				.buildDeployProjects(editBo);
		if (CollectionUtils.isEmpty(deployProjectList)) {
			throw ParamException.paramException("参数异常！");
		}
		deployProjectDao.deleteByDeployId(editBo.getId());
		deployProjectDao.insertBatch(deployProjectList);
		// 3.编辑申请步骤表
		List<DeployStep> deployStepList = DeployServiceConvertor
				.buildDeploySteps(editBo);
		AtomicInteger stepOrder = new AtomicInteger(1);
		deployStepList.forEach(item -> {
			item.setStepOrder(stepOrder.addAndGet(1));
		});
		if (CollectionUtils.isEmpty(deployStepList)) {
			throw ParamException.paramException("参数异常！");
		}
		deployStepDao.deleteByDeployId(editBo.getId());
		deployStepDao.insertBatch(deployStepList);
		updateStatusAndLog(deploy, DeployOp.EDIT, "");
		LOGGER.info("用户[{}]编辑部署申请[{}]成功！", sessionService.getCurrentUser()
				.getUsername(), editBo.getId());
	}

	/**
	 * 删除部署申请
	 * 
	 * @param editVo
	 * @return
	 */
	@Override
	@Transactional
	public void delete(Long deployId) {
		Deploy deploy = getAndCheckDataAuth(deployId);
		if (DeployStatus.NEW.getType() != deploy.getStatus()) {
			throw ServiceException.commonException("不可删除！");
		}
		deployProjectDao.deleteByDeployId(deployId);
		deployDao.deleteByPrimaryKey(deployId);
		deployProjectDao.deleteByDeployId(deployId);
		LOGGER.info("用户[{}]删除部署申请[{}]成功！", sessionService.getCurrentUser()
				.getUsername(), deployId);
	}

	/**
	 * 撤销部署申请
	 * 
	 * @param editVo
	 * @return
	 */
	@Override
	public void revoke(Long deployId) {
		Deploy deploy = getAndCheckDataAuth(deployId);
		LOGGER.info("用户[{}]撤销部署申请[{}]！", sessionService.getCurrentUser()
				.getUsername(), deployId);
		if (deploy.getFlowEngineInstanceId() != null) {
			LOGGER.info("用户[{}]撤销部署申请[{}],删除流程引擎实例[{}]！", sessionService
					.getCurrentUser().getUsername(), deployId, deploy
					.getFlowEngineInstanceId());
			activityServiceRpc.deleteTask(deploy.getFlowEngineInstanceId());
		}
		updateStatusAndLog(deploy, DeployOp.REVOKE, "");
		LOGGER.info("用户[{}]撤销部署申请[{}]成功！", sessionService.getCurrentUser()
				.getUsername(), deployId);
	}

	/**
	 * 提交部署申请
	 * 
	 * @param deployId
	 * @return
	 */
	@Override
	@Transactional
	public void submit(Long deployId) {
		Deploy deploy = getAndCheckDataAuth(deployId);
		// 第一次提交
		if (DeployStatus.NEW.getType() == deploy.getStatus()) {
			LOGGER.info("用户[{}]提交部署申请[{}]！", sessionService.getCurrentUser()
					.getUsername(), deployId);
			submit(deploy);
		} else if (DeployStatus.REFUSED.getType() == deploy.getStatus()) {
			// 拒绝、修改后提交
			LOGGER.info("用户[{}]重新提交部署申请[{}]！", sessionService.getCurrentUser()
					.getUsername(), deployId);
			resubmit(deploy);
		} else {
			// 异常请求
			LOGGER.warn("用户[{}]提交部署申请[{}]，部署申请状态不合法，status：{}！", sessionService
					.getCurrentUser().getUsername(), deployId, deploy
					.getStatus());
			throw ServiceException.commonException("不合法请求！");
		}
		LOGGER.info("用户[{}]提交部署申请[{}]成功！", sessionService.getCurrentUser()
				.getUsername(), deployId);
	}

	/**
	 * 处理任务
	 * 
	 * @return
	 */
	@Override
	public void completeTask(Long deployId, Boolean result, String comment) {
		Deploy deploy = deployDao.getByPrimaryKey(deployId);
		// 部署申请状态校验
		if (deploy == null
				|| (deploy.getStatus() != DeployStatus.PROCESSING.getType() && deploy
						.getStatus() != DeployStatus.REFUSED.getType())) {
			throw ServiceException.commonException("非法请求！");
		}
		LOGGER.info("用户[{}]处理部署申请[{}],instanceId:[{}]！", sessionService
				.getCurrentUser().getUsername(), deployId, deploy
				.getFlowEngineInstanceId());
		// 流程引擎处理部署申请
		TaskInfo taskInfo = activityServiceRpc.getCurrentTask(deploy
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
				LOGGER.info("用户[{}]审核拒绝部署申请[{}]！", sessionService
						.getCurrentUser().getUsername(), deployId);
				updateStatusAndLog(deploy, DeployOp.REFUSED, comment);
				noticeResult(deploy, deploy.getCreateId(), "拒绝处理");
			} else {
				LOGGER.info("用户[{}]审核通过部署申请[{}]！", sessionService
						.getCurrentUser().getUsername(), deployId);
				updateStatusAndLog(deploy, DeployOp.AGREE, comment);
			}
		}
		LOGGER.info("用户[{}]处理部署申请[{}]成功！", sessionService.getCurrentUser()
				.getUsername(), deployId);

		// 判断流程是否结束
		TaskInfo nextTask = activityServiceRpc.getCurrentTask(deploy
				.getFlowEngineInstanceId());
		if (nextTask == null) {
			LOGGER.info("部署申请[{}]处理结束！", deployId);
			updateStatusAndLog(deploy, DeployOp.COMPLETE, "");
			noticeResult(deploy, deploy.getCreateId(), "处理完成");
		} else {
			// 通知
			noticeProcess(deploy, Long.valueOf(nextTask.getAssigee()));
		}

	}

	/**
	 * 查询部署申请详情
	 * 
	 * @return
	 */
	@Override
	public DeployBo getById(Long deployId) {
		DeployWithCreatorDto deployWithCreator = deployDao
				.getWithCreator(deployId);
		// 查询部署申请当前处理人
		User currentProcessor = getCurrentProcessor(deployWithCreator
				.getFlowEngineInstanceId());
		List<Project> projects = projectDao.getByDeployId(deployId);
		List<DeployStep> deploySteps = deployStepDao.getByDeployId(deployId);
		return DeployServiceConvertor.buildDeployBo(deployWithCreator,
				currentProcessor, projects, deploySteps);
	}

	/**
	 * 查询部署申请操作日志
	 * 
	 * @return
	 */
	@Override
	public List<TicketOpLog> getOpLogs(Long deployId) {
		return bizLogService.getOpLogs(deployId,
				ConstantsValue.BIZ_TYPE_DEPLOYMENT);
	}

	/**
	 * 查询部署申请处理步骤
	 * 
	 * @return
	 */
	@Override
	public List<FlowStepItem> getFlowSteps(Long deployId) {
		Deploy deploy = getAndCheckDataAuth(deployId);
		if (deploy.getFlowEngineInstanceId() == null) {
			return Collections.emptyList();
		}
		List<TaskInfo> histortyTaskList = activityServiceRpc
				.findHistoryTasks(deploy.getFlowEngineInstanceId());
		if (deploy.getFlowEngineInstanceId() == null) {
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
	 * 查询所有部署申请
	 * 
	 * @param queryVo
	 * @return
	 */
	@Override
	public List<DeployBo> queryAllList(DeployQueryBo queryBo) {
		DeployQueryDto queryDto = DeployServiceConvertor
				.buildDeployQueryDto(queryBo);
		return queryByParam(queryDto);
	}

	/**
	 * 查询所有部署申请Count
	 * 
	 * @param queryVo
	 * @return
	 */
	@Override
	public int queryAllListCount(DeployQueryBo queryBo) {
		DeployQueryDto queryDto = DeployServiceConvertor
				.buildDeployQueryDto(queryBo);
		return deployDao.getCountByParam(queryDto);
	}

	/**
	 * 查询所有待处理的部署申请
	 * 
	 * @param queryVo
	 * @return
	 */
	@Override
	public List<DeployBo> queryWaitingList() {
		List<String> definitIds = getDeployFlowDefinitIds();
		if (CollectionUtils.isEmpty(definitIds)) {
			return Collections.emptyList();
		}
		// 查询待处理任务
		List<TaskInfo> taskInfoList = new ArrayList<TaskInfo>();
		definitIds.forEach(item -> {
			taskInfoList.addAll(activityServiceRpc.findTasksByUserId(item,
					sessionService.getCurrentUser().getUserId().toString()));
		});

		if (CollectionUtils.isEmpty(taskInfoList)) {
			return Collections.emptyList();
		}
		// 根据任务查询部署申请
		List<String> instanceIds = new ArrayList<String>();
		taskInfoList.forEach(item -> instanceIds.add(item.getInstanceId()));
		DeployQueryDto queryDto = new DeployQueryDto();
		queryDto.setInstanceIds(instanceIds);
		List<DeployBo> result = queryByParam(queryDto);
		if (CollectionUtils.isEmpty(result)) {
			return Collections.emptyList();
		}
		// 过滤非处理中的请求
		Iterator<DeployBo> itr = result.iterator();
		while (itr.hasNext()) {
			DeployBo deployBo = itr.next();
			if (DeployStatus.PROCESSING.getType() != deployBo.getStatus()) {
				itr.remove();
			}
		}
		return result;
	}

	/**
	 * 查询所有待处理的部署申请Count
	 * 
	 * @param queryVo
	 * @return
	 */
	@Override
	public int queryWaitingListCount() {
		return queryWaitingList().size();
	}

	/**
	 * 查询用户处理的部署申请历史
	 * 
	 * @param queryVo
	 */
	@Override
	public List<DeployBo> queryHistoryList(DeployQueryBo queryBo) {
		// TODO:
		return null;
	}

	/**
	 * 查询用户处理的部署申请历史Count
	 * 
	 * @param queryVo
	 */
	@Override
	public int queryHistoryListCount(DeployQueryBo queryBo) {
		// TODO:
		return 0;
	}

	/**
	 * 按天统计部署申请
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	@Override
	public List<HistogramItem> statisticByDay(Date start, Date end) {
		List<HistogramItem> list = deployDao.statisticByDay(start, end);
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
	 * @param deploy
	 */
	private void submit(Deploy deploy) {

		// 开启部署申请流程
		Map<String, Object> variables = getActivitInitVariables();

		LOGGER.info("用户[{}]提交部署申请[{}]，处理人：{}！", sessionService.getCurrentUser()
				.getUserId(), deploy.getId(), variables);

		String instanceId = activityServiceRpc
				.startProcesses(new ActivityStart(deploy
						.getFlowEngineDefinitionId(), variables));
		instanceId = instanceId.replaceAll("\"", "");
		LOGGER.info("用户[{}]提交部署申请[{}]，启动流程，instanceId:{}！", sessionService
				.getCurrentUser().getUserId(), deploy.getId(), instanceId);
		// 保存部署申请流程实例ID
		deploy.setSubmitTime(new Date());
		deploy.setFlowEngineInstanceId(instanceId);
		updateStatusAndLog(deploy, DeployOp.SUBMIT, "");
		// 提交部署申请
		completeTask(deploy.getId(), null, "");

	}

	/**
	 * 重新提交部署申请
	 * 
	 * @param DeployId
	 * @return
	 */
	private void resubmit(Deploy deploy) {

		// 重新提交部署申请
		completeTask(deploy.getId(), null, "");

		deploy.setSubmitTime(new Date());
		updateStatusAndLog(deploy, DeployOp.RESUBMIT, "");
	}

	/**
	 * 校验部署申请数据编辑权限
	 * 
	 * @param deployId
	 */
	private Deploy getAndCheckDataAuth(Long deployId) {

		Deploy deploy = deployDao.getByPrimaryKey(deployId);
		if (deploy == null
				|| sessionService.getCurrentUser().getUserId() != deploy
						.getCreateId()) {
			LOGGER.warn("数据操作权限不合法：{},操作人ID:{}", JSON.toJSONString(deploy),
					deployId);
			throw ServiceException.commonException("非法请求！");
		}
		return deploy;
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
	 * 查询部署申请流程引擎模板ID
	 * 
	 * @return
	 */
	private String getDeployFlowDefinitId(Byte type) {
		FlowTemplate flow = null;
		if (DeployType.BUGFIX.getType() == type) {
			flow = flowTemplateDao.getOneByCode(TemplateKeys.RELEASE_FIXBUG
					.getKey());
		} else if (DeployType.CODE_OPTIMIZE.getType() == type) {
			flow = flowTemplateDao.getOneByCode(TemplateKeys.RELEASE_OPTIMIZE
					.getKey());
		} else if (DeployType.VERSION_RELEASE.getType() == type) {
			flow = flowTemplateDao.getOneByCode(TemplateKeys.RELEASE_VERSION
					.getKey());
		}
		if (flow == null) {
			throw ServiceException.commonException("流程模板为空！");
		}
		return flow.getFlowEngineDefinitionId();
	}

	/**
	 * 查询所有审批模板ID
	 * 
	 * @return
	 */
	private List<String> getDeployFlowDefinitIds() {
		List<String> types = new ArrayList<String>();
		FlowTemplate flow = null;
		flow = flowTemplateDao.getOneByCode(TemplateKeys.RELEASE_FIXBUG
				.getKey());
		if (flow != null) {
			types.add(flow.getFlowEngineDefinitionId());
		}
		flow = flowTemplateDao.getOneByCode(TemplateKeys.RELEASE_OPTIMIZE
				.getKey());
		if (flow != null) {
			types.add(flow.getFlowEngineDefinitionId());
		}
		flow = flowTemplateDao.getOneByCode(TemplateKeys.RELEASE_VERSION
				.getKey());
		if (flow != null) {
			types.add(flow.getFlowEngineDefinitionId());
		}
		return types;
	}

	private List<DeployBo> queryByParam(DeployQueryDto workQueryDto) {
		// 查询部署申请主表
		List<DeployWithCreatorDto> list = deployDao
				.getListByParam(workQueryDto);
		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptyList();
		}
		List<Long> deployIds = new ArrayList<Long>();
		list.forEach(item -> deployIds.add(item.getId()));
		List<DeployBo> result = new ArrayList<DeployBo>();
		// 查询部署申请步骤
		Map<Long, List<DeployStep>> deployStepMap = getDeployStepsByDeployIds(deployIds);
		// 查询部署申请项目
		Map<Long, List<Project>> deployProjectMap = getProjectByDeployIds(deployIds);
		list.forEach(item -> result.add(DeployServiceConvertor.buildDeployBo(
				item, getCurrentProcessor(item.getFlowEngineInstanceId()),
				deployProjectMap.get(item.getId()),
				deployStepMap.get(item.getId()))));
		return result;
	}

	private Map<Long, List<DeployStep>> getDeployStepsByDeployIds(
			List<Long> deployIds) {
		Map<Long, List<DeployStep>> result = new HashMap<Long, List<DeployStep>>();
		List<DeployStep> stepList = deployStepDao.getByDeployIds(deployIds);
		if (CollectionUtils.isEmpty(stepList)) {
			return Collections.emptyMap();
		}
		stepList.forEach(item -> {
			List<DeployStep> tmpSteps = result.get(item.getDeployId());
			if (tmpSteps == null) {
				tmpSteps = new ArrayList<DeployStep>();
				result.put(item.getDeployId(), tmpSteps);
			}
			tmpSteps.add(item);
		});
		return result;
	}

	private Map<Long, List<Project>> getProjectByDeployIds(List<Long> deployIds) {
		Map<Long, List<Project>> result = new HashMap<Long, List<Project>>();
		List<DeployProjectDto> projectList = projectDao
				.getByDeployIds(deployIds);
		if (CollectionUtils.isEmpty(projectList)) {
			return Collections.emptyMap();
		}
		projectList.forEach(item -> {
			List<Project> tmpSteps = result.get(item.getDeployId());
			if (tmpSteps == null) {
				tmpSteps = new ArrayList<Project>();
				result.put(item.getDeployId(), tmpSteps);
			}
			tmpSteps.add(item);
		});
		return result;
	}

	/**
	 * 记录日志以及更改状态
	 * 
	 * @param deploy
	 * @param op
	 */
	private void updateStatusAndLog(Deploy deploy, DeployOp op, String comment) {
		// 更新数据状态
		if (op.getTargetStatus() != null) {
			deploy.setStatus(op.getTargetStatus().getType());
			deployDao.updateByPrimaryKeySelective(deploy);
		}
		// 日志
		bizLogService.saveDeployLog(deploy.getId(), deploy.getTitle(), op
				.getValue(), comment, sessionService.getCurrentUser()
				.getUserId());
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
	 * @param deployId
	 * @param title
	 * @param submitor
	 * @return
	 */
	private String getNoticeProcessMessage(Long deployId, String title,
			String submitor) {
		String messageTemplet = "【部署申请系统】提示您有一个待处理部署申请，请前往系统处理。部署申请编号：[number]，部署申请标题：[title]，提交人：[sumitor]";
		messageTemplet = messageTemplet.replaceAll("number",
				deployId.toString());
		messageTemplet = messageTemplet.replaceAll("title", title);
		messageTemplet = messageTemplet.replaceAll("sumitor", submitor);
		return messageTemplet;
	}

	/**
	 * 获取短信通知处理结果模板
	 * 
	 * @param DeployId
	 * @param title
	 * @param submitor
	 * @return
	 */
	private String getNoticeResultMessage(String deployTitle, String result) {
		String messageTemplet = "【部署申请系统】提示您,您提交的部署申请[title],已处理，处理结果[result]。";
		messageTemplet = messageTemplet.replaceAll("title", deployTitle);
		messageTemplet = messageTemplet.replaceAll("result", result);
		return messageTemplet;
	}

	/**
	 * 通知处理
	 * 
	 * @param DeployWithProjectDto
	 * @param noticeUserId
	 */
	private void noticeProcess(Deploy deploy, Long noticeUserId) {
		User nextProcessor = userDao.selectByPrimaryKey(Long
				.valueOf(noticeUserId));
		User submitor = userDao.selectByPrimaryKey(deploy.getCreateId());
		if (nextProcessor == null) {
			LOGGER.info("通知用户[{}]处理部署申请[{}]失败，用户不存在！", noticeUserId,
					deploy.getId());
		}
		// TODO:邮件短信息通知
		try {
			smsService.send(
					nextProcessor.getMobile(),
					getNoticeProcessMessage(deploy.getId(), deploy.getTitle(),
							submitor.getRealname()));
		} catch (Exception e) {
			LOGGER.error("通知用户处理部署申请失败，excption:{}", e);
			e.printStackTrace();
		}
		LOGGER.info("通知用户[{}]处理部署申请[{}]！", nextProcessor.getUsername(),
				deploy.getId());
	}

	/**
	 * 通知处理结果
	 * 
	 * @param DeployWithProjectDto
	 * @param noticeUserId
	 */
	private void noticeResult(Deploy deploy, Long noticeUserId, String result) {
		User nextProcessor = userDao.selectByPrimaryKey(Long
				.valueOf(noticeUserId));
		if (nextProcessor == null) {
			LOGGER.info("通知用户[{}]部署申请处理结果[{}]失败，用户不存在！", noticeUserId, result);

		}
		// TODO:邮件短信息通知
		try {
			smsService.send(nextProcessor.getMobile(),
					getNoticeResultMessage(deploy.getTitle(), result));
		} catch (Exception e) {
			LOGGER.error("通知用户部署申请处理结果失败，excption:{}", e);
			e.printStackTrace();
		}
		LOGGER.info("通知用户[{}]部署申请处理结果[{}]成功！", nextProcessor.getUsername(),
				result);
	}

	/**
	 * activiti初始化参数
	 * 
	 * @return
	 */
	private Map<String, Object> getActivitInitVariables() {
		// 开启部署申请流程
		Map<String, Object> variables = new HashMap<String, Object>();
		UserInfoBo proposer = sessionService.getCurrentUser();
		Group proposeGroup = groupDao.selectByPrimaryKey(proposer.getGroup()
				.getId());
		User groupLeader = userDao.selectByPrimaryKey(proposeGroup.getUserId());
		User operatorUser = userDao.getLeaderByGroupCode(OPERATOR_GROUP_CODE);

		User productUser = userDao.getLeaderByGroupCode(PROD_GROUP_CODE);

		User testUser = userDao.getLeaderByGroupCode(TEST_GROUP_CODE);

		User leaderUser = userDao.getLeaderByGroupCode(LEADER_GROUP_CODE);
		if (groupLeader == null || operatorUser == null || productUser == null
				|| testUser == null || leaderUser == null) {
			throw ServiceException.commonException("配置异常！");
		}
		variables.put("groupLeader", groupLeader.getId());
		variables.put("proposer", proposer.getUserId());
		variables.put("tester", testUser.getId());
		variables.put("proder", productUser.getId());
		variables.put("leader", leaderUser.getId());
		variables.put("operator", operatorUser.getId());
		return variables;
	}

	private static String OPERATOR_GROUP_CODE = "operator";// 运维
	private static String PROD_GROUP_CODE = "product";// 产品
	private static String TEST_GROUP_CODE = "test";// 测试
	private static String LEADER_GROUP_CODE = "leader";// 领导
}
