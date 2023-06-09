package com.xjinyao.report.core.build.aggregate;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.definition.value.Value;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.expression.model.Condition;
import com.xjinyao.report.core.expression.model.expr.dataset.DatasetExpression;
import com.xjinyao.report.core.model.Cell;
import com.xjinyao.report.core.utils.DataUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class SumAggregate extends Aggregate {
	@Override
	public List<BindData> aggregate(DatasetExpression expr, Cell cell, Context context) {
		String datasetName = expr.getDatasetName();
		String property = expr.getProperty();
		Cell leftCell = DataUtils.fetchLeftCell(cell, context, datasetName);
		Cell topCell = DataUtils.fetchTopCell(cell, context, datasetName);
		List<Object> leftList = null, topList = null;
		if (leftCell != null) {
			leftList = leftCell.getBindData();
		}
		if (topCell != null) {
			topList = topCell.getBindData();
		}
		BigDecimal result = null;
		if (leftList == null && topList == null) {
			List<?> data = context.getDatasetData(datasetName);
			result = buildSum(data, property, cell, expr, context);
		} else if (leftList == null) {
			result = buildSum(topList, property, cell, expr, context);
		} else if (topList == null) {
			result = buildSum(leftList, property, cell, expr, context);
		} else {
			List<Object> list = null;
			Object data = null;
			String prop = null;
			if (leftList.size() > topList.size()) {
				list = topList;
				data = leftCell.getData();
				Value value = leftCell.getValue();
				DatasetExpression de = DataUtils.fetchDatasetExpression(value);
				if (de == null) {
					throw new ReportComputeException("Unsupport value : " + value);
				}
				prop = de.getProperty();
			} else {
				list = leftList;
				data = topCell.getData();
				Value value = topCell.getValue();
				DatasetExpression de = DataUtils.fetchDatasetExpression(value);
				if (de == null) {
					throw new ReportComputeException("Unsupport value : " + value);
				}
				prop = de.getProperty();
			}
			Condition condition = getCondition(cell);
			if (condition == null) {
				condition = expr.getCondition();
			}
			result = new BigDecimal(0);
			for (Object obj : list) {
				if (condition != null && !condition.filter(cell, cell, obj, context)) {
					continue;
				}
				Object o = Utils.getProperty(obj, prop);
				if ((o != null && data != null && (o == data || o.equals(data))) || (o == null && data == null)) {
					Object value = Utils.getProperty(obj, property);
					if (value == null || value.toString().equals("")) {
						continue;
					}
					result = result.add(Utils.toBigDecimal(value));
				}
			}
		}
		List<BindData> list = new ArrayList<>();
		list.add(new BindData(result.doubleValue(), null));
		return list;
	}

	private BigDecimal buildSum(List<?> list, String property, Cell cell, DatasetExpression expr, Context context) {
		BigDecimal result = new BigDecimal(0);
		Condition condition = getCondition(cell);
		if (condition == null) {
			condition = expr.getCondition();
		}
		for (Object obj : list) {
			if (condition != null && !condition.filter(cell, cell, obj, context)) {
				continue;
			}
			Object value = Utils.getProperty(obj, property);
			if (value == null || value.toString().equals("")) {
				continue;
			}
			result = result.add(Utils.toBigDecimal(value));
		}
		return result;
	}
}
