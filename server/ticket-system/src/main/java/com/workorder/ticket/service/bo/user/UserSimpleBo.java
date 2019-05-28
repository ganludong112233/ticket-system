package com.workorder.ticket.service.bo.user;

import java.util.Date;

/**
 * 用户简业务对象
 * 
 * @author wzdong
 * @Date 2019年3月5日
 * @version 1.0
 */
public class UserSimpleBo extends UserBaseBo {

	private String email;
	private String mobile;
	private Byte type;
	private Byte status;
	private Date createTime;
	private Date forbiddenTime;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getForbiddenTime() {
		return forbiddenTime;
	}

	public void setForbiddenTime(Date forbiddenTime) {
		this.forbiddenTime = forbiddenTime;
	}

}
