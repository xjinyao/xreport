package com.xjinyao.report.core.build.paging;

import com.xjinyao.report.core.definition.Band;
import com.xjinyao.report.core.definition.Paper;
import com.xjinyao.report.core.model.Report;
import com.xjinyao.report.core.model.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class FixRowsPagination extends BasePagination implements Pagination {
	@Override
	public List<Page> doPaging(Report report) {
		Paper paper = report.getPaper();
		List<Row> rows = report.getRows();
		List<Row> headerRows = report.getHeaderRepeatRows();
		List<Row> footerRows = report.getFooterRepeatRows();
		List<Row> titleRows = report.getTitleRows();
		List<Row> summaryRows = report.getSummaryRows();
		int fixRows = paper.getFixRows() - headerRows.size() - footerRows.size();
		if (fixRows < 0) {
			fixRows = 1;
		}
		List<Row> pageRepeatHeaders = new ArrayList<>();
		List<Row> pageRepeatFooters = new ArrayList<>();
		pageRepeatHeaders.addAll(headerRows);
		pageRepeatFooters.addAll(footerRows);
		List<Page> pages = new ArrayList<>();
		List<Row> pageRows = new ArrayList<>();
		int pageIndex = 1;
		for (Row row : rows) {
			int height = row.getRealHeight();
			if (height == 0) {
				continue;
			}
			Band band = row.getBand();
			if (band != null) {
				String rowKey = row.getRowKey();
				int index = -1;
				if (band.equals(Band.headerrepeat)) {
					for (int j = 0; j < pageRepeatHeaders.size(); j++) {
						Row headerRow = pageRepeatHeaders.get(j);
						if (headerRow.getRowKey().equals(rowKey)) {
							index = j;
							break;
						}
					}
					pageRepeatHeaders.remove(index);
					pageRepeatHeaders.add(index, row);
				} else if (band.equals(Band.footerrepeat)) {
					for (int j = 0; j < pageRepeatFooters.size(); j++) {
						Row footerRow = pageRepeatFooters.get(j);
						if (footerRow.getRowKey().equals(rowKey)) {
							index = j;
							break;
						}
					}
					pageRepeatFooters.remove(index);
					pageRepeatFooters.add(index, row);
				}
				continue;
			}
			pageRows.add(row);
			row.setPageIndex(pageIndex);
			if (pageRows.size() >= fixRows) {
				Page newPage = buildPage(pageRows, pageRepeatHeaders, pageRepeatFooters, titleRows, pageIndex, report);
				pageIndex++;
				pages.add(newPage);
				pageRows = new ArrayList<>();
			}
		}
		if (pageRows.size() > 0) {
			Page newPage = buildPage(pageRows, pageRepeatHeaders, pageRepeatFooters, titleRows, pageIndex, report);
			pageIndex++;
			pages.add(newPage);
		}
		report.getContext().setTotalPages(pages.size());
		buildPageHeaderFooter(pages, report);
		buildSummaryRows(summaryRows, pages);
		return pages;
	}
}
