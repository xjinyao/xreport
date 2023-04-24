package com.xjinyao.report.core.expression.model.expr.set;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.expression.model.Condition;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.NoneExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectListExpressionData;
import com.xjinyao.report.core.model.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class CellConditionExpression extends CellExpression {
	private static final long serialVersionUID = 536887481808944331L;
	protected Condition condition;

	public CellConditionExpression(String cellName, Condition condition) {
		super(cellName);
		this.condition = condition;
	}

	@Override
	protected ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		List<Cell> targetCells = Utils.fetchTargetCells(cell, context, cellName);
		targetCells = filterCells(cell, context, condition, targetCells);
		if (targetCells.size() > 1) {
			List<Object> list = new ArrayList<>();
			for (Cell targetCell : targetCells) {
				list.add(targetCell.getData());
			}
			return new ObjectListExpressionData(list);
		} else if (targetCells.size() == 1) {
			return new ObjectExpressionData(targetCells.get(0).getData());
		} else {
			return new NoneExpressionData();
		}
	}

	@Override
	public ExpressionData<?> computePageCells(Cell cell, Cell currentCell, Context context) {
		List<Cell> targetCells = fetchPageCells(cell, currentCell, context);
		targetCells = filterCells(cell, context, condition, targetCells);
		if (targetCells.size() > 1) {
			List<Object> list = new ArrayList<>();
			for (Cell targetCell : targetCells) {
				list.add(targetCell.getData());
			}
			return new ObjectListExpressionData(list);
		} else if (targetCells.size() == 1) {
			return new ObjectExpressionData(targetCells.get(0).getData());
		} else {
			return new NoneExpressionData();
		}
	}
}
