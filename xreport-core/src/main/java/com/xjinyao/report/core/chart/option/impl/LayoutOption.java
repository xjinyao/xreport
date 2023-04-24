package com.xjinyao.report.core.chart.option.impl;

import com.xjinyao.report.core.chart.option.Option;
import com.xjinyao.report.core.chart.option.Padding;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class LayoutOption implements Option {
	private Padding padding;

	@Override
	public String buildOptionJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"layout\":{");
		sb.append("\"padding\":" + padding.toJson());
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String getType() {
		return "layout";
	}

	public Padding getPadding() {
		return padding;
	}

	public void setPadding(Padding padding) {
		this.padding = padding;
	}
}
