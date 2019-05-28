package com.workorder.ticket.controller.vo.deploy;

import com.workorder.ticket.service.bo.deploy.DeployStepBo;

/**
 * 部署步骤说明
 * 
 * @author wzdong
 * @Date 2019年3月25日
 * @version 1.0
 */
public class DeployStepVo {
	private Long id;
	private Integer stepOrder;
	private Byte type;
	private String content;

	public DeployStepVo() {

	}

	public DeployStepVo(DeployStepBo step) {
		if (step != null) {
			this.id = step.getId();
			this.stepOrder = step.getStepOrder();
			this.type = step.getType();
			this.content = step.getContent();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStepOrder() {
		return stepOrder;
	}

	public void setStepOrder(Integer stepOrder) {
		this.stepOrder = stepOrder;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
