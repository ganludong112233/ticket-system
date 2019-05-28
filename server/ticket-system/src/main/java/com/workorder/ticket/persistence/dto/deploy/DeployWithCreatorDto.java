package com.workorder.ticket.persistence.dto.deploy;

import com.workorder.ticket.persistence.entity.Deploy;

/**
 * 部署请求对象，包含创建人信息
 * 
 * @author wzdong
 * @Date 2019年3月25日
 * @version 1.0
 */
public class DeployWithCreatorDto extends Deploy {
	private String createRealName;
	private String createUsername;

	public String getCreateRealName() {
		return createRealName;
	}

	public void setCreateRealName(String createRealName) {
		this.createRealName = createRealName;
	}

	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

}
