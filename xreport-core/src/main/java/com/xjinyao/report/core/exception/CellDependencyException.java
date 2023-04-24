package com.xjinyao.report.core.exception;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class CellDependencyException extends ReportException {

	private static final long serialVersionUID = 5765713360910995235L;

	public CellDependencyException() {
		super("Report cells has cyclic dependency.");
	}
}
