package com.xjinyao.report.core.expression.function;

import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.expression.model.data.BindDataListExpressionData;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectListExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class FormatDateFunction implements Function {
	private final String defaultPattern = "yyyy-MM-dd HH:mm:ss";

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		if (dataList == null) {
			return "";
		}
		Object obj = null;
		String pattern = defaultPattern;
		for (ExpressionData<?> data : dataList) {
			if (data instanceof ObjectListExpressionData) {
				ObjectListExpressionData listExpressionData = (ObjectListExpressionData) data;
				List<?> list = listExpressionData.getData();
				if (list.size() > 0) {
					obj = list.get(0);
				}
				if (list.size() > 1) {
					pattern = list.get(1).toString();
				}
			} else if (data instanceof ObjectExpressionData) {
				obj = ((ObjectExpressionData) data).getData();
			} else if (data instanceof BindDataListExpressionData) {
				BindDataListExpressionData bindDataList = (BindDataListExpressionData) data;
				List<BindData> list = bindDataList.getData();
				if (list.size() > 0) {
					obj = list.get(0).getValue();
				}
				if (list.size() > 1) {
					pattern = list.get(1).getValue().toString();
				}
			}
		}
		if (obj == null) {
			throw new ReportComputeException("Function [formatdate] need a Date type parameter at least");
		} else {
			if (obj instanceof Date) {
				SimpleDateFormat sd = new SimpleDateFormat(pattern);
				return sd.format((Date) obj);
			} else {
				throw new ReportComputeException("Function [formatdate] first parameter is Date type");
			}
		}
	}

	@Override
	public String name() {
		return "formatdate";
	}
}
