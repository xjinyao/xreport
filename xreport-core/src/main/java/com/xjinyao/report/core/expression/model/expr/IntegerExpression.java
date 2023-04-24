package com.xjinyao.report.core.expression.model.expr;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.model.Cell;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class IntegerExpression extends BaseExpression {
	private static final long serialVersionUID = -8903608366537307519L;
	private Integer value;

	public IntegerExpression(Integer value) {
		this.value = value;
	}

	@Override
	public ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		return new ObjectExpressionData(value);
	}
}
