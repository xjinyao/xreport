package com.xjinyao.report.core.expression.function;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ParameterIsEmptyFunction extends ParameterFunction {
	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context,
						  Cell currentCell) {
		Object obj = super.execute(dataList, context, currentCell);
		if (obj == null || obj.toString().trim().equals("")) {
			return true;
		}
		return false;
	}

	@Override
	public String name() {
		return "emptyparam";
	}
}
