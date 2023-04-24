package com.xjinyao.report.core.build.aggregate;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.definition.Order;
import com.xjinyao.report.core.expression.model.expr.dataset.DatasetExpression;
import com.xjinyao.report.core.model.Cell;
import com.xjinyao.report.core.utils.DataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SelectAggregate extends Aggregate {
	@Override
	public List<BindData> aggregate(DatasetExpression expr, Cell cell, Context context) {
		List<?> objList = DataUtils.fetchData(cell, context, expr.getDatasetName());
		return doAggregate(expr, cell, context, objList);
	}

	protected List<BindData> doAggregate(DatasetExpression expr, Cell cell, Context context, List<?> objList) {
		List<BindData> list = new ArrayList<>();
		Map<String, String> mappingMap = context.getMapping(expr);
		String property = expr.getProperty();
		for (Object o : objList) {
			boolean conditionResult = doCondition(expr.getCondition(), cell, o, context);
			if (!conditionResult) {
				continue;
			}
			List<Object> bindList = new ArrayList<>();
			bindList.add(o);
			Object data = Utils.getProperty(o, property);
			Object mappingData = mappingData(mappingMap, data);
			if (mappingData == null) {
				list.add(new BindData(data, bindList));
			} else {
				list.add(new BindData(data, mappingData, bindList));
			}
		}
		if (list.size() == 0) {
			List<Object> rowList = new ArrayList<>();
			rowList.add(new HashMap<>());
			list.add(new BindData("", rowList));
		}
		if (list.size() > 1) {
			Order order = expr.getOrder();
			orderBindDataList(list, order);
		}
		return list;
	}
}
