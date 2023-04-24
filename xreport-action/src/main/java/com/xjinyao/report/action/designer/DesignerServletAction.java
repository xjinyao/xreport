package com.xjinyao.report.action.designer;

import com.xjinyao.report.action.RenderPageServletAction;
import com.xjinyao.report.action.cache.TempObjectCache;
import com.xjinyao.report.action.exception.ReportDesignException;
import com.xjinyao.report.core.cache.CacheUtils;
import com.xjinyao.report.core.definition.ReportDefinition;
import com.xjinyao.report.core.dsl.ReportParserLexer;
import com.xjinyao.report.core.dsl.ReportParserParser;
import com.xjinyao.report.core.dsl.ReportParserParser.DatasetContext;
import com.xjinyao.report.core.export.ReportRender;
import com.xjinyao.report.core.expression.ErrorInfo;
import com.xjinyao.report.core.expression.ScriptErrorListener;
import com.xjinyao.report.core.parser.ReportParser;
import com.xjinyao.report.core.provider.report.ReportFile;
import com.xjinyao.report.core.provider.report.ReportProvider;
import lombok.Setter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class DesignerServletAction extends RenderPageServletAction {
	private ReportRender reportRender;
	private ReportParser reportParser;
	@Setter
	private String gatewayUrlPrefix;

	private List<ReportProvider> reportProviders = new ArrayList<>();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = retriveMethod(req);
		if (method != null) {
			invokeMethod(method, req, resp);
		} else {
			VelocityContext context = new VelocityContext();
			String contextPath = req.getContextPath();
			String basePath = StringUtils.isNotBlank(gatewayUrlPrefix) ? (gatewayUrlPrefix + contextPath) : contextPath;
			context.put("contextPath", "/".equals(basePath) ? "" : basePath);
			resp.setContentType("text/html");
			resp.setCharacterEncoding("utf-8");
			Template template = ve.getTemplate("xreport-html/designer.html", "utf-8");
			PrintWriter writer = resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}

	public void scriptValidation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String content = req.getParameter("content");
		ANTLRInputStream antlrInputStream = new ANTLRInputStream(content);
		ReportParserLexer lexer = new ReportParserLexer(antlrInputStream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		ReportParserParser parser = new ReportParserParser(tokenStream);
		ScriptErrorListener errorListener = new ScriptErrorListener();
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);
		parser.expression();
		List<ErrorInfo> infos = errorListener.getInfos();
		writeObjectToJson(resp, infos);
	}

	public void conditionScriptValidation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String content = req.getParameter("content");
		ANTLRInputStream antlrInputStream = new ANTLRInputStream(content);
		ReportParserLexer lexer = new ReportParserLexer(antlrInputStream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		ReportParserParser parser = new ReportParserParser(tokenStream);
		ScriptErrorListener errorListener = new ScriptErrorListener();
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);
		parser.expr();
		List<ErrorInfo> infos = errorListener.getInfos();
		writeObjectToJson(resp, infos);
	}


	public void parseDatasetName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String expr = req.getParameter("expr");
		ANTLRInputStream antlrInputStream = new ANTLRInputStream(expr);
		ReportParserLexer lexer = new ReportParserLexer(antlrInputStream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		ReportParserParser parser = new ReportParserParser(tokenStream);
		parser.removeErrorListeners();
		DatasetContext ctx = parser.dataset();
		String datasetName = ctx.Identifier().getText();
		Map<String, String> result = new HashMap<>();
		result.put("datasetName", datasetName);
		writeObjectToJson(resp, result);
	}

	public void savePreviewData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String content = req.getParameter("content");
		content = decodeContent(content);
		InputStream inputStream = IOUtils.toInputStream(content, "utf-8");
		ReportDefinition reportDef = reportParser.parse(inputStream, "p");
		reportRender.rebuildReportDefinition(reportDef);
		IOUtils.closeQuietly(inputStream);
		TempObjectCache.putObject(PREVIEW_KEY, reportDef);
	}

	public void loadReport(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String file = req.getParameter("file");
		if (file == null) {
			throw new ReportDesignException("Report file can not be null.");
		}
		file = ReportUtils.decodeFileName(file);
		Object obj = TempObjectCache.getObject(file);
		if (obj != null && obj instanceof ReportDefinition) {
			ReportDefinition reportDef = (ReportDefinition) obj;
			TempObjectCache.removeObject(file);
			writeObjectToJson(resp, new ReportDefinitionWrapper(reportDef));
		} else {
			ReportDefinition reportDef = reportRender.parseReport(file);
			writeObjectToJson(resp, new ReportDefinitionWrapper(reportDef));
		}
	}

	public void loadReportInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String file = req.getParameter("file");
		if (file == null) {
			throw new ReportDesignException("Report file can not be null.");
		}
		file = ReportUtils.decodeFileName(file);
		ReportFile reportDef = reportRender.loadReportInfo(file);
		if (reportDef == null) {
			throw new ReportDesignException("Report file is not found.");
		}
		writeObjectToJson(resp, reportDef);

	}

	public void deleteReportFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String file = req.getParameter("file");
		if (file == null) {
			throw new ReportDesignException("Report file can not be null.");
		}
		ReportProvider targetReportProvider = null;
		for (ReportProvider provider : reportProviders) {
			if (file.startsWith(provider.getPrefix())) {
				targetReportProvider = provider;
				break;
			}
		}
		if (targetReportProvider == null) {
			throw new ReportDesignException("File [" + file + "] not found available report provider.");
		}
		targetReportProvider.deleteReport(file);
	}


	public void saveReportFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String file = req.getParameter("file");
		file = ReportUtils.decodeFileName(file);
		String content = req.getParameter("content");
		content = decodeContent(content);
		ReportProvider targetReportProvider = null;
		for (ReportProvider provider : reportProviders) {
			if (file.startsWith(provider.getPrefix())) {
				targetReportProvider = provider;
				break;
			}
		}
		if (targetReportProvider == null) {
			throw new ReportDesignException("File [" + file + "] not found available report provider.");
		}
		targetReportProvider.saveReport(file, content);
		InputStream inputStream = IOUtils.toInputStream(content, "utf-8");
		ReportDefinition reportDef = reportParser.parse(inputStream, file);
		reportRender.rebuildReportDefinition(reportDef);
		CacheUtils.cacheReportDefinition(file, reportDef);
		IOUtils.closeQuietly(inputStream);
	}

	public void loadReportProviders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		writeObjectToJson(resp, reportProviders);
	}

	public void setReportRender(ReportRender reportRender) {
		this.reportRender = reportRender;
	}

	public void setReportParser(ReportParser reportParser) {
		this.reportParser = reportParser;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		super.setApplicationContext(applicationContext);
		Collection<ReportProvider> providers = applicationContext.getBeansOfType(ReportProvider.class).values();
		for (ReportProvider provider : providers) {
			if (provider.disabled() || provider.getName() == null) {
				continue;
			}
			reportProviders.add(provider);
		}
	}

	@Override
	public String url() {
		return "/designer";
	}
}
