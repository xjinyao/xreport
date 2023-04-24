package com.xjinyao.report.core.exception;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ReportParseException extends ReportException {
	private static final long serialVersionUID = -8757106306597844487L;

	public ReportParseException(Exception ex) {
		super(ex);
	}

	public ReportParseException(String msg) {
		super(msg);
	}
}
