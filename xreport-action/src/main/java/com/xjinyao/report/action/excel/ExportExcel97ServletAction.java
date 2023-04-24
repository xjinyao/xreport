package com.xjinyao.report.action.excel;

import com.xjinyao.report.action.BaseServletAction;
import com.xjinyao.report.action.cache.TempObjectCache;
import com.xjinyao.report.action.exception.ReportDesignException;
import com.xjinyao.report.core.build.ReportBuilder;
import com.xjinyao.report.core.definition.ReportDefinition;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.export.ExportConfigure;
import com.xjinyao.report.core.export.ExportConfigureImpl;
import com.xjinyao.report.core.export.ExportManager;
import com.xjinyao.report.core.export.excel.low.Excel97Producer;
import com.xjinyao.report.core.model.Report;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ExportExcel97ServletAction extends BaseServletAction {
	private ReportBuilder reportBuilder;
	private ExportManager exportManager;
	private Excel97Producer excelProducer = new Excel97Producer();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = retriveMethod(req);
		if (method != null) {
			invokeMethod(method, req, resp);
		} else {
			buildExcel(req, resp, false, false);
		}
	}

	public void paging(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		buildExcel(req, resp, true, false);
	}

	public void sheet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		buildExcel(req, resp, false, true);
	}

	public void buildExcel(HttpServletRequest req, HttpServletResponse resp, boolean withPage, boolean withSheet) throws IOException {
		String file = req.getParameter("_u");
		if (StringUtils.isBlank(file)) {
			throw new ReportComputeException("Report file can not be null.");
		}
		String fileName = req.getParameter("_n");
		if (StringUtils.isNotBlank(fileName)) {
			fileName = decode(fileName);
		} else {
			fileName = "xreport.xls";
		}
		resp.setContentType("application/octet-stream;charset=ISO8859-1");
		resp.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		Map<String, Object> parameters = buildParameters(req);
		OutputStream outputStream = resp.getOutputStream();
		if (file.equals(PREVIEW_KEY)) {
			ReportDefinition reportDefinition = (ReportDefinition) TempObjectCache.getObject(PREVIEW_KEY);
			if (reportDefinition == null) {
				throw new ReportDesignException("Report data has expired,can not do export excel.");
			}
			Report report = reportBuilder.buildReport(reportDefinition, parameters);
			if (withPage) {
				excelProducer.produceWithPaging(report, outputStream);
			} else if (withSheet) {
				excelProducer.produceWithSheet(report, outputStream);
			} else {
				excelProducer.produce(report, outputStream);
			}
		} else {
			ExportConfigure configure = new ExportConfigureImpl(file, parameters, outputStream);
			if (withPage) {
				exportManager.exportExcelWithPaging(configure);
			} else if (withSheet) {
				exportManager.exportExcelWithPagingSheet(configure);
			} else {
				exportManager.exportExcel(configure);
			}
		}
		outputStream.flush();
		outputStream.close();
	}

	public void setReportBuilder(ReportBuilder reportBuilder) {
		this.reportBuilder = reportBuilder;
	}

	public void setExportManager(ExportManager exportManager) {
		this.exportManager = exportManager;
	}

	@Override
	public String url() {
		return "/excel97";
	}
}
