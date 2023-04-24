package com.xjinyao.report.action.importexcel;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class Span {
	private int rowSpan;
	private int colSpan;

	public Span(int rowSpan, int colSpan) {
		this.rowSpan = rowSpan;
		this.colSpan = colSpan;
	}

	public int getRowSpan() {
		return rowSpan;
	}

	public int getColSpan() {
		return colSpan;
	}
}
