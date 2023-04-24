package com.xjinyao.report.core.exception;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ReportException extends RuntimeException {
	private static final long serialVersionUID = 2970559370876740683L;

	public ReportException(String msg) {
		super(msg);
	}

	public ReportException(Exception ex) {
		super(ex);
		ex.printStackTrace();
	}
}
