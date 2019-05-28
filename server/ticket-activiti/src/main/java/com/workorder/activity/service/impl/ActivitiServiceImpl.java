package com.workorder.activity.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.workorder.activity.service.ActivityService;
import com.workorder.ticket.model.TaskInfo;

@Service
public class ActivitiServiceImpl implements ActivityService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ActivitiServiceImpl.class);
	@Resource
	public ProcessEngine processEngine;
	@Resource
	private RuntimeService runtimeService;
	@Resource
	private TaskService taskService;
	@Resource
	private HistoryService historyService;
	@Resource
	private RepositoryService repositoryService;
	@Resource
	private ProcessEngineConfigurationImpl processEngineConfiguration;

	/**
	 * 启动流程
	 * 
	 * @param bizId
	 *            业务id
	 */
	@Override
	public String startProcesses(String processDefinitionId,
			Map<String, Object> variables) {
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceById(processDefinitionId, variables);
		LOGGER.info("启动新流程，instanceId:{}", processInstance.getId());
		return processInstance.getId();
	}

	/**
	 * 
	 * <p>
	 * 描述: 根据用户id查询待办任务列表
	 * </p>
	 * 
	 */
	@Override
	public List<Task> findTasksByUserId(String processDefinitionId,
			String processor) {
		List<Task> resultTask = taskService.createTaskQuery()
				.processDefinitionId(processDefinitionId)
				.taskCandidateOrAssigned(processor).list();
		return resultTask;
	}

	/**
	 * <p>
	 * 描述:获取当前任务
	 * </p>
	 * 
	 */
	@Override
	public Task getCurrentTask(String processInstanceId) {
		return taskService.createTaskQuery()// 创建查询对象
				.processInstanceId(processInstanceId)// 通过流程实例id来查询当前任务
				.singleResult();
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
	@Override
	public void completeTask(String taskId, String processor, Boolean result,
			String comment) {
		// 获取流程实例
		taskService.claim(taskId, processor);

		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("verify", result);
		taskService.addComment(taskId, null, comment);
		taskService.complete(taskId, vars);
	}

	/**
	 * 
	 * <p>
	 * 描述: 查询历史任务列表
	 * </p>
	 * 
	 */
	@Override
	public List<TaskInfo> findHistoryTasks(String processInstanceId) {
		List<HistoricActivityInstance> list = processEngine.getHistoryService()
				.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).finished()
				.orderByHistoricActivityInstanceEndTime().asc().list();
		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptyList();
		}
		List<TaskInfo> result = new ArrayList<TaskInfo>();
		for (HistoricActivityInstance hai : list) {
			if ("exclusiveGateway".equals(hai.getActivityType())) {
				// 过滤网关
				continue;
			}
			List<Comment> comments = taskService.getTaskComments(hai
					.getTaskId());
			StringBuilder commentBuilder = new StringBuilder();
			// 如果当前任务有批注
			if (comments != null && comments.size() > 0) {
				comments.forEach(item -> commentBuilder.append(item
						.getFullMessage()));
			}

			result.add(new TaskInfo(hai.getTaskId(), hai.getActivityName(), hai
					.getProcessInstanceId(), hai.getAssignee(), commentBuilder
					.toString()));
		}
		return result;
	}

	/**
	 * 
	 * <p>
	 * 描述:删除任务审批
	 * </p>
	 * 
	 * @param processInstanceId
	 */
	@Override
	public void deleteTask(String processInstanceId) {
		runtimeService.deleteProcessInstance(processInstanceId, "删除任务");
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
	@Override
	public void queryProImg(String processInstanceId) {
		try {
			// 获取历史流程实例
			HistoricProcessInstance processInstance = historyService
					.createHistoricProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();

			// 根据流程定义获取输入流
			InputStream is = repositoryService
					.getProcessDiagram(processInstance.getProcessDefinitionId());
			BufferedImage bi = ImageIO.read(is);
			File file = new File("demo2.png");
			if (!file.exists())
				file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			ImageIO.write(bi, "png", fos);
			fos.close();
			is.close();
			System.out.println("图片生成成功");

			List<Task> tasks = taskService.createTaskQuery()
					.taskCandidateUser("userId").list();
			for (Task t : tasks) {
				System.out.println(t.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getTaskHistory(String processInstanceId) {
		List<HistoricActivityInstance> list = processEngine.getHistoryService()
				.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).finished()
				.orderByHistoricActivityInstanceId().asc().list();
		for (HistoricActivityInstance hai : list) {
			LOGGER.info("ID:{},活动名称：{}", hai.getId(), hai.getActivityName());
		}
	}

	@Override
	public String deploy(String bpmnKey) {
		// 部署流程定义文件
		RepositoryService repositoryService = processEngine
				.getRepositoryService(); // 创建一个对流程编译库操作的Service
		DeploymentBuilder deploymentBuilder = repositoryService
				.createDeployment(); // 获取一个builder
		deploymentBuilder.addClasspathResource("activiti/"+bpmnKey + ".bpmn20.xml"); // 这里写上流程编译路径
		deploymentBuilder.key(bpmnKey);
		Deployment deployment = deploymentBuilder.deploy(); // 部署
		String deploymentId = deployment.getId(); // 获取deployment的ID
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery().deploymentId(deploymentId)
				.singleResult(); // 根据deploymentId来获取流程定义对象
		LOGGER.info("流程定义文件 [{}] , 流程ID [{}], 部署ID[{}]",
				processDefinition.getId(), processDefinition.getId(),
				deploymentId);
		return processDefinition.getId();
	}
}
