package com.workorder.ticket.controller.vo.common;

/**
 * 柱状图Item
 * 
 * @author wzdong
 * @Date 2019年3月11日
 * @version 1.0
 */
public class HistogramItem {
	private String item;
	private Long value;

	public HistogramItem() {
	}

	public HistogramItem(String item) {
		this.item = item;
		this.value = 0L;
	}

	public HistogramItem(String item, Long value) {
		this.item = item;
		this.value = value;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

}
