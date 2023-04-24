package com.xjinyao.report.core.expression.model.expr;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.NoneExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class CurrentCellDataExpression extends BaseExpression {
	private static final long serialVersionUID = 7517926036810650110L;
	private String property;

	@Override
	protected ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		List<Object> bindDataList = cell.getBindData();
		if (bindDataList == null || bindDataList.size() == 0) {
			return new NoneExpressionData();
		}
		Object obj = bindDataList.get(0);
		Object data = Utils.getProperty(obj, property);
		return new ObjectExpressionData(data);
	}

	public void setProperty(String property) {
		this.property = property;
	}
}
