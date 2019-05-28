package com.workorder.ticket.persistence.dao;

import com.workorder.ticket.persistence.entity.Group;

public interface GroupDao {
	int deleteByPrimaryKey(Long id);

	int insert(Group record);

	int insertSelective(Group record);

	Group selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Group record);

	int updateByPrimaryKey(Group record);
	
	int cancelGroupLeaderForUser(Long userId);

}