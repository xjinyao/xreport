package com.xjinyao.report.core.expression.function.string;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class IndexOfFunction extends StringFunction {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		String text = buildString(dataList);
		String targetText = null;
		if (dataList.size() > 1) {
			ExpressionData<?> exprData = dataList.get(1);
			if (exprData instanceof ObjectExpressionData) {
				ObjectExpressionData objData = (ObjectExpressionData) exprData;
				Object obj = objData.getData();
				if (obj == null) {
					throw new ReportComputeException("Function [" + name() + "] parameter can not be null.");
				}
				targetText = obj.toString();
			}
		}
		int start = 0;
		if (dataList.size() == 3) {
			ExpressionData<?> exprData = dataList.get(2);
			start = buildStart(exprData);
		}
		return text.indexOf(targetText, start);
	}

	private int buildStart(ExpressionData<?> exprData) {
		if (exprData instanceof ObjectExpressionData) {
			ObjectExpressionData objData = (ObjectExpressionData) exprData;
			Object obj = objData.getData();
			if (obj == null) {
				throw new ReportComputeException("Function [" + name() + "] parameter can not be null.");
			}
			return Utils.toBigDecimal(obj).intValue();
		}
		throw new ReportComputeException("Function [" + name() + "] start position data is invalid : " + exprData);
	}

	@Override
	public String name() {
		return "indexof";
	}

}
