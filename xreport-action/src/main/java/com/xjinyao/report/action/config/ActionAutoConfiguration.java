package com.xjinyao.report.action.config;

import com.xjinyao.report.action.cache.HttpSessionReportCache;
import com.xjinyao.report.action.chart.ChartServletAction;
import com.xjinyao.report.action.designer.DatasourceServletAction;
import com.xjinyao.report.action.designer.DesignerServletAction;
import com.xjinyao.report.action.designer.SearchFormDesignerAction;
import com.xjinyao.report.action.excel.ExportExcel97ServletAction;
import com.xjinyao.report.action.excel.ExportExcelServletAction;
import com.xjinyao.report.action.html.HtmlPreviewServletAction;
import com.xjinyao.report.action.image.ImageServletAction;
import com.xjinyao.report.action.importexcel.ImportExcelServletAction;
import com.xjinyao.report.action.pdf.ExportPdfServletAction;
import com.xjinyao.report.action.res.ResourceLoaderServletAction;
import com.xjinyao.report.action.word.ExportWordServletAction;
import com.xjinyao.report.core.build.ReportBuilder;
import com.xjinyao.report.core.export.ExportManager;
import com.xjinyao.report.core.export.ReportRender;
import com.xjinyao.report.core.parser.ReportParser;
import com.xjinyao.report.core.properties.XReportProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author 谢进伟
 * @createDate 2023/4/24 09:46
 */
@EnableConfigurationProperties(XReportProperties.class)
public class ActionAutoConfiguration {


	@Bean
	public DatasourceServletAction datasourceServletAction() {
		return new DatasourceServletAction();
	}

	@Bean
	public ResourceLoaderServletAction ResourceLoaderServletAction() {
		return new ResourceLoaderServletAction();
	}

	@Bean
	public DesignerServletAction designerServletAction(ReportRender reportRender,
													   ReportParser reportParser,
													   XReportProperties xReportProperties) {
		DesignerServletAction designerServletAction = new DesignerServletAction();
		designerServletAction.setReportRender(reportRender);
		designerServletAction.setReportParser(reportParser);
		designerServletAction.setGatewayUrlPrefix(xReportProperties.getGatewayUrlPrefix());
		return designerServletAction;
	}

	@Bean
	public SearchFormDesignerAction searchFormDesignerAction(XReportProperties xReportProperties) {
		SearchFormDesignerAction searchFormDesignerAction = new SearchFormDesignerAction();
		searchFormDesignerAction.setGatewayUrlPrefix(xReportProperties.getGatewayUrlPrefix());
		return searchFormDesignerAction;
	}

	@Bean
	public HtmlPreviewServletAction htmlPreviewServletAction(ExportManager exportManager,
															 ReportBuilder reportBuilder,
															 ReportRender reportRender) {
		HtmlPreviewServletAction htmlPreviewServletAction = new HtmlPreviewServletAction();
		htmlPreviewServletAction.setExportManager(exportManager);
		htmlPreviewServletAction.setReportBuilder(reportBuilder);
		htmlPreviewServletAction.setReportRender(reportRender);
		return htmlPreviewServletAction;
	}

	@Bean
	public ExportWordServletAction exportWordServletAction(ExportManager exportManager,
														   ReportBuilder reportBuilder) {
		ExportWordServletAction exportWordServletAction = new ExportWordServletAction();
		exportWordServletAction.setExportManager(exportManager);
		exportWordServletAction.setReportBuilder(reportBuilder);
		return exportWordServletAction;
	}

	@Bean
	public ExportPdfServletAction exportPdfServletAction(ExportManager exportManager,
														 ReportBuilder reportBuilder,
														 ReportRender reportRender) {
		ExportPdfServletAction exportPdfServletAction = new ExportPdfServletAction();
		exportPdfServletAction.setExportManager(exportManager);
		exportPdfServletAction.setReportBuilder(reportBuilder);
		exportPdfServletAction.setReportRender(reportRender);
		return exportPdfServletAction;
	}

	@Bean
	public ExportExcelServletAction exportExcelServletAction(ExportManager exportManager,
															 ReportBuilder reportBuilder) {
		ExportExcelServletAction exportExcelServletAction = new ExportExcelServletAction();
		exportExcelServletAction.setExportManager(exportManager);
		exportExcelServletAction.setReportBuilder(reportBuilder);
		return exportExcelServletAction;
	}

	@Bean
	public ExportExcel97ServletAction exportExcel97ServletAction(ExportManager exportManager,
																 ReportBuilder reportBuilder) {
		ExportExcel97ServletAction exportExcel97ServletAction = new ExportExcel97ServletAction();
		exportExcel97ServletAction.setExportManager(exportManager);
		exportExcel97ServletAction.setReportBuilder(reportBuilder);
		return exportExcel97ServletAction;
	}

	@Bean
	public ImageServletAction imageServletAction() {
		return new ImageServletAction();
	}

	@Bean
	public ImportExcelServletAction importExcelServletAction() {
		return new ImportExcelServletAction();
	}

	@Bean
	public ChartServletAction chartServletAction() {
		return new ChartServletAction();
	}

	@Bean
	public HttpSessionReportCache httpSessionReportCache() {
		return new HttpSessionReportCache();
	}
}
