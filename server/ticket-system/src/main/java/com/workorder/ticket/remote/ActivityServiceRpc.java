package com.workorder.ticket.remote;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.workorder.ticket.model.ActivityStart;
import com.workorder.ticket.model.TaskInfo;

@FeignClient(name = "ticket-activiti")
public interface ActivityServiceRpc {
	@PostMapping("/start")
	public String startProcesses(@RequestBody ActivityStart startVo);

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
			@RequestParam("processor") String processor);

	/**
	 * 
	 * <p>
	 * 描述: 获取当前任务
	 * </p>
	 * 
	 */
	@GetMapping("/getCurrentTask")
	public TaskInfo getCurrentTask(
			@RequestParam("processInstanceId") String processInstanceId);

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
			@RequestParam(value = "comment", required = false) String comment);

	/**
	 * 
	 * <p>
	 * 描述: 查询历史任务列表
	 * </p>
	 * 
	 */
	@GetMapping("/findHistoryTasks")
	public List<TaskInfo> findHistoryTasks(
			@RequestParam("processInstanceId") String processInstanceId);

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
			@RequestParam("processInstanceId") String processInstanceId);

	@PostMapping("/deploy")
	public String deploy(@RequestParam("bpmnKey") String bpmnKey);
}
