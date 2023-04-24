package com.xjinyao.report.core.exception;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ConvertException extends ReportException {
	private static final long serialVersionUID = 8681316352205087220L;

	public ConvertException(Exception ex) {
		super(ex);
	}

	public ConvertException(String msg) {
		super(msg);
	}
}
