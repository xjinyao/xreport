package com.xjinyao.report.core.cache;

import com.xjinyao.report.core.definition.ReportDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class DefaultMemoryReportDefinitionCache implements ReportDefinitionCache {
	private Map<String, ReportDefinition> reportMap = new ConcurrentHashMap<>();

	@Override
	public ReportDefinition getReportDefinition(String file) {
		return reportMap.get(file);
	}

	@Override
	public void cacheReportDefinition(String file, ReportDefinition reportDefinition) {
		if (reportMap.containsKey(file)) {
			reportMap.remove(file);
		}
		reportMap.put(file, reportDefinition);
	}
}
