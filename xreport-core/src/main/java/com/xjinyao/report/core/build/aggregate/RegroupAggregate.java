package com.xjinyao.report.core.build.aggregate;

import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.expr.dataset.DatasetExpression;
import com.xjinyao.report.core.model.Cell;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class RegroupAggregate extends GroupAggregate {
	@Override
	public List<BindData> aggregate(DatasetExpression expr, Cell cell, Context context) {
		List<?> objList = context.getDatasetData(expr.getDatasetName());
		return doAggregate(expr, cell, context, objList);
	}
}
