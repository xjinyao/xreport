package com.xjinyao.report.core.chart.plugins;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class DataLabelsPlugin implements Plugin {
	private boolean display;

	@Override
	public String getName() {
		return "data-labels";
	}

	@Override
	public String toJson(String type) {
		StringBuilder sb = new StringBuilder();
		sb.append("\"datalabels\":{\"display\":" + display + ",");
		sb.append("\"font\":{");
		sb.append("\"size\":14,");
		sb.append("\"weight\":\"bold\"");
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}
}
