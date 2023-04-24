package com.xjinyao.report.core.exception;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ReportComputeException extends ReportException {
	private static final long serialVersionUID = -5079596691655241415L;

	public ReportComputeException(Exception ex) {
		super(ex);
	}

	public ReportComputeException(String msg) {
		super(msg);
	}
}
