package com.xjinyao.report.core.chart.dataset;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.model.Cell;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface Dataset {
	String buildDataJson(Context context, Cell cell);

	String getType();
}
