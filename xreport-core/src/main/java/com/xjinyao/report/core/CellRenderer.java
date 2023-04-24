package com.xjinyao.report.core;

import com.xjinyao.report.core.model.ReportCell;

import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface CellRenderer {
	Object doRender(ReportCell cell, Map<String, Object> parameters);
}
