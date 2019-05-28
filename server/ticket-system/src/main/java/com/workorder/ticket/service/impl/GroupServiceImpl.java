package com.workorder.ticket.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.workorder.ticket.persistence.dao.GroupDao;
import com.workorder.ticket.service.GroupService;
import com.workorder.ticket.service.bo.group.GroupBaseBo;

@Service
public class GroupServiceImpl implements GroupService {
	@Resource
	private GroupDao groupDao;

	@Override
	public List<GroupBaseBo> queryAll() {
		// TODO: 查询所有组
		return null;
	}
}
