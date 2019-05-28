package com.workorder.ticket.service.common.impl;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.esms.MessageData;
import com.esms.PostMsg;
import com.esms.common.entity.Account;
import com.esms.common.entity.BusinessType;
import com.esms.common.entity.GsmsResponse;
import com.esms.common.entity.MTPack;
import com.esms.common.entity.MTPack.MsgType;
import com.esms.common.entity.MTPack.SendType;
import com.workorder.ticket.service.common.SmsService;

/**
 * 短信发送服务接口
 * 
 * @author LZY
 * @create 2015-11-17
 */
@Component
public class SmsServiceImpl implements SmsService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SmsServiceImpl.class);
	private Account ac = null;
	private PostMsg pm = null;

	@Value("${sms-user}")
	private String userName;
	@Value("${sms-password}")
	private String passWord;
	@Value("${sms-ip}")
	private String ip;
	@Value("${sms-cmport}")
	private String cmport;
	@Value("${sms-wsport}")
	private String wsport;

	/**
	 * 单发 一对一
	 * 
	 * @param mobile
	 *            手机号码
	 * @param content
	 *            短信内容
	 * @throws Exception
	 */
	public void send(String mobile, String content) throws Exception {
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(content)) {
			throw new IllegalArgumentException("mobile，content不能为空！");
		}
		MTPack pack = new MTPack();
		pack.setBatchID(UUID.randomUUID());
		pack.setBatchName("单条短信发送");
		/* SMS短信发送，MMS彩信发送 */
		pack.setMsgType(MsgType.SMS);
		pack.setBizType(0);
		pack.setDistinctFlag(false);
		/* 默认群发 */
		pack.setSendType(SendType.MASS);

		ArrayList<MessageData> msgs = new ArrayList<MessageData>();
		msgs.add(new MessageData(mobile, content));
		pack.setMsgs(msgs);
		GsmsResponse resp = pm.post(ac, pack);
		LOGGER.info("sendOnce code:" + resp.getResult() + ";message:"
				+ resp.getMessage());
	}

	/**
	 * 群发，多对一
	 * 
	 * @param mobile
	 * @param content
	 * @throws Exception
	 */
	public void sendBatch(String mobile, String content)
			throws IllegalArgumentException, Exception {
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(content)) {
			throw new IllegalArgumentException("mobile，content为空！");
		}
		MTPack pack = new MTPack();
		pack.setBatchID(UUID.randomUUID());
		pack.setBatchName("短信群发");
		/* SMS短信发送，MMS彩信发送 */
		pack.setMsgType(MTPack.MsgType.SMS);
		pack.setBizType(0);
		pack.setDistinctFlag(false);
		/* GROUP组发，MASS群发 */
		pack.setSendType(SendType.MASS);

		ArrayList<MessageData> msgs = new ArrayList<MessageData>();
		for (String s : mobile.split(",")) {
			msgs.add(new MessageData(s, content));
		}
		pack.setMsgs(msgs);
		GsmsResponse resp = pm.post(ac, pack);
		LOGGER.info("sendBatch code:" + resp.getResult() + ";message:"
				+ resp.getMessage());
	}

	/**
	 * 组发， 多对多
	 * 
	 * @param mobile
	 * @param content
	 * @throws Exception
	 */
	public void sendBatch(String[] mobile, String[] content)
			throws IllegalArgumentException, Exception {
		if (mobile.length == 0 || content.length == 0) {
			throw new IllegalArgumentException("mobile，content不能为空！");
		}
		if (mobile.length != content.length) {
			throw new IllegalArgumentException("mobile，content不匹配！");
		}
		MTPack pack = new MTPack();
		pack.setBatchID(UUID.randomUUID());
		pack.setBatchName("短信群发");
		/* SMS短信发送，MMS彩信发送 */
		pack.setMsgType(MTPack.MsgType.SMS);
		pack.setBizType(0);
		pack.setDistinctFlag(false);
		/* GROUP组发，MASS群发 */
		pack.setSendType(SendType.GROUP);

		ArrayList<MessageData> msgs = new ArrayList<MessageData>();
		for (int i = 0; i < mobile.length; i++) {
			msgs.add(new MessageData(mobile[i], content[i]));
		}

		pack.setMsgs(msgs);
		GsmsResponse resp = pm.post(ac, pack);
		LOGGER.info("sendBatch group code:" + resp.getResult() + ";message:"
				+ resp.getMessage());
	}

	/**
	 * 获取账号信息
	 * 
	 * @param pm
	 * @param account
	 * @throws Exception
	 */
	public void getAccountInfo() throws Exception {
		/* 获取账号详细信息 */
		LOGGER.info("accountinfo:" + pm.getAccountInfo(ac));
		/* 获取账号绑定业务类型 */
		BusinessType[] bizTypes = pm.getBizTypes(ac);
		if (bizTypes != null) {
			for (BusinessType bizType : bizTypes) {
				LOGGER.info("getAccountInfo:" + bizType);
			}
		}
	}

	/**
	 * 初始化参数
	 */
	@PostConstruct
	private void init() {
		ac = new Account(userName, passWord);
		pm = new PostMsg();
		/* 设置网关的IP和port，用于发送信息 */
		pm.getCmHost().setHost(ip, Integer.valueOf(cmport));
		/* 设置网关的 IP和port，用于获取账号信息、上行、状态报告等等 */
		pm.getWsHost().setHost(ip, Integer.valueOf(wsport));
	}
}
