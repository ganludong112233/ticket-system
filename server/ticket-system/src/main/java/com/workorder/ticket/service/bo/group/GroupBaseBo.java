package com.workorder.ticket.service.bo.group;

import com.workorder.ticket.persistence.entity.Group;

/**
 * 组基础信息
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public class GroupBaseBo {
	private Long id;
	private String name;

	public GroupBaseBo() {
	}

	public GroupBaseBo(Group group) {
		this.id = group.getId();
		this.name = group.getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
