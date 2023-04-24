package com.xjinyao.report.core.build;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class Dataset {
	private String name;
	private List<?> data;

	public Dataset(String name, List<?> data) {
		this.name = name;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public List<?> getData() {
		return data;
	}
}
