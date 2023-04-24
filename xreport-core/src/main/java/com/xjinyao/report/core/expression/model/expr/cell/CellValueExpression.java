package com.xjinyao.report.core.expression.model.expr.cell;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.expr.BaseExpression;
import com.xjinyao.report.core.model.Cell;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class CellValueExpression extends BaseExpression {
	private static final long serialVersionUID = 5964924636009364350L;

	@Override
	protected ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		while (!context.isCellPocessed(cell.getName())) {
			context.getReportBuilder().buildCell(context, null);
		}
		return new ObjectExpressionData(cell.getData());
	}
}
