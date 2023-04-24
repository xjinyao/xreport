package com.xjinyao.report.core.expression.model.expr.set;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class CellCoordinateSet {
	private List<CellCoordinate> cellCoordinates;

	public CellCoordinateSet(List<CellCoordinate> cellCoordinates) {
		this.cellCoordinates = cellCoordinates;
	}

	public List<CellCoordinate> getCellCoordinates() {
		return cellCoordinates;
	}
}
