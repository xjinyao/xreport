package com.xjinyao.report.core.expression.function;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectListExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.util.List;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ParameterFunction implements Function {
	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		if (dataList == null || dataList.size() < 1) {
			throw new ReportComputeException("Function [param] need one parameter.");
		}
		Object obj = null;
		ExpressionData<?> data = dataList.get(0);
		if (data instanceof ObjectExpressionData) {
			ObjectExpressionData objData = (ObjectExpressionData) data;
			obj = objData.getData();
		} else if (data instanceof ObjectListExpressionData) {
			ObjectListExpressionData listData = (ObjectListExpressionData) data;
			List<?> list = listData.getData();
			if (list.size() > 0) {
				obj = list.get(0);
			}
		}
		if (obj == null) {
			throw new ReportComputeException("Function [param] need one parameter.");
		}
		Map<String, Object> map = context.getParameters();
		if (map == null) {
			return null;
		}
		return map.get(obj.toString());
	}

	@Override
	public String name() {
		return "param";
	}
}
