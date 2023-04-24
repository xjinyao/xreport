package com.xjinyao.report.core.expression.model.expr;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.Expression;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ExpressionBlock extends BaseExpression {
	private static final long serialVersionUID = -400528304334443664L;
	private List<Expression> expressionList;
	private Expression returnExpression;

	@Override
	protected ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		ExpressionData<?> data = null;
		if (expressionList != null) {
			for (Expression expr : expressionList) {
				data = expr.execute(cell, currentCell, context);
			}
		}
		if (returnExpression != null) {
			data = returnExpression.execute(cell, currentCell, context);
		}
		return data;
	}

	public List<Expression> getExpressionList() {
		return expressionList;
	}

	public void setExpressionList(List<Expression> expressionList) {
		this.expressionList = expressionList;
	}

	public Expression getReturnExpression() {
		return returnExpression;
	}

	public void setReturnExpression(Expression returnExpression) {
		this.returnExpression = returnExpression;
	}
}
