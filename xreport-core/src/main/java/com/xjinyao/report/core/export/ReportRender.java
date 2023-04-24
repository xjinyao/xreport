package com.xjinyao.report.core.export;

import com.xjinyao.report.core.build.ReportBuilder;
import com.xjinyao.report.core.cache.CacheUtils;
import com.xjinyao.report.core.definition.CellDefinition;
import com.xjinyao.report.core.definition.Expand;
import com.xjinyao.report.core.definition.ReportDefinition;
import com.xjinyao.report.core.exception.ReportException;
import com.xjinyao.report.core.exception.ReportParseException;
import com.xjinyao.report.core.export.builder.down.DownCellbuilder;
import com.xjinyao.report.core.export.builder.right.RightCellbuilder;
import com.xjinyao.report.core.model.Report;
import com.xjinyao.report.core.parser.ReportParser;
import com.xjinyao.report.core.provider.report.ReportFile;
import com.xjinyao.report.core.provider.report.ReportProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ReportRender implements ApplicationContextAware {
	private ReportParser reportParser;
	private ReportBuilder reportBuilder;
	private Collection<ReportProvider> reportProviders;
	private DownCellbuilder downCellParentbuilder = new DownCellbuilder();
	private RightCellbuilder rightCellParentbuilder = new RightCellbuilder();

	public Report render(String file, Map<String, Object> parameters) {
		ReportDefinition reportDefinition = getReportDefinition(file);
		return reportBuilder.buildReport(reportDefinition, parameters);
	}

	public Report render(ReportDefinition reportDefinition, Map<String, Object> parameters) {
		return reportBuilder.buildReport(reportDefinition, parameters);
	}

	public ReportDefinition getReportDefinition(String file) {
		ReportDefinition reportDefinition = CacheUtils.getReportDefinition(file);
		if (reportDefinition == null) {
			reportDefinition = parseReport(file);
			rebuildReportDefinition(reportDefinition);
			CacheUtils.cacheReportDefinition(file, reportDefinition);
		}
		return reportDefinition;
	}

	public void rebuildReportDefinition(ReportDefinition reportDefinition) {
		List<CellDefinition> cells = reportDefinition.getCells();
		for (CellDefinition cell : cells) {
			addRowChildCell(cell, cell);
			addColumnChildCell(cell, cell);
		}
		for (CellDefinition cell : cells) {
			Expand expand = cell.getExpand();
			if (expand.equals(Expand.Down)) {
				downCellParentbuilder.buildParentCell(cell, cells);
			} else if (expand.equals(Expand.Right)) {
				rightCellParentbuilder.buildParentCell(cell, cells);
			}
		}
	}

	public ReportDefinition parseReport(String file) {
		InputStream inputStream = null;
		try {
			inputStream = buildReportFile(file);
			ReportDefinition reportDefinition = reportParser.parse(inputStream, file);
			return reportDefinition;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				throw new ReportParseException(e);
			}
		}
	}

	public ReportFile loadReportInfo(String file) {
		ReportFile reportFile = null;
		for (ReportProvider provider : reportProviders) {
			if (file.startsWith(provider.getPrefix())) {
				reportFile = provider.loadReportInfo(file);
			}
		}
		return reportFile;
	}


	private InputStream buildReportFile(String file) {
		InputStream inputStream = null;
		for (ReportProvider provider : reportProviders) {
			if (file.startsWith(provider.getPrefix())) {
				inputStream = provider.loadReport(file);
			}
		}
		if (inputStream == null) {
			throw new ReportException("Report [" + file + "] not support.");
		}
		return inputStream;
	}

	private void addRowChildCell(CellDefinition cell, CellDefinition childCell) {
		CellDefinition leftCell = cell.getLeftParentCell();
		if (leftCell == null) {
			return;
		}
		List<CellDefinition> childrenCells = leftCell.getRowChildrenCells();
		childrenCells.add(childCell);
		addRowChildCell(leftCell, childCell);
	}

	private void addColumnChildCell(CellDefinition cell, CellDefinition childCell) {
		CellDefinition topCell = cell.getTopParentCell();
		if (topCell == null) {
			return;
		}
		List<CellDefinition> childrenCells = topCell.getColumnChildrenCells();
		childrenCells.add(childCell);
		addColumnChildCell(topCell, childCell);
	}

	public void setReportParser(ReportParser reportParser) {
		this.reportParser = reportParser;
	}

	public void setReportBuilder(ReportBuilder reportBuilder) {
		this.reportBuilder = reportBuilder;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		reportProviders = applicationContext.getBeansOfType(ReportProvider.class).values();
	}
}
