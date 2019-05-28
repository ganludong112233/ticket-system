package com.workorder.ticket.service.common;

/**
 * 短信发送服务接口
 * 
 * @author LZY
 * @create 2015-11-17
 */
public interface SmsService {

	/**
	 * 单发 一对一
	 * 
	 * @param mobile
	 *            手机号码
	 * @param content
	 *            短信内容
	 * @throws Exception
	 */
	public void send(String mobile, String content) throws Exception;

	/**
	 * 群发，多对一
	 * 
	 * @param mobile
	 * @param content
	 * @throws Exception
	 */
	public void sendBatch(String mobile, String content) throws IllegalArgumentException, Exception;

	/**
	 * 组发， 多对多
	 * 
	 * @param mobile
	 * @param content
	 * @throws Exception
	 */
	public void sendBatch(String[] mobile, String[] content) throws IllegalArgumentException, Exception;

	/**
	 * 获取账号信息
	 * 
	 * @param pm
	 * @param account
	 * @throws Exception
	 */
	public void getAccountInfo() throws Exception;

}
