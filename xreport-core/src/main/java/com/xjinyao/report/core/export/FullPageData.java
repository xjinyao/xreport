package com.xjinyao.report.core.export;

import com.xjinyao.report.core.build.paging.Page;

import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class FullPageData {
	private int totalPages;
	private int columnMargin;
	private List<List<Page>> pageList;

	public FullPageData(int totalPages, int columnMargin, List<List<Page>> pageList) {
		this.totalPages = totalPages;
		this.columnMargin = columnMargin;
		this.pageList = pageList;
	}

	public int getColumnMargin() {
		return columnMargin;
	}

	public List<List<Page>> getPageList() {
		return pageList;
	}

	public int getTotalPages() {
		return totalPages;
	}
}
