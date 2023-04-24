package com.xjinyao.report.core.expression.function;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.data.BindDataListExpressionData;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectListExpressionData;
import com.xjinyao.report.core.model.Cell;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class GetFunction implements Function {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		int index = 1;
		String propertyName = null;
		List<Object> list = new ArrayList<>();
		if (dataList.size() == 1) {
			buildList(dataList.get(0), list);
		} else if (dataList.size() == 2) {
			buildList(dataList.get(0), list);
			index = buildIndex(dataList.get(1));
		} else if (dataList.size() == 3) {
			buildList(dataList.get(0), list);
			index = buildIndex(dataList.get(1));
			ExpressionData<?> d = dataList.get(2);
			if (d instanceof ObjectExpressionData) {
				ObjectExpressionData exprData = (ObjectExpressionData) d;
				Object obj = exprData.getData();
				if (obj != null) {
					propertyName = obj.toString();
				}
			}
		}
		Object obj = null;
		if (list.size() > 0) {
			if (index <= list.size()) {
				obj = list.get(index - 1);
			} else {
				obj = list.get(list.size() - 1);
			}
		}
		if (StringUtils.isNotBlank(propertyName)) {
			obj = Utils.getProperty(obj, propertyName);
		}
		return obj;
	}

	private int buildIndex(ExpressionData<?> d) {
		int index = 1;
		if (d instanceof ObjectExpressionData) {
			ObjectExpressionData exprData = (ObjectExpressionData) d;
			Object obj = exprData.getData();
			if (obj != null) {
				index = Utils.toBigDecimal(obj).intValue();
			}
		}
		return index;
	}

	private void buildList(ExpressionData<?> d, List<Object> list) {
		if (d instanceof ObjectExpressionData) {
			ObjectExpressionData exprData = (ObjectExpressionData) d;
			list.add(exprData.getData());
		} else if (d instanceof ObjectListExpressionData) {
			ObjectListExpressionData listData = (ObjectListExpressionData) d;
			list.addAll(listData.getData());
		} else if (d instanceof BindDataListExpressionData) {
			BindDataListExpressionData listData = (BindDataListExpressionData) d;
			for (BindData bindData : listData.getData()) {
				list.addAll(bindData.getDataList());
			}
		}
	}

	@Override
	public String name() {
		return "get";
	}
}
