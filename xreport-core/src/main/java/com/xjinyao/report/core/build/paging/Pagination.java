package com.xjinyao.report.core.build.paging;

import com.xjinyao.report.core.model.Report;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public interface Pagination {
	List<Page> doPaging(Report report);
}
