package com.workorder.ticket.model;

/**
 * 任务信息
 * 
 * @author wzdong
 * @Date 2019年3月6日
 * @version 1.0
 */
public class TaskInfo {
	private String taskId;
	private String taskName;
	private String instanceId;
	private String assigee;
	private String comment;

	public TaskInfo() {
	}

	public TaskInfo(String taskId, String taskName, String instanceId,
			String assigee, String comment) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.instanceId = instanceId;
		this.assigee = assigee;
		this.comment = comment;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getAssigee() {
		return assigee;
	}

	public void setAssigee(String assigee) {
		this.assigee = assigee;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
