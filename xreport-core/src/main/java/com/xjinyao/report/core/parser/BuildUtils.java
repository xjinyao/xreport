package com.xjinyao.report.core.parser;

import com.xjinyao.report.core.definition.CellDefinition;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class BuildUtils {
	public static int buildRowNumberEnd(CellDefinition cell, int rowNumber) {
		int rowSpan = cell.getRowSpan();
		rowSpan = rowSpan > 0 ? rowSpan - 1 : rowSpan;
		return rowNumber + rowSpan;
	}

	public static int buildColNumberEnd(CellDefinition cell, int colNumber) {
		int colSpan = cell.getColSpan();
		colSpan = colSpan > 0 ? colSpan - 1 : colSpan;
		return colNumber + colSpan;
	}
}
