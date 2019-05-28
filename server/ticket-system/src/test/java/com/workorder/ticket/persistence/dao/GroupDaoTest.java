package com.workorder.ticket.persistence.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.workorder.ticket.TicketLauncher;
import com.workorder.ticket.persistence.entity.Group;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TicketLauncher.class })
public class GroupDaoTest {
	@Resource
	private GroupDao groupDao;

	@Test
	public void getById() {
		Group group = groupDao.selectByPrimaryKey(1L);
		System.out.println(JSON.toJSONString(group));
	}
}
