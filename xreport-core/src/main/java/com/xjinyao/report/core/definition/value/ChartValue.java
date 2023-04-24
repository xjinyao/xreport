package com.xjinyao.report.core.definition.value;

import com.xjinyao.report.core.chart.Chart;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ChartValue implements Value {
	private Chart chart;

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public ValueType getType() {
		return ValueType.chart;
	}

	public Chart getChart() {
		return chart;
	}

	public void setChart(Chart chart) {
		this.chart = chart;
	}
}
