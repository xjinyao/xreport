package com.xjinyao.report.core.build.cell.down;

import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.model.Cell;
import com.xjinyao.report.core.model.Report;
import com.xjinyao.report.core.model.Row;

import java.util.*;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class DownDuplicate {
	private int index;
	private Cell mainCell;
	private int rowSize;
	private Context context;
	private int minRowNumber = -1;
	private Map<Row, Row> rowMap = new HashMap<>();
	private List<Row> newRowList = new ArrayList<>();

	public DownDuplicate(Cell mainCell, int rowSize, Context context) {
		this.mainCell = mainCell;
		this.rowSize = rowSize;
		this.context = context;
	}

	public Row newRow(Row row, int currentRowNumber) {
		if (rowMap.containsKey(row)) {
			return rowMap.get(row);
		} else {
			int rowNumber = currentRowNumber;
			Row newRow = row.newRow();
			rowNumber = rowNumber + rowSize * (index - 1) + rowSize;
			if (minRowNumber == -1 || minRowNumber > rowNumber) {
				minRowNumber = rowNumber;
			}
			newRow.setTempRowNumber(rowNumber);
			newRowList.add(newRow);
			rowMap.put(row, newRow);
			return newRow;
		}
	}

	public Cell getMainCell() {
		return mainCell;
	}

	public int getRowSize() {
		return rowSize;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Context getContext() {
		return context;
	}

	public void complete() {
		if (minRowNumber < 1) {
			return;
		}
		Report report = context.getReport();
		Collections.sort(newRowList, new Comparator<Row>() {
			@Override
			public int compare(Row o1, Row o2) {
				return o1.getTempRowNumber() - o2.getTempRowNumber();
			}
		});
		report.insertRows(minRowNumber, newRowList);
	}

	public void reset() {
		rowMap.clear();
	}
}
