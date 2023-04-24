package com.xjinyao.report.core.build;

import com.xjinyao.report.core.build.aggregate.*;
import com.xjinyao.report.core.definition.value.AggregateType;
import com.xjinyao.report.core.exception.CellComputeException;
import com.xjinyao.report.core.expression.model.expr.dataset.DatasetExpression;
import com.xjinyao.report.core.model.Cell;
import com.xjinyao.report.core.build.aggregate.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class DatasetUtils {
	private static Map<AggregateType, Aggregate> aggregates = new HashMap<>();

	static {
		aggregates.put(AggregateType.group, new GroupAggregate());
		aggregates.put(AggregateType.select, new SelectAggregate());
		aggregates.put(AggregateType.reselect, new ReselectAggregate());
		aggregates.put(AggregateType.regroup, new RegroupAggregate());
		aggregates.put(AggregateType.avg, new AvgAggregate());
		aggregates.put(AggregateType.count, new CountAggregate());
		aggregates.put(AggregateType.sum, new SumAggregate());
		aggregates.put(AggregateType.min, new MinAggregate());
		aggregates.put(AggregateType.max, new MaxAggregate());
		aggregates.put(AggregateType.customgroup, new CustomGroupAggregate());
	}

	public static List<BindData> computeDatasetExpression(DatasetExpression expr, Cell cell, Context context) {
		AggregateType aggregateType = expr.getAggregate();
		Aggregate aggregate = aggregates.get(aggregateType);
		if (aggregate != null) {
			return aggregate.aggregate(expr, cell, context);
		} else {
			throw new CellComputeException("Unknow aggregate : " + aggregateType);
		}
	}
}
