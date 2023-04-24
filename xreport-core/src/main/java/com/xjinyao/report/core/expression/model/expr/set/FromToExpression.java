package com.xjinyao.report.core.expression.model.expr.set;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectListExpressionData;
import com.xjinyao.report.core.expression.model.expr.BaseExpression;
import com.xjinyao.report.core.model.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class FromToExpression extends BaseExpression {
	private static final long serialVersionUID = -3250140935488901894L;
	private BaseExpression fromExpression;
	private BaseExpression toExpression;

	public FromToExpression(BaseExpression fromExpression, BaseExpression toExpression) {
		this.fromExpression = fromExpression;
		this.toExpression = toExpression;
	}

	@Override
	protected ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		Object fromData = fromExpression.execute(cell, currentCell, context);
		Object toData = toExpression.execute(cell, currentCell, context);
		int from = convertFloatData(fromData), to = convertFloatData(toData);
		List<Integer> list = new ArrayList<>();
		for (int i = from; i <= to; i++) {
			list.add(i);
		}
		return new ObjectListExpressionData(list);
	}

	private int convertFloatData(Object data) {
		if (data instanceof ObjectExpressionData) {
			Object obj = ((ObjectExpressionData) data).getData();
			return Utils.toBigDecimal(obj).intValue();
		} else {
			throw new ReportComputeException("Can not convert [" + data + "] to integer.");
		}
	}
}
