package com.xjinyao.report.core.build.cell.right;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.model.Cell;
import com.xjinyao.report.core.model.Column;
import com.xjinyao.report.core.model.Report;

import java.util.*;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class RightDuplicate {
	private int index;
	private int columnSize;
	private Context context;
	private Cell mainCell;
	private int minColNumber = -1;
	private Map<Column, Column> colMap = new HashMap<>();
	private List<Column> newColList = new ArrayList<>();

	public RightDuplicate(Cell mainCell, int columnSize, Context context) {
		this.mainCell = mainCell;
		this.columnSize = columnSize;
		this.context = context;
	}

	public Column newColumn(Column col, int colNumber) {
		if (colMap.containsKey(col)) {
			return colMap.get(col);
		} else {
			Column newCol = col.newColumn();
			colNumber = colNumber + columnSize * (index - 1) + columnSize;
			if (minColNumber == -1 || minColNumber > colNumber) {
				minColNumber = colNumber;
			}
			newCol.setTempColumnNumber(colNumber);
			newColList.add(newCol);
			colMap.put(col, newCol);
			return newCol;
		}
	}

	public int getColSize() {
		return columnSize;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Context getContext() {
		return context;
	}

	public Cell getMainCell() {
		return mainCell;
	}

	public void complete() {
		if (minColNumber < 1) {
			return;
		}
		Report report = context.getReport();
		Collections.sort(newColList, new Comparator<Column>() {
			@Override
			public int compare(Column o1, Column o2) {
				return o1.getTempColumnNumber() - o2.getTempColumnNumber();
			}
		});
		report.insertColumns(minColNumber, newColList);
	}

	public void reset() {
		colMap.clear();
	}
}
