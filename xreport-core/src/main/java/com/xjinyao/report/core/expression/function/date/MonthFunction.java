package com.xjinyao.report.core.expression.function.date;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.util.Calendar;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class MonthFunction extends CalendarFunction {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		Calendar c = buildCalendar(dataList);
		int month = c.get(Calendar.MONTH) + 1;
		return month + 1;
	}

	@Override
	public String name() {
		return "month";
	}
}
