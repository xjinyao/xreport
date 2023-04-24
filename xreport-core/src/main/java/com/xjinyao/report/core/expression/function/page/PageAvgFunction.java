package com.xjinyao.report.core.expression.function.page;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectListExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class PageAvgFunction extends PageFunction {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		if (dataList == null) {
			return 0;
		}
		int size = 0;
		BigDecimal total = new BigDecimal(0);
		for (ExpressionData<?> exprData : dataList) {
			if (exprData instanceof ObjectListExpressionData) {
				ObjectListExpressionData listExpr = (ObjectListExpressionData) exprData;
				List<?> list = listExpr.getData();
				for (Object obj : list) {
					if (obj == null) {
						continue;
					}
					BigDecimal bigData = Utils.toBigDecimal(obj);
					total = total.add(bigData);
					size++;
				}
			}
		}
		return total.divide(new BigDecimal(size), 8, BigDecimal.ROUND_HALF_UP);
	}

	@Override
	public String name() {
		return "pavg";
	}
}
