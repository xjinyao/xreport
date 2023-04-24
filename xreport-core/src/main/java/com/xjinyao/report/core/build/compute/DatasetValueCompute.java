package com.xjinyao.report.core.build.compute;

import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.build.DatasetUtils;
import com.xjinyao.report.core.definition.value.ValueType;
import com.xjinyao.report.core.expression.model.expr.dataset.DatasetExpression;
import com.xjinyao.report.core.model.Cell;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class DatasetValueCompute implements ValueCompute {
	@Override
	public List<BindData> compute(Cell cell, Context context) {
		DatasetExpression expr = (DatasetExpression) cell.getValue();
		return DatasetUtils.computeDatasetExpression(expr, cell, context);
	}

	@Override
	public ValueType type() {
		return ValueType.dataset;
	}
}
