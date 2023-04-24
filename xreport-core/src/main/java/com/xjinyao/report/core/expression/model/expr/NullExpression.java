package com.xjinyao.report.core.expression.model.expr;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.NoneExpressionData;
import com.xjinyao.report.core.model.Cell;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class NullExpression extends BaseExpression {
	private static final long serialVersionUID = -5448531052217619991L;

	@Override
	public ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		return new NoneExpressionData();
	}
}
