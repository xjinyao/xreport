package com.xjinyao.report.core.expression.function.math;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.model.Cell;
import org.apache.commons.lang.math.RandomUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class RandomFunction extends MathFunction {
	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		int feed = 0;
		if (dataList.size() > 0) {
			BigDecimal data = buildBigDecimal(dataList);
			feed = data.intValue();
		}
		if (feed == 0) {
			return Math.random();
		}
		return RandomUtils.nextInt(feed);
	}

	@Override
	public String name() {
		return "random";
	}
}
