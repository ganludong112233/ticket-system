package com.workorder.activity.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.workorder.ticket.model.TaskInfo;

public interface ActivityService {
	/**
	 * 启动流程
	 * 
	 * @param bizId
	 *            业务id
	 */
	public String startProcesses(String processDefinitionId,
			Map<String, Object> variables);

	/**
	 * 
	 * <p>
	 * 描述: 根据用户id查询待办任务列表
	 * </p>
	 * 
	 */
	public List<Task> findTasksByUserId(String processDefinitionId,
			String processor);

	/**
	 * <p>
	 * 描述:获取当前任务
	 * </p>
	 * 
	 */
	public Task getCurrentTask(String processInstanceId);

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
	public void completeTask(String taskId, String processor, Boolean result,
			String comment);

	/**
	 * 
	 * <p>
	 * 描述: 查询历史任务列表
	 * </p>
	 * 
	 */
	public List<TaskInfo> findHistoryTasks(String processInstanceId);

	/**
	 * 
	 * <p>
	 * 描述:删除任务审批
	 * </p>
	 * 
	 * @param processInstanceId
	 */
	public void deleteTask(String processInstanceId);

	/**
	 * 
	 * <p>
	 * 描述: 生成流程图 首先启动流程，获取processInstanceId，替换即可生成
	 * </p>
	 * 
	 * @param processInstanceId
	 * @throws Exception
	 */
	public void queryProImg(String processInstanceId);

	public String deploy(String bpmnKey);

}
