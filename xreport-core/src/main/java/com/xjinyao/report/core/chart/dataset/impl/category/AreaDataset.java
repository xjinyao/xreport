package com.xjinyao.report.core.chart.dataset.impl.category;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.model.Cell;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class AreaDataset extends LineDataset {
	@Override
	public String buildDataJson(Context context, Cell cell) {
		String props = "\"fill\":true";
		String datasetJson = buildDatasetJson(context, cell, props);
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		String labels = getLabels();
		sb.append("\"labels\":" + labels + ",");
		sb.append("\"datasets\":[" + datasetJson + "]");
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String getType() {
		return "line";
	}
}
