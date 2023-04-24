package com.xjinyao.report.core.cache;


/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface ReportCache {
	Object getObject(String file);

	void storeObject(String file, Object obj);

	boolean disabled();
}
