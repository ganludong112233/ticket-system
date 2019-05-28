package com.workorder.ticket.controller.vo.workorder;


/**
 * 工单编辑对象
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public class WorkOrderEditVo {
	private Long id;
	private String title;
	private Byte type;
	private String description;
	private String comment;
	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
