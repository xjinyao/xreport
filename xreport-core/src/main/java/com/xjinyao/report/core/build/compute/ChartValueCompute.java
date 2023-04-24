package com.xjinyao.report.core.build.compute;

import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.chart.Chart;
import com.xjinyao.report.core.chart.ChartData;
import com.xjinyao.report.core.definition.value.ChartValue;
import com.xjinyao.report.core.definition.value.ValueType;
import com.xjinyao.report.core.model.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ChartValueCompute implements ValueCompute {

	@Override
	public List<BindData> compute(Cell cell, Context context) {
		ChartValue chartValue = (ChartValue) cell.getValue();
		Chart chart = chartValue.getChart();
		ChartData data = chart.doCompute(cell, context);
		List<BindData> list = new ArrayList<>();
		list.add(new BindData(data));
		return list;
	}

	@Override
	public ValueType type() {
		return ValueType.chart;
	}
}
