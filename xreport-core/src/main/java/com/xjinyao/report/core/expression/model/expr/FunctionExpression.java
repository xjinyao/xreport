package com.xjinyao.report.core.expression.model.expr;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.expression.ExpressionUtils;
import com.xjinyao.report.core.expression.function.Function;
import com.xjinyao.report.core.expression.function.page.PageFunction;
import com.xjinyao.report.core.expression.model.Expression;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectListExpressionData;
import com.xjinyao.report.core.expression.model.expr.set.CellExpression;
import com.xjinyao.report.core.model.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class FunctionExpression extends BaseExpression {
	private static final long serialVersionUID = -6981657541024043558L;
	private String name;
	private List<BaseExpression> expressions;

	@Override
	public ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		Map<String, Function> functions = ExpressionUtils.getFunctions();
		Function targetFunction = functions.get(name);
		if (targetFunction == null) {
			throw new ReportComputeException("Function [" + name + "] not exist.");
		}
		List<ExpressionData<?>> dataList = new ArrayList<>();
		if (expressions != null) {
			for (BaseExpression expr : expressions) {
				if (targetFunction instanceof PageFunction) {
					ExpressionData<?> data = buildPageExpressionData(expr, cell, currentCell, context);
					dataList.add(data);
				} else {
					ExpressionData<?> data = expr.execute(cell, currentCell, context);
					dataList.add(data);
				}
			}
		}
		Object obj = targetFunction.execute(dataList, context, currentCell);
		if (obj instanceof List) {
			return new ObjectListExpressionData((List<?>) obj);
		}
		return new ObjectExpressionData(obj);
	}

	private ExpressionData<?> buildPageExpressionData(Expression expr, Cell cell, Cell currentCell, Context context) {
		if (expr instanceof CellExpression) {
			CellExpression cellExpr = (CellExpression) expr;
			if (cellExpr.supportPaging()) {
				return cellExpr.computePageCells(cell, currentCell, context);
			} else {
				return cellExpr.execute(cell, currentCell, context);
			}
		} else {
			return expr.execute(cell, currentCell, context);
		}
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BaseExpression> getExpressions() {
		return expressions;
	}

	public void setExpressions(List<BaseExpression> expressions) {
		this.expressions = expressions;
	}
}
