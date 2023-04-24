package com.xjinyao.report.core.cache;

import com.xjinyao.report.core.chart.ChartData;
import com.xjinyao.report.core.definition.ReportDefinition;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class CacheUtils implements ApplicationContextAware {
	private static ReportCache reportCache;
	private static ReportDefinitionCache reportDefinitionCache;
	private static String CHART_DATA_key = "_chart_data_";

	@SuppressWarnings("unchecked")
	public static ChartData getChartData(String chartId) {
		String key = CHART_DATA_key;
		if (reportCache != null) {
			Map<String, ChartData> chartDataMap = (Map<String, ChartData>) reportCache.getObject(key);
			if (chartDataMap != null) {
				return chartDataMap.get(chartId);
			}
		}
		return null;
	}

	public static void storeChartDataMap(Map<String, ChartData> map) {
		String key = CHART_DATA_key;
		if (reportCache != null) {
			reportCache.storeObject(key, map);
		}
	}

	public static Object getObject(String file) {
		if (reportCache != null) {
			return reportCache.getObject(file);
		}
		return null;
	}

	public static void storeObject(String file, Object obj) {
		if (reportCache != null) {
			reportCache.storeObject(file, obj);
		}
	}

	public static ReportDefinition getReportDefinition(String file) {
		return reportDefinitionCache.getReportDefinition(file);
	}

	public static void cacheReportDefinition(String file, ReportDefinition reportDefinition) {
		reportDefinitionCache.cacheReportDefinition(file, reportDefinition);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Collection<ReportCache> coll = applicationContext.getBeansOfType(ReportCache.class).values();
		for (ReportCache cache : coll) {
			if (cache.disabled()) {
				continue;
			}
			reportCache = cache;
			break;
		}
		Collection<ReportDefinitionCache> reportCaches = applicationContext.getBeansOfType(ReportDefinitionCache.class).values();
		if (reportCaches.size() == 0) {
			reportDefinitionCache = new DefaultMemoryReportDefinitionCache();
		} else {
			reportDefinitionCache = reportCaches.iterator().next();
		}
	}
}
