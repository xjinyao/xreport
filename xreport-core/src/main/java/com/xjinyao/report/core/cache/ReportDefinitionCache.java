package com.xjinyao.report.core.cache;

import com.xjinyao.report.core.definition.ReportDefinition;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface ReportDefinitionCache {
	ReportDefinition getReportDefinition(String file);

	void cacheReportDefinition(String file, ReportDefinition reportDefinition);
}
