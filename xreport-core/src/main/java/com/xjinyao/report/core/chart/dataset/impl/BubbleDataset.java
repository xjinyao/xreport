package com.xjinyao.report.core.chart.dataset.impl;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.chart.dataset.BaseDataset;
import com.xjinyao.report.core.chart.dataset.BubbleData;
import com.xjinyao.report.core.model.Cell;
import com.xjinyao.report.core.utils.DataUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class BubbleDataset extends BaseDataset {
	private String datasetName;
	private String xProperty;
	private String yProperty;
	private String rProperty;
	private String categoryProperty;

	@Override
	public String buildDataJson(Context context, Cell cell) {
		Map<Object, List<BubbleData>> map = new LinkedHashMap<>();
		List<?> dataList = DataUtils.fetchData(cell, context, datasetName);
		for (Object obj : dataList) {
			if (obj == null) {
				continue;
			}
			Object categoryValue = Utils.getProperty(obj, categoryProperty);
			if (categoryValue == null) {
				continue;
			}
			Object xValue = Utils.getProperty(obj, xProperty);
			Object yValue = Utils.getProperty(obj, yProperty);
			Object rValue = Utils.getProperty(obj, rProperty);
			if (xValue == null || yValue == null || rValue == null) {
				continue;
			}
			List<BubbleData> list = null;
			if (map.containsKey(categoryValue)) {
				list = map.get(categoryValue);
			} else {
				list = new ArrayList<>();
				map.put(categoryValue, list);
			}
			double x = Utils.toBigDecimal(xValue).doubleValue();
			double y = Utils.toBigDecimal(yValue).doubleValue();
			double r = Utils.toBigDecimal(rValue).doubleValue();
			list.add(new BubbleData(x, y, r));
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"datasets\":[");
		int index = 0;
		for (Object obj : map.keySet()) {
			if (index > 0) {
				sb.append(",");
			}
			sb.append("{");
			sb.append("\"borderColor\":\"rgb(" + getRgbColor(index) + ")\",");
			sb.append("\"backgroundColor\":\"rgba(" + getRgbColor(index) + ",0.5)\",");
			sb.append("\"label\":\"" + obj + "\",");
			sb.append("\"data\":[");
			List<BubbleData> list = map.get(obj);
			int i = 0;
			for (BubbleData data : list) {
				if (i > 0) {
					sb.append(",");
				}
				i++;
				sb.append("{");
				sb.append("\"x\":" + data.getX() + ",");
				sb.append("\"y\":" + data.getY() + ",");
				sb.append("\"r\":" + data.getR());
				sb.append("}");
			}
			sb.append("]");
			sb.append("}");
			index++;
		}
		sb.append("]}");
		return sb.toString();
	}

	@Override
	public String getType() {
		return "bubble";
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public String getCategoryProperty() {
		return categoryProperty;
	}

	public void setCategoryProperty(String categoryProperty) {
		this.categoryProperty = categoryProperty;
	}

	public String getxProperty() {
		return xProperty;
	}

	public void setxProperty(String xProperty) {
		this.xProperty = xProperty;
	}

	public String getyProperty() {
		return yProperty;
	}

	public void setyProperty(String yProperty) {
		this.yProperty = yProperty;
	}

	public String getrProperty() {
		return rProperty;
	}

	public void setrProperty(String rProperty) {
		this.rProperty = rProperty;
	}
}
