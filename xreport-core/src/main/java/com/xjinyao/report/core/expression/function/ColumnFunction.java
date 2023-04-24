package com.xjinyao.report.core.expression.function;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.model.Cell;
import com.xjinyao.report.core.model.Column;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ColumnFunction implements Function {
	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		Column col = currentCell.getColumn();
		return col.getColumnNumber();
	}

	@Override
	public String name() {
		return "column";
	}
}
