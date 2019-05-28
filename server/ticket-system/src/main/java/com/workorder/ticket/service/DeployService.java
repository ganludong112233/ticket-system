package com.workorder.ticket.service;

import java.util.Date;
import java.util.List;

import com.workorder.ticket.controller.vo.common.FlowStepItem;
import com.workorder.ticket.controller.vo.common.HistogramItem;
import com.workorder.ticket.controller.vo.common.TicketOpLog;
import com.workorder.ticket.service.bo.deploy.DeployBo;
import com.workorder.ticket.service.bo.deploy.DeployEditBo;
import com.workorder.ticket.service.bo.deploy.DeployQueryBo;

/**
 * 部署申请服务
 * 
 * @author wzdong
 * @Date 2019年3月25日
 * @version 1.0
 */
public interface DeployService {
	/**
	 * 新建部署申请
	 * 
	 * @param initVo
	 * @return
	 */
	public void create(DeployEditBo initBo);

	/**
	 * 编辑部署申请
	 * 
	 * @param editVo
	 * @return
	 */
	public void edit(DeployEditBo editBo);

	/**
	 * 删除部署申请
	 * 
	 * @param editVo
	 * @return
	 */
	public void delete(Long deployId);

	/**
	 * 撤销部署申请
	 * 
	 * @param editVo
	 * @return
	 */
	public void revoke(Long deployId);

	/**
	 * 提交部署申请
	 * 
	 * @param DeployId
	 * @return
	 */
	public void submit(Long deployId);

	/**
	 * 查询部署申请详情
	 * 
	 * @return
	 */
	public DeployBo getById(Long deployId);

	/**
	 * 查询部署申请处理日志
	 * 
	 * @return
	 */
	public List<TicketOpLog> getOpLogs(Long DeployId);

	/**
	 * 查询部署申请处理步骤
	 * 
	 * @return
	 */
	public List<FlowStepItem> getFlowSteps(Long DeployId);

	/**
	 * 查询所有部署申请
	 * 
	 * @param queryVo
	 * @return
	 */
	public List<DeployBo> queryAllList(DeployQueryBo queryBo);

	/**
	 * 查询所有部署申请Count
	 * 
	 * @param queryVo
	 * @return
	 */
	public int queryAllListCount(DeployQueryBo queryBo);

	/**
	 * 查询所有待处理的部署申请
	 * 
	 * @param queryVo
	 * @return
	 */
	public List<DeployBo> queryWaitingList();

	/**
	 * 查询所有待处理的部署申请Count
	 * 
	 * @param queryVo
	 * @return
	 */
	public int queryWaitingListCount();

	/**
	 * 查询用户处理的部署申请历史
	 * 
	 * @param queryVo
	 */
	public List<DeployBo> queryHistoryList(DeployQueryBo queryBo);

	/**
	 * 查询用户处理的部署申请历史Count
	 * 
	 * @param queryVo
	 */
	public int queryHistoryListCount(DeployQueryBo queryBo);

	/**
	 * 处理任务
	 * 
	 * @return
	 */
	public void completeTask(Long deployId, Boolean result, String comment);

	/**
	 * 按天统计部署申请
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<HistogramItem> statisticByDay(Date start, Date end);

}
