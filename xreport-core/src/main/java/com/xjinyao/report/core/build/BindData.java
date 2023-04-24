package com.xjinyao.report.core.build;

import java.util.List;


/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class BindData {
	private Object value;
	private Object label;
	private List<Object> dataList;

	public BindData(Object value) {
		this.value = value;
	}

	public BindData(Object value, List<Object> dataList) {
		this.value = value;
		this.dataList = dataList;
	}

	public BindData(Object value, Object label, List<Object> dataList) {
		this.value = value;
		this.label = label;
		this.dataList = dataList;
	}

	public Object getValue() {
		return value;
	}

	public List<Object> getDataList() {
		return dataList;
	}

	public Object getLabel() {
		return label;
	}
}
