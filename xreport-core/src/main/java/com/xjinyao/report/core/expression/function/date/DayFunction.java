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
public class DayFunction extends CalendarFunction {
	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		Calendar c = buildCalendar(dataList);
		int day = c.get(Calendar.DAY_OF_MONTH);
		return day;
	}

	@Override
	public String name() {
		return "day";
	}
}
