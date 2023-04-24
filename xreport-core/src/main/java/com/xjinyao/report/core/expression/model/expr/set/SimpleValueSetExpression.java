package com.xjinyao.report.core.expression.model.expr.set;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.expr.BaseExpression;
import com.xjinyao.report.core.model.Cell;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SimpleValueSetExpression extends BaseExpression {
	private static final long serialVersionUID = -5433811018086391838L;
	private Object simpleValue;

	public SimpleValueSetExpression(Object simpleValue) {
		this.simpleValue = simpleValue;
	}

	@Override
	protected ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		return new ObjectExpressionData(simpleValue);
	}
}
