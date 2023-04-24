package com.xjinyao.report.core.exception;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class DatasetUndefinitionException extends ReportException {
	private static final long serialVersionUID = -1897331038232057797L;

	public DatasetUndefinitionException(String datasetName) {
		super("Dataset [" + datasetName + "] not definition.");
	}
}
