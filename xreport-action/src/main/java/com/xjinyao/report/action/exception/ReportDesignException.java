package com.xjinyao.report.action.exception;

import com.xjinyao.report.core.exception.ReportException;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ReportDesignException extends ReportException {
	private static final long serialVersionUID = 4046240733455821337L;

	public ReportDesignException(Exception ex) {
		super(ex);
	}

	public ReportDesignException(String msg) {
		super(msg);
	}
}
