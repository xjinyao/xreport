package com.xjinyao.report.core.chart.plugins;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface Plugin {
	String toJson(String type);

	String getName();
}
