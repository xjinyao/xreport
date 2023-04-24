package com.xjinyao.report.core.expression.function.page;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectListExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class PageCountFunction extends PageFunction {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		if (dataList == null) {
			return 0;
		}
		int size = 0;
		for (ExpressionData<?> data : dataList) {
			if (data instanceof ObjectListExpressionData) {
				ObjectListExpressionData listExpressionData = (ObjectListExpressionData) data;
				size += listExpressionData.getData().size();
			} else if (data instanceof ObjectExpressionData) {
				size++;
			}
		}
		return size;
	}

	@Override
	public String name() {
		return "pcount";
	}

}
