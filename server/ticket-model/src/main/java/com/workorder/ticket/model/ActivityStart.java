package com.workorder.ticket.model;

import java.util.Map;

public class ActivityStart {
	private String processDefinitionId;
	private Map<String, Object> variables;

	public ActivityStart() {
	}

	public ActivityStart(String processDefinitionId,
			Map<String, Object> variables) {
		this.processDefinitionId = processDefinitionId;
		this.variables = variables;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

}
