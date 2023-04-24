package com.xjinyao.report.core.parser;

import com.xjinyao.report.core.definition.*;
import com.xjinyao.report.core.definition.*;
import com.xjinyao.report.core.definition.datasource.DatasourceDefinition;
import com.xjinyao.report.core.definition.searchform.SearchForm;
import com.xjinyao.report.core.exception.ReportException;
import com.xjinyao.report.core.exception.ReportParseException;
import com.xjinyao.report.core.parser.impl.*;
import com.xjinyao.report.core.parser.impl.searchform.SearchFormParser;
import com.xjinyao.report.core.parser.impl.*;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.*;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ReportParser {
	private Map<String, Parser<?>> parsers = new HashMap<>();

	public ReportParser() {
		parsers.put("row", new RowParser());
		parsers.put("column", new ColumnParser());
		parsers.put("cell", new CellParser());
		parsers.put("datasource", new DatasourceParser());
		parsers.put("paper", new PaperParser());
		parsers.put("header", new HeaderFooterParser());
		parsers.put("footer", new HeaderFooterParser());
		parsers.put("search-form", new SearchFormParser());
	}

	public ReportDefinition parse(InputStream inputStream, String file) {
		ReportDefinition report = new ReportDefinition();
		report.setReportFullName(file);
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(inputStream);
			Element element = document.getRootElement();
			if (!element.getName().equals("xreport")) {
				throw new ReportParseException("Unknow report file.");
			}
			List<RowDefinition> rows = new ArrayList<>();
			List<ColumnDefinition> columns = new ArrayList<>();
			List<CellDefinition> cells = new ArrayList<>();
			List<DatasourceDefinition> datasources = new ArrayList<>();
			report.setRows(rows);
			report.setColumns(columns);
			report.setCells(cells);
			report.setDatasources(datasources);
			for (Object obj : element.elements()) {
				if (obj == null || !(obj instanceof Element)) {
					continue;
				}
				Element ele = (Element) obj;
				Parser<?> parser = parsers.get(ele.getName());
				if (parser != null) {
					Object target = parser.parse(ele);
					if (target instanceof RowDefinition) {
						rows.add((RowDefinition) target);
					} else if (target instanceof ColumnDefinition) {
						columns.add((ColumnDefinition) target);
					} else if (target instanceof CellDefinition) {
						cells.add((CellDefinition) target);
					} else if (target instanceof DatasourceDefinition) {
						datasources.add((DatasourceDefinition) target);
					} else if (target instanceof Paper) {
						Paper paper = (Paper) target;
						report.setPaper(paper);
					} else if (target instanceof HeaderFooterDefinition) {
						HeaderFooterDefinition hf = (HeaderFooterDefinition) target;
						if (ele.getName().equals("header")) {
							report.setHeader(hf);
						} else {
							report.setFooter(hf);
						}
					} else if (target instanceof SearchForm) {
						SearchForm form = (SearchForm) target;
						report.setSearchForm(form);
						report.setSearchFormXml(ele.asXML());
					}
				} else {
					throw new ReportParseException("Unknow element :" + ele.getName());
				}
			}
			Collections.sort(rows);
			Collections.sort(columns);
		} catch (Exception ex) {
			throw new ReportParseException(ex);
		}
		rebuild(report);
		return report;
	}

	private void rebuild(ReportDefinition report) {
		List<CellDefinition> cells = report.getCells();
		Map<String, CellDefinition> cellsMap = new HashMap<>();
		Map<String, CellDefinition> cellsRowColMap = new HashMap<>();
		for (CellDefinition cell : cells) {
			cellsMap.put(cell.getName(), cell);
			int rowNum = cell.getRowNumber(), colNum = cell.getColumnNumber(), rowSpan = cell.getRowSpan(), colSpan = cell.getColSpan();
			rowSpan = rowSpan > 0 ? rowSpan-- : 1;
			colSpan = colSpan > 0 ? colSpan-- : 1;
			int rowStart = rowNum, rowEnd = rowNum + rowSpan, colStart = colNum, colEnd = colNum + colSpan;
			for (int i = rowStart; i < rowEnd; i++) {
				cellsRowColMap.put(i + "," + colNum, cell);
			}
			for (int i = colStart; i < colEnd; i++) {
				cellsRowColMap.put(rowNum + "," + i, cell);
			}
		}
		for (CellDefinition cell : cells) {
			int rowNumber = cell.getRowNumber();
			int colNumber = cell.getColumnNumber();
			String leftParentCellName = cell.getLeftParentCellName();
			if (StringUtils.isNotBlank(leftParentCellName)) {
				if (!leftParentCellName.equals("root")) {
					CellDefinition targetCell = cellsMap.get(leftParentCellName);
					if (targetCell == null) {
						throw new ReportException("Cell [" + cell.getName() + "] 's left parent cell [" + leftParentCellName + "] not exist.");
					}
					cell.setLeftParentCell(targetCell);
				}
			} else {
				if (colNumber > 1) {
					CellDefinition targetCell = cellsRowColMap.get(rowNumber + "," + (colNumber - 1));
					cell.setLeftParentCell(targetCell);
				}
			}
			String topParentCellName = cell.getTopParentCellName();
			if (StringUtils.isNotBlank(topParentCellName)) {
				if (!topParentCellName.equals("root")) {
					CellDefinition targetCell = cellsMap.get(topParentCellName);
					if (targetCell == null) {
						throw new ReportException("Cell [" + cell.getName() + "] 's top parent cell [" + topParentCellName + "] not exist.");
					}
					cell.setTopParentCell(targetCell);
				}
			} else {
				if (rowNumber > 1) {
					CellDefinition targetCell = cellsRowColMap.get((rowNumber - 1) + "," + colNumber);
					cell.setTopParentCell(targetCell);
				}
			}
		}
	}
}
