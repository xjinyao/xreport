package com.xjinyao.report.action.pdf;

import com.xjinyao.report.action.BaseServletAction;
import com.xjinyao.report.action.cache.TempObjectCache;
import com.xjinyao.report.action.exception.ReportDesignException;
import com.xjinyao.report.core.build.ReportBuilder;
import com.xjinyao.report.core.definition.Paper;
import com.xjinyao.report.core.definition.ReportDefinition;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.export.ExportConfigure;
import com.xjinyao.report.core.export.ExportConfigureImpl;
import com.xjinyao.report.core.export.ExportManager;
import com.xjinyao.report.core.export.ReportRender;
import com.xjinyao.report.core.export.pdf.PdfProducer;
import com.xjinyao.report.core.model.Report;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

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
public class ExportPdfServletAction extends BaseServletAction {
	private ReportBuilder reportBuilder;
	private ExportManager exportManager;
	private ReportRender reportRender;
	private PdfProducer pdfProducer = new PdfProducer();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = retriveMethod(req);
		if (method != null) {
			invokeMethod(method, req, resp);
		} else {
			buildPdf(req, resp, false);
		}
	}

	public void show(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		buildPdf(req, resp, true);
	}

	public void buildPdf(HttpServletRequest req, HttpServletResponse resp, boolean forPrint) throws IOException {
		String file = req.getParameter("_u");
		file = decode(file);
		if (StringUtils.isBlank(file)) {
			throw new ReportComputeException("Report file can not be null.");
		}
		String fileName = req.getParameter("_n");
		fileName = buildDownloadFileName(file, fileName, ".pdf");
		fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		if (forPrint) {
			resp.setContentType("application/pdf");
			resp.setHeader("Content-Disposition", "inline;filename=\"" + fileName + "\"");
		} else {
			resp.setContentType("application/octet-stream;charset=ISO8859-1");
			resp.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		}
		Map<String, Object> parameters = buildParameters(req);
		OutputStream outputStream = resp.getOutputStream();
		if (file.equals(PREVIEW_KEY)) {
			ReportDefinition reportDefinition = (ReportDefinition) TempObjectCache.getObject(PREVIEW_KEY);
			if (reportDefinition == null) {
				throw new ReportDesignException("Report data has expired,can not do export pdf.");
			}
			Report report = reportBuilder.buildReport(reportDefinition, parameters);
			pdfProducer.produce(report, outputStream);
		} else {
			ExportConfigure configure = new ExportConfigureImpl(file, parameters, outputStream);
			exportManager.exportPdf(configure);
		}
		outputStream.flush();
		outputStream.close();
	}

	public void newPaging(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String file = req.getParameter("_u");
		if (StringUtils.isBlank(file)) {
			throw new ReportComputeException("Report file can not be null.");
		}
		Report report = null;
		Map<String, Object> parameters = buildParameters(req);
		if (file.equals(PREVIEW_KEY)) {
			ReportDefinition reportDefinition = (ReportDefinition) TempObjectCache.getObject(PREVIEW_KEY);
			if (reportDefinition == null) {
				throw new ReportDesignException("Report data has expired,can not do export pdf.");
			}
			report = reportBuilder.buildReport(reportDefinition, parameters);
		} else {
			ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
			report = reportRender.render(reportDefinition, parameters);
		}
		String paper = req.getParameter("_paper");
		ObjectMapper mapper = new ObjectMapper();
		Paper newPaper = mapper.readValue(paper, Paper.class);
		report.rePaging(newPaper);
	}

	public void setReportRender(ReportRender reportRender) {
		this.reportRender = reportRender;
	}

	public void setExportManager(ExportManager exportManager) {
		this.exportManager = exportManager;
	}

	public void setReportBuilder(ReportBuilder reportBuilder) {
		this.reportBuilder = reportBuilder;
	}

	@Override
	public String url() {
		return "/pdf";
	}
}
