package com.xjinyao.report.core.exception;

import com.xjinyao.report.core.model.ReportCell;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class IllegalCellExpandException extends ReportException {
	private static final long serialVersionUID = -2442986317129037490L;

	public IllegalCellExpandException(ReportCell cell) {
		super("Cell expand is " + cell.getExpand() + " is invalid.");
	}
}
