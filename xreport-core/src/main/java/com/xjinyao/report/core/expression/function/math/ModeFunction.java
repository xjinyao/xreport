package com.xjinyao.report.core.expression.function.math;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 求众数
 *
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ModeFunction extends MathFunction {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		int max = 0;
		BigDecimal result = null;
		List<BigDecimal> list = buildDataList(dataList);
		Map<Double, Integer> map = new HashMap<>();
		for (BigDecimal bigData : list) {
			if (bigData == null) continue;
			double d = bigData.doubleValue();
			if (map.containsKey(d)) {
				int count = map.get(d);
				count++;
				map.put(d, count);
				if (count > max) {
					max = count;
					result = bigData;
				}
			} else {
				map.put(d, 1);
				if (result == null) {
					max = 1;
					result = bigData;
				}
			}
		}
		return result;
	}

	@Override
	public String name() {
		return "mode";
	}
}
