package com.xjinyao.report.core.expression.model.expr;

import com.xjinyao.report.core.Utils;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectListExpressionData;
import com.xjinyao.report.core.expression.model.expr.set.CellExpression;
import com.xjinyao.report.core.model.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class RelativeCellExpression extends CellExpression {
	private static final long serialVersionUID = 8826396779392348224L;

	public RelativeCellExpression(String cellName) {
		super(cellName);
	}

	@Override
	public boolean supportPaging() {
		return false;
	}

	@Override
	protected ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		List<Cell> targetCells = Utils.fetchTargetCells(currentCell, context, cellName);
		int size = targetCells.size();
		if (size == 0) {
			throw new ReportComputeException("Unknow cell " + cellName);
		} else if (size == 1) {
			return new ObjectExpressionData(targetCells.get(0).getData());
		} else {
			Cell targetCell = null;
			for (Cell c : targetCells) {
				if (c.getRow() == currentCell.getRow() || c.getColumn() == currentCell.getColumn()) {
					targetCell = c;
					break;
				}
			}
			if (targetCell != null) {
				return new ObjectExpressionData(targetCell.getData());
			} else {
				List<Object> list = new ArrayList<>();
				for (Cell c : targetCells) {
					list.add(c.getData());
				}
				return new ObjectListExpressionData(list);
			}
		}
	}
}
