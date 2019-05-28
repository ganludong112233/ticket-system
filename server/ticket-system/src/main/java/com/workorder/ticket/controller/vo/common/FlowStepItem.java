package com.workorder.ticket.controller.vo.common;

/**
 * 处理流程步骤
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public class FlowStepItem {
	private String name;// 流程名称
	private String processor;// 处理人
	private String comment;// 备注

	public FlowStepItem() {
	}

	public FlowStepItem(String name, String processor) {
		this.name = name;
		this.processor = processor;
	}

	public FlowStepItem(String name, String processor, String comment) {
		this.name = name;
		this.processor = processor;
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
