package com.xjinyao.report.core.exception;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class CellNotExistException extends ReportException {
	private static final long serialVersionUID = -2436297948073253411L;

	public CellNotExistException(String cellName) {
		super("Cell [" + cellName + "] not exist.");
	}
}
