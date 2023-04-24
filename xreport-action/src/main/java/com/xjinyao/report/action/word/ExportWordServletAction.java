package com.xjinyao.report.action.word;

import com.xjinyao.report.action.BaseServletAction;
import com.xjinyao.report.action.cache.TempObjectCache;
import com.xjinyao.report.action.exception.ReportDesignException;
import com.xjinyao.report.core.build.ReportBuilder;
import com.xjinyao.report.core.definition.ReportDefinition;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.export.ExportConfigure;
import com.xjinyao.report.core.export.ExportConfigureImpl;
import com.xjinyao.report.core.export.ExportManager;
import com.xjinyao.report.core.export.word.high.WordProducer;
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
public class ExportWordServletAction extends BaseServletAction {
	private ReportBuilder reportBuilder;
	private ExportManager exportManager;
	private WordProducer wordProducer = new WordProducer();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = retriveMethod(req);
		if (method != null) {
			invokeMethod(method, req, resp);
		} else {
			buildWord(req, resp);
		}
	}

	public void buildWord(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String file = req.getParameter("_u");
		file = decode(file);
		if (StringUtils.isBlank(file)) {
			throw new ReportComputeException("Report file can not be null.");
		}
		String fileName = req.getParameter("_n");
		fileName = buildDownloadFileName(file, fileName, ".docx");
		fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		resp.setContentType("application/octet-stream;charset=ISO8859-1");
		resp.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		Map<String, Object> parameters = buildParameters(req);
		OutputStream outputStream = resp.getOutputStream();
		if (file.equals(PREVIEW_KEY)) {
			ReportDefinition reportDefinition = (ReportDefinition) TempObjectCache.getObject(PREVIEW_KEY);
			if (reportDefinition == null) {
				throw new ReportDesignException("Report data has expired,can not do export word.");
			}
			Report report = reportBuilder.buildReport(reportDefinition, parameters);
			wordProducer.produce(report, outputStream);
		} else {
			ExportConfigure configure = new ExportConfigureImpl(file, parameters, outputStream);
			exportManager.exportWord(configure);
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
		return "/word";
	}
}
