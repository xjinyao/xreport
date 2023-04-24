package com.xjinyao.report.core.chart.dataset.impl.category;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.model.Cell;

import java.util.List;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class PieDataset extends CategoryDataset {
	@Override
	public String buildDataJson(Context context, Cell cell) {
		String datasetJson = buildDatasetJson(context, cell, null);
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		String labels = getLabels();
		sb.append("\"labels\":" + labels + ",");
		sb.append("\"datasets\":[" + datasetJson + "]");
		sb.append("}");
		return sb.toString();
	}

	@Override
	protected String buildDatasets(Map<Object, Map<Object, List<Object>>> map, String props) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Object series : map.keySet()) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append("{");
			sb.append("\"label\":\"" + series + "\",");
			Map<Object, List<Object>> categoryMap = map.get(series);
			sb.append("\"backgroundColor\":" + buildBackgroundColor(i, categoryMap.size()) + ",");
			sb.append("\"data\":" + buildData(categoryMap));
			if (props != null) {
				sb.append("," + props);
			}
			sb.append("}");
			i++;
		}
		return sb.toString();
	}

	private String buildBackgroundColor(int start, int size) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = start; i < size; i++) {
			String color = getRgbColor(i);
			if (sb.length() > 1) {
				sb.append(",");
			}
			sb.append("\"rgb(" + color + ")\"");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public String getType() {
		return "pie";
	}
}
