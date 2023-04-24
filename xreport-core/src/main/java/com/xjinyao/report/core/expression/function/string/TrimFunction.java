package com.xjinyao.report.core.expression.function.string;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class TrimFunction extends StringFunction {
	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		String text = buildString(dataList);
		return text.trim();
	}

	@Override
	public String name() {
		return "trim";
	}
}
