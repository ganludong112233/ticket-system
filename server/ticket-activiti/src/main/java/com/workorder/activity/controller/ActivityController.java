package com.workorder.activity.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workorder.activity.service.ActivityService;
import com.workorder.ticket.model.ActivityStart;
import com.workorder.ticket.model.TaskInfo;

@RestController
public class ActivityController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ActivityController.class);
	@Resource
	private ActivityService activityService;

	/**
	 * 启动流程
	 * 
	 */
	@PostMapping("/start")
	public String startProcesses(@RequestBody ActivityStart startVo) {
		return activityService.startProcesses(startVo.getProcessDefinitionId(),
				startVo.getVariables());
	}

	/**
	 * 
	 * <p>
	 * 描述: 根据用户id查询待办任务列表
	 * </p>
	 * 
	 */
	@GetMapping("/findTasksByProcessor")
	public List<TaskInfo> findTasksByUserId(
			@RequestParam("processDefinitionId") String processDefinitionId,
			@RequestParam("processor") String processor) {
		List<Task> taskList = activityService.findTasksByUserId(
				processDefinitionId, processor);
		if (CollectionUtils.isEmpty(taskList)) {
			return Collections.emptyList();
		}
		List<TaskInfo> result = new ArrayList<>();
		taskList.forEach(item -> result.add(new TaskInfo(item.getId(), item
				.getName(), item.getProcessInstanceId(), item.getAssignee(),
				null)));
		return result;
	}

	/**
	 * 
	 * <p>
	 * 描述: 获取当前任务
	 * </p>
	 * 
	 */
	@GetMapping("/getCurrentTask")
	public TaskInfo getCurrentTask(
			@RequestParam("processInstanceId") String processInstanceId) {
		LOGGER.info("查询instance：{}当前任务！", processInstanceId);
		Task task = activityService.getCurrentTask(processInstanceId);

		if (task == null) {
			LOGGER.info("查询instance：{}当前没有任务！", processInstanceId);
			return null;
		}
		LOGGER.info("查询instance：{}当前任务，task:{}！", processInstanceId,
				task.getName());
		return new TaskInfo(task.getId(), task.getName(),
				task.getProcessInstanceId(), task.getAssignee(), null);
	}

	/**
	 * 
	 * <p>
	 * 描述:任务审批 （通过/拒接）
	 * </p>
	 * 
	 * @param taskId
	 *            任务id
	 * @param userId
	 *            用户id
	 * @param result
	 *            false OR true
	 */
	@PostMapping("/completeTask")
	public void completeTask(@RequestParam("taskId") String taskId,
			@RequestParam("processor") String processor,
			@RequestParam(value = "result", required = false) Boolean result,
			@RequestParam(value = "comment", required = false) String comment) {
		activityService.completeTask(taskId, processor, result, comment);
	}

	/**
	 * 
	 * <p>
	 * 描述: 查询历史任务列表
	 * </p>
	 * 
	 */
	@GetMapping("/findHistoryTasks")
	public List<TaskInfo> findHistoryTasks(
			@RequestParam("processInstanceId") String processInstanceId) {
		return activityService.findHistoryTasks(processInstanceId);
	}

	/**
	 * 
	 * <p>
	 * 删除任务审批
	 * </p>
	 * 
	 * @param processInstanceId
	 */
	@PostMapping("/deleteTask")
	public void deleteTask(
			@RequestParam("processInstanceId") String processInstanceId) {
		activityService.deleteTask(processInstanceId);
	}

	/**
	 * 
	 * <p>
	 * 描述: 生成流程图 首先启动流程，获取processInstanceId，替换即可生成
	 * </p>
	 * 
	 * @param processInstanceId
	 * @throws Exception
	 */
	public void queryProImg(
			@RequestParam("processInstanceId") String processInstanceId) {
	}

	@PostMapping("/deploy")
	public String deploy(@RequestParam("bpmnKey") String bpmnKey,
			HttpServletResponse response) {
		return activityService.deploy(bpmnKey);
	}
}
