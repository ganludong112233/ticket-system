package com.workorder.ticket.common.item;

import java.util.Date;

/**
 * 时间区间参数
 * 
 * @author wzdong
 * @Date 2018年12月26日
 * @version 1.0
 */
public class DateRange {
	private Date begin;
	private Date end;

	public DateRange() {
	}

	public DateRange(Date begin, Date end) {
		this.begin = begin;
		this.end = end;
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

}
