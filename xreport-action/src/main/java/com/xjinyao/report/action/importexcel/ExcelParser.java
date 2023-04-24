package com.xjinyao.report.action.importexcel;

import com.xjinyao.report.core.definition.ReportDefinition;

import java.io.InputStream;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public abstract class ExcelParser {
	public abstract ReportDefinition parse(InputStream inputStream) throws Exception;

	public abstract boolean support(String name);
}
