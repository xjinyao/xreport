package com.xjinyao.report.core.expression.function.string;

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
public class ReplaceFunction extends StringFunction {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		if (dataList.size() != 3) {
			throw new ReportComputeException("Function [" + name() + "] need three parameters.");
		}
		String text = buildString(dataList);
		String targetText = null, replaceText = null;
		ExpressionData<?> exprData = dataList.get(1);
		if (exprData instanceof ObjectExpressionData) {
			ObjectExpressionData objData = (ObjectExpressionData) exprData;
			Object obj = objData.getData();
			if (obj == null) {
				throw new ReportComputeException("Function [" + name() + "] parameter can not be null.");
			}
			targetText = obj.toString();
		}
		exprData = dataList.get(2);
		replaceText = buildStart(exprData);
		return text.replaceAll(targetText, replaceText);
	}

	private String buildStart(ExpressionData<?> exprData) {
		if (exprData instanceof ObjectExpressionData) {
			ObjectExpressionData objData = (ObjectExpressionData) exprData;
			Object obj = objData.getData();
			if (obj == null) {
				throw new ReportComputeException("Function [" + name() + "] parameter can not be null.");
			}
			return obj.toString();
		}
		throw new ReportComputeException("Function [" + name() + "] start position data is invalid : " + exprData);
	}

	@Override
	public String name() {
		return "replace";
	}

}
