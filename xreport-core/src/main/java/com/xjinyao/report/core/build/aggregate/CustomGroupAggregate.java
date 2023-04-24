package com.xjinyao.report.core.build.aggregate;

import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.definition.Order;
import com.xjinyao.report.core.definition.value.GroupItem;
import com.xjinyao.report.core.expression.model.Condition;
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
public class CustomGroupAggregate extends Aggregate {

	@Override
	public List<BindData> aggregate(DatasetExpression expr, Cell cell, Context context) {
		List<?> objList = DataUtils.fetchData(cell, context, expr.getDatasetName());
		List<BindData> list = doAggregate(expr, cell, context, objList);
		return list;
	}

	protected List<BindData> doAggregate(DatasetExpression expr, Cell cell, Context context, List<?> objList) {
		List<BindData> list = new ArrayList<>();
		List<GroupItem> groupItems = expr.getGroupItems();
		if (objList.size() == 0) {
			list.add(new BindData(""));
			return list;
		} else if (objList.size() == 1) {
			Object o = objList.get(0);
			boolean conditionResult = doCondition(expr.getCondition(), cell, o, context);
			if (!conditionResult) {
				list.add(new BindData(""));
				return list;
			}
			String itemName = groupData(groupItems, cell, context, o);
			if (itemName == null) {
				list.add(new BindData(""));
				return list;
			}
			List<Object> rowList = new ArrayList<>();
			rowList.add(o);
			list.add(new BindData(itemName, rowList));
			return list;
		}
		Map<Object, List<Object>> map = new HashMap<>();
		for (Object o : objList) {
			boolean conditionResult = doCondition(expr.getCondition(), cell, o, context);
			if (!conditionResult) {
				continue;
			}
			String itemName = groupData(groupItems, cell, context, o);
			if (itemName == null) {
				continue;
			}
			List<Object> rowList = null;
			if (map.containsKey(itemName)) {
				rowList = map.get(itemName);
			} else {
				rowList = new ArrayList<>();
				map.put(itemName, rowList);
				list.add(new BindData(itemName, rowList));
			}
			rowList.add(o);
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

	private String groupData(List<GroupItem> groupItems, Cell cell, Context context, Object o) {
		for (GroupItem item : groupItems) {
			Condition condition = item.getCondition();
			boolean doCondition = doCondition(condition, cell, o, context);
			if (doCondition) {
				return item.getName();
			}
		}
		return null;
	}
}
