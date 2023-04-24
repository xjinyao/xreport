package com.xjinyao.report.core.build.paging;

import com.xjinyao.report.core.model.Row;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class RepeatRows {
	private List<Row> headerRepeatRows;
	private List<Row> footerRepeatRows;

	public RepeatRows(List<Row> headerRepeatRows, List<Row> footerRepeatRows) {
		this.headerRepeatRows = headerRepeatRows;
		this.footerRepeatRows = footerRepeatRows;
	}

	public List<Row> getFooterRepeatRows() {
		return footerRepeatRows;
	}

	public List<Row> getHeaderRepeatRows() {
		return headerRepeatRows;
	}
}
