package com.xjinyao.report.core.exception;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class CellComputeException extends ReportException {
	private static final long serialVersionUID = -1363254031247074841L;

	public CellComputeException(Exception ex) {
		super(ex);
	}

	public CellComputeException(String msg) {
		super(msg);
	}
}
