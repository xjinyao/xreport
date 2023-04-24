package com.xjinyao.report.core.model;

import com.xjinyao.report.core.definition.CellStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public abstract class Line {
	private CellStyle customCellStyle;
	private List<Cell> cells = new ArrayList<>();

	public CellStyle getCustomCellStyle() {
		return customCellStyle;
	}

	public void setCustomCellStyle(CellStyle customCellStyle) {
		this.customCellStyle = customCellStyle;
	}

	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}
}
