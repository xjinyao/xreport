package com.xjinyao.report.core.expression.function;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.model.Cell;
import com.xjinyao.report.core.model.Row;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class RowFunction implements Function {
	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		Row row = currentCell.getRow();
		return row.getRowNumber();
	}

	@Override
	public String name() {
		return "row";
	}
}
