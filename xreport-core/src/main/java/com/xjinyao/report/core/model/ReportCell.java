package com.xjinyao.report.core.model;

import com.xjinyao.report.core.definition.CellStyle;
import com.xjinyao.report.core.definition.Expand;
import com.xjinyao.report.core.definition.value.Value;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface ReportCell {
	CellStyle getCellStyle();

	String getName();

	int getRowSpan();

	int getColSpan();

	Row getRow();

	Column getColumn();

	Object getData();

	Value getValue();

	Expand getExpand();

	List<Object> getBindData();
}
