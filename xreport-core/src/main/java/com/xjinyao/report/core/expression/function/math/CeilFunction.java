package com.xjinyao.report.core.expression.function.math;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class CeilFunction extends MathFunction {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		BigDecimal data = buildBigDecimal(dataList);
		int pos = 0;
		if (dataList.size() == 2) {
			ExpressionData<?> exprData = dataList.get(1);
			if (exprData instanceof ObjectExpressionData) {
				ObjectExpressionData objData = (ObjectExpressionData) exprData;
				Object obj = objData.getData();
				if (obj == null) {
					throw new ReportComputeException("Ceil Function second parameter can not be null.");
				}
				pos = Utils.toBigDecimal(obj).intValue();
			}
		}
		data.setScale(pos, BigDecimal.ROUND_CEILING);
		return Math.ceil(data.doubleValue());
	}

	@Override
	public String name() {
		return "ceil";
	}
}
