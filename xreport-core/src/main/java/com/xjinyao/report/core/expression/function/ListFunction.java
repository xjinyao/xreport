package com.xjinyao.report.core.expression.function;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectListExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ListFunction implements Function {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		List<Object> list = new ArrayList<>();
		for (ExpressionData<?> d : dataList) {
			if (d instanceof ObjectExpressionData) {
				ObjectExpressionData exprData = (ObjectExpressionData) d;
				list.add(exprData.getData());
			} else if (d instanceof ObjectListExpressionData) {
				ObjectListExpressionData listData = (ObjectListExpressionData) d;
				list.addAll(listData.getData());
			}
		}
		return list;
	}

	@Override
	public String name() {
		return "list";
	}
}
